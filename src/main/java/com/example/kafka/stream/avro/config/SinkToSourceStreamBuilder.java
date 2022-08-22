package com.example.kafka.stream.avro.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.RecordContext;
import org.apache.kafka.streams.processor.TopicNameExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.kafka.stream.avro.model.UserSink;
import com.example.kafka.stream.avro.model.UserSinkSource;
import com.example.kafka.stream.avro.model.UserSource;
import com.example.kafka.stream.avro.model.UserSourceOne;
import com.example.kafka.stream.avro.model.UserSourceTwo;
import com.example.kafka.stream.avro.type.AvroStreamConstant;
import com.example.kafka.stream.avro.type.AvroStreamUtil;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

@Component
public class SinkToSourceStreamBuilder {

    @Autowired
    private AvroStreamUtil avroStreamUtil;

    @PostConstruct
    public void buildStream() {

        Properties config = avroStreamUtil.getDefaultProps("avro-user-source-enrich-application");

        Map<String, Object> configMap = new HashMap<>();
        config.forEach((key, value) -> configMap.put(key.toString(), value));

        Serde<UserSinkSource> userSinkSerde = new SpecificAvroSerde<>();
        userSinkSerde.configure(configMap, false);

        Serde<UserSourceOne> userSourceOneSerde = new SpecificAvroSerde<>();
        userSourceOneSerde.configure(configMap, false);

        Serde<UserSourceTwo> userSourceTwoSerde = new SpecificAvroSerde<>();
        userSourceTwoSerde.configure(configMap, false);

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, UserSinkSource> userSinkKStream = builder
            .stream(AvroStreamConstant.Topic.USER_SINK_SOURCE, Consumed.with(Serdes.String(), userSinkSerde));
        TopicNameExtractor topicExtractor = new TopicNameExtractor() {
            @Override
            public String extract(Object key, Object value, RecordContext recordContext) {
                if (value instanceof UserSourceTwo) {
                    return AvroStreamConstant.Topic.USER_SOURCE_2;
                }
                return AvroStreamConstant.Topic.USER_SOURCE;
            }
        };

        userSinkKStream
            .filter((key, value) -> AvroStreamConstant.MessageType.SOURCE_1.equals(value.getPlusSinkSource()))
            .map((key, sink) -> {
                UserSourceOne userSource = UserSourceOne.newBuilder()
                    .setNameSourceOne(sink.getNameSinkSource())
                    .setAgeSourceOne(sink.getAgeSink())
                    .build();
                return new KeyValue<>(key, userSource);
            })
            .to(AvroStreamConstant.Topic.USER_SOURCE_1, Produced.with(Serdes.String(), userSourceOneSerde));

        userSinkKStream
            .filter((key, value) -> AvroStreamConstant.MessageType.SOURCE_2.equals(value.getPlusSinkSource()))
            .map((key, sink) -> {
                UserSourceTwo userSource = UserSourceTwo.newBuilder()
                    .setNameSourceTwo(sink.getNameSinkSource())
                    .setAgeSourceTwo(sink.getAgeSink())
                    .build();
                return new KeyValue<>(key, userSource);
            })
            .to(AvroStreamConstant.Topic.USER_SOURCE_2, Produced.with(Serdes.String(), userSourceTwoSerde));
        //to(AvroStreamConstant.Topic.USER_SINK, Produced.with(Serdes.String(), userSinkSerde));

        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();

    }
}

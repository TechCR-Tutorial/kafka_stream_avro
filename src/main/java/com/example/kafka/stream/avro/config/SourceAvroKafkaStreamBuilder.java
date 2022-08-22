package com.example.kafka.stream.avro.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.kafka.stream.avro.model.UserSink;
import com.example.kafka.stream.avro.model.UserSource;
import com.example.kafka.stream.avro.type.AvroStreamConstant;
import com.example.kafka.stream.avro.type.AvroStreamUtil;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

@Component
public class SourceAvroKafkaStreamBuilder {

    @Autowired
    private AvroStreamUtil avroStreamUtil;

    @PostConstruct
    public void buildStream() {

        Properties config = avroStreamUtil.getDefaultProps("avro-user-sink-enrich-application");

        Map<String, Object> configMap = new HashMap<>();
        config.forEach( (key, value) -> configMap.put(key.toString(), value));

        Serde<UserSource> userSourceSerde = new SpecificAvroSerde<>();
        userSourceSerde.configure(configMap, false);

        Serde<UserSink> userSinkSerde = new SpecificAvroSerde<>();
        userSinkSerde.configure(configMap, false);

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, UserSource> userSourceStream = builder
            .stream(AvroStreamConstant.Topic.USER_SOURCE, Consumed.with(Serdes.String(), userSourceSerde));
        userSourceStream
            .map((key, source) -> {
                UserSource userSource = (UserSource) source;
                UserSink sinkEvent = UserSink.newBuilder()
                    .setNameSink(userSource.getNameSource())
                    .setAgeSink(userSource.getAgeSource())
                    .setPlusSink(AvroStreamConstant.MessageType.SOURCE_1)
                    .build();
                return new KeyValue<>(key, sinkEvent);
            })
            .to(AvroStreamConstant.Topic.USER_SINK, Produced.with(Serdes.String(), userSinkSerde));

        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }
}

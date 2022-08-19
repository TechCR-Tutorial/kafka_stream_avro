package com.example.kafka.stream.avro.config;

import java.util.Collections;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.kafka.stream.avro.model.UserSink;
import com.example.kafka.stream.avro.model.UserSource;
import com.example.kafka.stream.avro.type.AvroStreamConstant;
import com.example.kafka.stream.json.config.CustomJsonSerde;
import com.example.kafka.stream.json.config.SinkJsonSerde;
import com.example.kafka.stream.json.model.ExampleSourceOne;
import com.example.kafka.stream.json.model.Sink;
import com.example.kafka.stream.json.type.SimpleStreamConstant;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.ReflectionAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

@Component
public class SourceAvroKafkaStreamBuilder {

    @Value(value = "${spring.kafka.bootstrap-server}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistry;

    @PostConstruct
    public void buildStream() {
        final Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "avro-user-sink-enrich-application");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put("ssl.endpoint.identification.algorithm", "https");
        config.put("sasl.mechanism", "PLAIN");
        config.put("request.timeout.ms", 20000);
        config.put("retry.backoff.ms", 500);
        config.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username='SYNBWX6GH6LM5PL6' password='BmiTW8ZJbdfpqe1NeiJFA0+0xz0JbXtbeniM+eHYigxKArRP6cRDE6z+pKcIgyM9';");
        config.put("security.protocol", "SASL_SSL");
        config.put("schema.registry.url", schemaRegistry);
        config.put("basic.auth.credentials.source", "USER_INFO");
        config.put("schema.registry.basic.auth.user.info", "65F6YLYZBQFSV5CJ:X9rIo/bRfPM+lkGuQLWLTDsdApyj6PLknqDNvdlmR606aAa7nmIvxCJcCUnDZ3HL");

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
                    .setPlusSink("Plus_Sink")
                    .build();
                return new KeyValue<>(key, sinkEvent);
            })
            .to(AvroStreamConstant.Topic.USER_SINK, Produced.with(Serdes.String(), userSinkSerde));

        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }
}

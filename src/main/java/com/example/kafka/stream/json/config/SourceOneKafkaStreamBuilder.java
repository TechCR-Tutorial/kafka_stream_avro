package com.example.kafka.stream.json.config;

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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.kafka.stream.json.model.ExampleSourceOne;
import com.example.kafka.stream.json.model.Sink;
import com.example.kafka.stream.json.type.SimpleStreamConstant;

@Profile("default")
@Component
public class SourceOneKafkaStreamBuilder {

    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Value(value = "${spring.kafka.bootstrap-server}")
    private String bootstrapAddress;

    @PostConstruct
    void buildPipeline() {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "source-one-enrich-application");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        StreamsBuilder builder = new StreamsBuilder();
//        KStream<String, ExampleSourceOne> sourceOneStream = builder
//            .stream(SimpleStreamConstant.Topic.SOURCE_1, Consumed.with(Serdes.String(), new SourceOneJsonSerde()));
        KStream<String, Object> sourceOneStream = builder
            .stream(SimpleStreamConstant.Topic.SOURCE_1, Consumed.with(Serdes.String(), new CustomJsonSerde()));
        sourceOneStream
            .map((key, source) -> {
                ExampleSourceOne sourceOne = (ExampleSourceOne) source;
                Sink sinkEvent = Sink.builder().name(sourceOne.getNameOne()).age(sourceOne.getAgeOne()).build();
                return new KeyValue<>(key, sinkEvent);
            })
            .to(SimpleStreamConstant.Topic.SINK_1, Produced.with(Serdes.String(), new SinkJsonSerde()));

        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }
}

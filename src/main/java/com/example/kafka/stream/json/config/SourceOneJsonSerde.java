package com.example.kafka.stream.json.config;

import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.kafka.stream.json.model.ExampleSourceOne;

public class SourceOneJsonSerde extends Serdes.WrapperSerde<ExampleSourceOne> {

    public SourceOneJsonSerde() {
        super(
            new JsonSerializer<>(),
            new JsonDeserializer<ExampleSourceOne>().trustedPackages("com.example.kafka.stream.json.model")
        );
    }
}

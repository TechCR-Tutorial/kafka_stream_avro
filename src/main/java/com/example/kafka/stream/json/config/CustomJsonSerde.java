package com.example.kafka.stream.json.config;

import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CustomJsonSerde extends Serdes.WrapperSerde {

    public CustomJsonSerde() {
        super(
            new JsonSerializer<>(),
            new JsonDeserializer().trustedPackages("com.example.kafka.stream.json.model")
        );
    }
}

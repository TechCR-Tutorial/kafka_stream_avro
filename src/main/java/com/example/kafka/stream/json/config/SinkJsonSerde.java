package com.example.kafka.stream.json.config;

import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.kafka.stream.json.model.Sink;

public class SinkJsonSerde extends Serdes.WrapperSerde<Sink> {

    public SinkJsonSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>());
    }
}

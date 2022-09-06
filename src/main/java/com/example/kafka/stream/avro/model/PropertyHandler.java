package com.example.kafka.stream.avro.model;

import org.springframework.stereotype.Service;

import com.example.kafka.stream.avro.config.CustomConfigPropertyLoader;

@Service
public class PropertyHandler {

    private CustomConfigPropertyLoader propertyLoader;


    public PropertyHandler(CustomConfigPropertyLoader propertyLoader) {
        this.propertyLoader = propertyLoader;
    }


}

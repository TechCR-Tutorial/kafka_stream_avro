package com.example.kafka.stream.avro.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@PropertySources({
    @PropertySource(value = "classpath:custom-props-main.yaml", factory = YamlPropertySourceFactory.class),
    @PropertySource(value = "classpath:custom-props-child.yaml", factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
})
@ConfigurationProperties(prefix="custom.props")
public class CustomConfigPropertyLoader {

    Map<String, Object> child = new HashMap<>();
    Map<String, Object> main = new HashMap<>();

}

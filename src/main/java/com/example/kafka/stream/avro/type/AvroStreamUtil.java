package com.example.kafka.stream.avro.type;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AvroStreamUtil {

    @Value(value = "${spring.kafka.bootstrap-server}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistry;

    public Properties getDefaultProps(String applicationId) {
        final Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
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

        return config;

    }

}

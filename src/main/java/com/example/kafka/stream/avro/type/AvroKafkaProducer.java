package com.example.kafka.stream.avro.type;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.kafka.stream.avro.model.UserSource;

@Profile("ccloud")
@Service
public class AvroKafkaProducer {

    private KafkaTemplate<String, UserSource> userSourceKafkaTemplate;

    public AvroKafkaProducer(KafkaTemplate<String, UserSource> userSourceKafkaTemplate) {
        this.userSourceKafkaTemplate = userSourceKafkaTemplate;
    }

    public void produceUserSource(UserSource sourceOne) {
        userSourceKafkaTemplate.send(AvroStreamConstant.Topic.USER_SOURCE, sourceOne.getNameSource(), sourceOne);
    }

}

package com.example.kafka.stream.avro.type;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.kafka.stream.avro.model.UserSink;
import com.example.kafka.stream.avro.model.UserSinkSource;
import com.example.kafka.stream.avro.model.UserSource;

@Profile("ccloud")
@Service
public class AvroKafkaProducer {

    private KafkaTemplate<String, UserSource> userSourceKafkaTemplate;
    private KafkaTemplate<String, UserSinkSource> userSinkKafkaTemplate;

    public AvroKafkaProducer(KafkaTemplate<String, UserSource> userSourceKafkaTemplate,
                             KafkaTemplate<String, UserSinkSource> userSinkKafkaTemplate) {
        this.userSourceKafkaTemplate = userSourceKafkaTemplate;
        this.userSinkKafkaTemplate = userSinkKafkaTemplate;
    }

    public void produceUserSource(UserSource sourceOne) {
        userSourceKafkaTemplate.send(AvroStreamConstant.Topic.USER_SOURCE, sourceOne.getNameSource(), sourceOne);
    }

    public void produceUserSink(UserSinkSource userSinkSouce) {
        userSinkKafkaTemplate.send(AvroStreamConstant.Topic.USER_SINK_SOURCE,
            userSinkSouce.getNameSinkSource(), userSinkSouce);
    }

}

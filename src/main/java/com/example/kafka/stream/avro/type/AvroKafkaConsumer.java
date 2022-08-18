package com.example.kafka.stream.avro.type;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.kafka.stream.avro.model.UserSink;
import com.example.kafka.stream.avro.model.UserSource;
import com.example.kafka.stream.json.model.ExampleSourceOne;
import com.example.kafka.stream.json.model.Sink;
import com.example.kafka.stream.json.model.SourceTwo;
import com.example.kafka.stream.json.type.SimpleStreamConstant;

import lombok.extern.slf4j.Slf4j;

@Profile("ccloud")
@Service
@Slf4j
public class AvroKafkaConsumer {

    private static final String SIMPLE_AVRO_CONSUMER_ID = "simple_avro_consumer_id";

    @KafkaListener(topics = AvroStreamConstant.Topic.USER_SINK, groupId = SIMPLE_AVRO_CONSUMER_ID)
    public void consumeUserSink(ConsumerRecord<String, UserSink> sinkRecord) {
        log.info(String.format("Consumed User Sink Record -> %s : %s", sinkRecord.key(), sinkRecord.value()));
    }

    @KafkaListener(topics = AvroStreamConstant.Topic.USER_SOURCE, groupId = SIMPLE_AVRO_CONSUMER_ID)
    public void consumeUserSource(ConsumerRecord<String, UserSource> sourceRecord) {
        log.info(String.format("Consumed User Source Record -> %s : %s", sourceRecord.key(), sourceRecord.value()));
    }

}

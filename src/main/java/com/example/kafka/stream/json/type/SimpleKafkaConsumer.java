package com.example.kafka.stream.json.type;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.kafka.stream.json.model.Sink;
import com.example.kafka.stream.json.model.ExampleSourceOne;
import com.example.kafka.stream.json.model.SourceTwo;

import lombok.extern.slf4j.Slf4j;

@Profile("default")
@Service
@Slf4j
public class SimpleKafkaConsumer {

    private static final String SIMPLE_CONSUMER_ID = "simple_consumer_id";

    @KafkaListener(topics = SimpleStreamConstant.Topic.SINK_1, groupId = SIMPLE_CONSUMER_ID)
    public void consumeSinkOne(ConsumerRecord<String, Sink> sinkRecord) {
        log.info(String.format("Consumed Sink Record -> %s", sinkRecord.value()));
    }

    @KafkaListener(topics = SimpleStreamConstant.Topic.SOURCE_1, groupId = SIMPLE_CONSUMER_ID)
    public void consumeSourceOne(ConsumerRecord<String, ExampleSourceOne> sourceOneRecord) {
        log.info(String.format("Consumed SourceOne Record -> %s", sourceOneRecord.value()));
    }

    @KafkaListener(topics = SimpleStreamConstant.Topic.SOURCE_2, groupId = SIMPLE_CONSUMER_ID)
    public void consumeSourceTwo(ConsumerRecord<String, SourceTwo> sourceTwoRecord) {
        log.info(String.format("Consumed SourceTwo Record -> %s", sourceTwoRecord.value()));
    }
}

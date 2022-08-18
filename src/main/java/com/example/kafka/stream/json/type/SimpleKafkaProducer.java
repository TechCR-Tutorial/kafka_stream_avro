package com.example.kafka.stream.json.type;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.kafka.stream.json.model.ExampleSourceOne;
import com.example.kafka.stream.json.model.SourceTwo;

@Profile("default")
@Service
public class SimpleKafkaProducer {

    private KafkaTemplate<String, ExampleSourceOne> sourceOneTemplate;
    private KafkaTemplate<String, SourceTwo> sourceTwoTemplate;

    public SimpleKafkaProducer(KafkaTemplate<String, ExampleSourceOne> sourceOneTemplate,
                               KafkaTemplate<String, SourceTwo> sourceTwoTemplate) {
        this.sourceOneTemplate = sourceOneTemplate;
        this.sourceTwoTemplate = sourceTwoTemplate;
    }

    public void produceSourceOne(ExampleSourceOne sourceOne) {
        sourceOneTemplate.send(SimpleStreamConstant.Topic.SOURCE_1, sourceOne);
    }

    public void produceSourceTwo(SourceTwo sourceTwo) {
        sourceTwoTemplate.send(SimpleStreamConstant.Topic.SOURCE_2, sourceTwo);
    }
}

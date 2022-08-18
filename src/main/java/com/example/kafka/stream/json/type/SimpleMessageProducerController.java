package com.example.kafka.stream.json.type;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafka.stream.json.model.ExampleSourceOne;
import com.example.kafka.stream.json.model.SourceTwo;

@Profile("default")
@RestController
@RequestMapping(value = "/simple")
public class SimpleMessageProducerController {

    private SimpleKafkaProducer simpleKafkaProducer;

    public SimpleMessageProducerController(SimpleKafkaProducer simpleKafkaProducer) {
        this.simpleKafkaProducer = simpleKafkaProducer;
    }

    //@GetMapping("/simple")
    @GetMapping
    public void simple() {
        ExampleSourceOne sourceOne = ExampleSourceOne.builder().nameOne("Param_1").ageOne(0).build();
        simpleKafkaProducer.produceSourceOne(sourceOne);
    }

    @GetMapping("/source1")
    public void publishSourceOne(@RequestParam String name, @RequestParam int age) {
        ExampleSourceOne sourceOne = ExampleSourceOne.builder().nameOne(name).ageOne(age).build();
        simpleKafkaProducer.produceSourceOne(sourceOne);
    }

    @GetMapping("/source2")
    public void publishSourceTwo(@RequestParam String name, @RequestParam int age) {
        SourceTwo sourceTwo = SourceTwo.builder().nameTwo(name).ageTwo(age).build();
        simpleKafkaProducer.produceSourceTwo(sourceTwo);
    }
}

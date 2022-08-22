package com.example.kafka.stream.avro.type;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafka.stream.avro.model.UserSink;
import com.example.kafka.stream.avro.model.UserSinkSource;
import com.example.kafka.stream.avro.model.UserSource;
import com.example.kafka.stream.json.model.ExampleSourceOne;
import com.example.kafka.stream.json.model.SourceTwo;
import com.example.kafka.stream.json.type.SimpleKafkaProducer;

@Profile("ccloud")
@RestController
@RequestMapping(value = "/avro")
public class AvroMessageProducerController {

    private AvroKafkaProducer avroKafkaProducer;

    public AvroMessageProducerController(AvroKafkaProducer avroKafkaProducer) {
        this.avroKafkaProducer = avroKafkaProducer;
    }

    @GetMapping
    public void simple() {
        UserSource userSource = UserSource.newBuilder().setNameSource("Param_1").setAgeSource(0).build();
        avroKafkaProducer.produceUserSource(userSource);
    }

    @GetMapping("/user")
    public void simple(@RequestParam String name, @RequestParam int age) {
        UserSource userSource = UserSource.newBuilder().setNameSource(name).setAgeSource(age).build();
        avroKafkaProducer.produceUserSource(userSource);
    }

    @GetMapping("/sink")
    public void sink(@RequestParam String name, @RequestParam int age,
                     @RequestParam(defaultValue = AvroStreamConstant.MessageType.SOURCE_1) String type) {
        UserSinkSource userSinkSouce = UserSinkSource.newBuilder()
            .setNameSinkSource(name).setAgeSink(age).setPlusSinkSource(type).build();
        avroKafkaProducer.produceUserSink(userSinkSouce);
    }

}

package com.example.kafka.stream.json.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExampleSourceOne {

    private String nameOne;
    private Integer ageOne;
}

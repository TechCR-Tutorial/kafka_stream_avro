package com.example.kafka.stream.json.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SourceTwo {

    private String nameTwo;
    private Integer ageTwo;
}

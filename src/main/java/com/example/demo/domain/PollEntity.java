package com.example.demo.domain;

import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollEntity {
    private Long id;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;

    //Builder to not have sirculare vote options
    @Builder.Default private List<Long> optionIds = new ArrayList<>();
}

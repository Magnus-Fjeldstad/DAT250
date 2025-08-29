package com.example.demo.domain;
import lombok.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteEntity {
    private Long id;
    private Instant publishedAt;
    private Long userId;
    private Long pollId;
    private Long optionId;
}

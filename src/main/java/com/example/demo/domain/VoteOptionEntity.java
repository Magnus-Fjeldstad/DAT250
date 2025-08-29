package com.example.demo.domain;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteOptionEntity {
    private Long id;
    private Long pollId;
    private String caption;
    private int presentationOrder;
}

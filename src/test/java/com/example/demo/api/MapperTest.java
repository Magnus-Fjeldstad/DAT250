package com.example.demo.api;

import com.example.demo.domain.*;
import com.example.demo.generated.model.*;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MappersTest {

    @Test
    void testToDto_fromUserCreate() {
        UserCreate input = new UserCreate().username("alice").email("alice@example.com");

        User result = Mappers.toDto(input, 1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("alice");
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void testFromDto_toUserEntity() {
        UserCreate input = new UserCreate().username("bob").email("bob@example.com");

        UserEntity entity = Mappers.fromDto(input);

        assertThat(entity.getUsername()).isEqualTo("bob");
        assertThat(entity.getEmail()).isEqualTo("bob@example.com");
    }

    @Test
    void testToDto_fromUserEntity() {
        UserEntity entity = UserEntity.builder().id(42L).username("charlie").email("charlie@example.com").build();

        User dto = Mappers.toDto(entity);

        assertThat(dto.getId()).isEqualTo(42L);
        assertThat(dto.getUsername()).isEqualTo("charlie");
        assertThat(dto.getEmail()).isEqualTo("charlie@example.com");
    }

    @Test
    void testToDto_fromPollEntity() {
        Instant publishedAt = Instant.now();
        Instant validUntil = publishedAt.plusSeconds(3600);

        PollEntity entity = PollEntity.builder()
                .id(5L)
                .question("Favorite color?")
                .publishedAt(publishedAt)
                .validUntil(validUntil)
                .optionIds(List.of(1L, 2L))
                .build();

        Poll dto = Mappers.toDto(entity);

        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getQuestion()).isEqualTo("Favorite color?");
        assertThat(dto.getPublishedAt()).isEqualTo(OffsetDateTime.ofInstant(publishedAt, ZoneOffset.UTC));
        assertThat(dto.getValidUntil()).isEqualTo(OffsetDateTime.ofInstant(validUntil, ZoneOffset.UTC));
        assertThat(dto.getOptionIds()).containsExactly(1L, 2L);
    }

    @Test
    void testFromDto_toPollEntity() {
        OffsetDateTime publishedAt = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime validUntil = publishedAt.plusHours(1);

        PollCreate dto = new PollCreate()
                .question("Best season?")
                .publishedAt(publishedAt)
                .validUntil(validUntil);

        PollEntity entity = Mappers.fromDto(dto);

        assertThat(entity.getQuestion()).isEqualTo("Best season?");
        assertThat(entity.getPublishedAt()).isEqualTo(publishedAt.toInstant());
        assertThat(entity.getValidUntil()).isEqualTo(validUntil.toInstant());
    }

    @Test
    void testFromDto_toVoteOptionEntity() {
        PollCreateOptionsInner dto = new PollCreateOptionsInner()
                .caption("Red")
                .presentationOrder(2);

        VoteOptionEntity entity = Mappers.fromDto(7L, dto);

        assertThat(entity.getPollId()).isEqualTo(7L);
        assertThat(entity.getCaption()).isEqualTo("Red");
        assertThat(entity.getPresentationOrder()).isEqualTo(2);
    }

    @Test
    void testToDto_fromVoteEntity() {
        Instant publishedAt = Instant.now();
        VoteEntity entity = VoteEntity.builder()
                .id(9L)
                .publishedAt(publishedAt)
                .userId(1L)
                .pollId(2L)
                .optionId(3L)
                .build();

        Vote dto = Mappers.toDto(entity);

        assertThat(dto.getId()).isEqualTo(9L);
        assertThat(dto.getPublishedAt()).isEqualTo(OffsetDateTime.ofInstant(publishedAt, ZoneOffset.UTC));
        assertThat(dto.getUserId()).isEqualTo(1L);
        assertThat(dto.getPollId()).isEqualTo(2L);
        assertThat(dto.getOptionId()).isEqualTo(3L);
    }
}

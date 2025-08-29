package com.example.demo.api;

import com.example.demo.domain.PollEntity;
import com.example.demo.domain.UserEntity;
import com.example.demo.domain.VoteEntity;
import com.example.demo.domain.VoteOptionEntity;
import com.example.demo.generated.model.User;
import com.example.demo.generated.model.UserCreate;
import com.example.demo.generated.model.Poll;
import com.example.demo.generated.model.PollCreate;
import com.example.demo.generated.model.PollCreateOptionsInner;
import com.example.demo.generated.model.Vote;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class Mappers {
    private Mappers(){}

    // time helpers
    private static OffsetDateTime toOdt(java.time.Instant i){ return i == null ? null : OffsetDateTime.ofInstant(i, ZoneOffset.UTC); }
    private static java.time.Instant toInstant(OffsetDateTime odt){ return odt == null ? null : odt.toInstant(); }

    public static User toDto(UserCreate src, long id){
        return new User().id(id).username(src.getUsername()).email(src.getEmail());
    }

    public static UserEntity fromDto(UserCreate src){
        return UserEntity.builder()
                .username(src.getUsername()).email(src.getEmail()).build();
    }

    public static User toDto(UserEntity u){
        return new User().id(u.getId()).username(u.getUsername()).email(u.getEmail());
    }

    public static Poll toDto(PollEntity p){
        return new Poll()
                .id(p.getId())
                .question(p.getQuestion())
                .publishedAt(toOdt(p.getPublishedAt()))
                .validUntil(toOdt(p.getValidUntil()))
                .optionIds(p.getOptionIds());
    }

    public static PollEntity fromDto(PollCreate c){
        return PollEntity.builder()
                .question(c.getQuestion())
                .publishedAt(toInstant(c.getPublishedAt()))
                .validUntil(toInstant(c.getValidUntil()))
                .build();
    }

    public static VoteOptionEntity fromDto(Long pollId, PollCreateOptionsInner o){
        return VoteOptionEntity.builder()
                .pollId(pollId) // settes i service etterpå
                .caption(o.getCaption())
                .presentationOrder(o.getPresentationOrder())
                .build();
    }

    public static Vote toDto(VoteEntity v){
        return new Vote()
                .id(v.getId())
                .publishedAt(toOdt(v.getPublishedAt()))
                .userId(v.getUserId())
                .pollId(v.getPollId())
                .optionId(v.getOptionId());
    }
}

package com.example.demo.controller;

import com.example.demo.api.Mappers;
import com.example.demo.generated.api.VotesApi;
import com.example.demo.generated.model.Vote;
import com.example.demo.generated.model.VoteCreate;
import com.example.demo.service.PollManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class VotesApiController implements VotesApi {

    private final PollManager mgr;

    @Override
    public ResponseEntity<Vote> createVote(Long pollId, VoteCreate body) {
        var v = mgr.createOrUpdateVote(pollId, body.getUserId(), body.getOptionId());
        return ResponseEntity.created(URI.create("/votes/" + v.getId()))
                .body(Mappers.toDto(v));
    }

    @Override
    public ResponseEntity<List<Vote>> listVotes() {
        var out = mgr.listVotes().stream().map(Mappers::toDto).toList();
        return ResponseEntity.ok(out);
    }
}

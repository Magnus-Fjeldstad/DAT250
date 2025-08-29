package com.example.demo.controller;

import com.example.demo.api.Mappers;
import com.example.demo.domain.PollEntity;
import com.example.demo.domain.VoteOptionEntity;
import com.example.demo.generated.api.PollsApi;
import com.example.demo.generated.model.Poll;
import com.example.demo.generated.model.PollCreate;
import com.example.demo.service.PollManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PollsApiController implements PollsApi {

    private final PollManager mgr;

    @Override
    public ResponseEntity<List<Poll>> listPolls() {
        var out = mgr.listPolls().stream().map(Mappers::toDto).toList();
        return ResponseEntity.ok(out);
    }

    @Override
    public ResponseEntity<Poll> createPoll(PollCreate body) {
        PollEntity p = Mappers.fromDto(body);
        List<VoteOptionEntity> opts = body.getOptions().stream()
                .map(o -> Mappers.fromDto(null, o))
                .toList();
        PollEntity saved = mgr.createPoll(p, opts);
        return ResponseEntity.created(URI.create("/polls/" + saved.getId()))
                .body(Mappers.toDto(saved));
    }
}

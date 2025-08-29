package com.example.demo.controller;

import com.example.demo.api.Mappers;
import com.example.demo.domain.UserEntity;
import com.example.demo.generated.api.UsersApi;
import com.example.demo.generated.model.User;
import com.example.demo.generated.model.UserCreate;
import com.example.demo.service.PollManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;



@RestController
@RequiredArgsConstructor
public class UsersApiController implements UsersApi {

    private final PollManager mgr;

    @Override
    public ResponseEntity<List<User>> listUsers() {
        var out = mgr.listUsers().stream()
                .map(Mappers::toDto)
                .toList();
        return ResponseEntity.ok(out);
    }

    @Override
    public ResponseEntity<User> createUser(UserCreate body) {
        UserEntity saved = mgr.createUser(Mappers.fromDto(body));
        return ResponseEntity.created(URI.create("/users/" + saved.getId()))
                .body(Mappers.toDto(saved));
    }
}

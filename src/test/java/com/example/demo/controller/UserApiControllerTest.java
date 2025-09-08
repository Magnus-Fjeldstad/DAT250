package com.example.demo.controller;

import com.example.demo.domain.UserEntity;
import com.example.demo.generated.model.UserCreate;
import com.example.demo.service.PollManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersApiControllerTest {

    @LocalServerPort
    int port;

    @MockBean
    PollManager pollManager;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void testListUsers_returnsListOfUsers() {
        var users = List.of(
                UserEntity.builder().id(1L).username("alice").email("alice@example.com").build(),
                UserEntity.builder().id(2L).username("bob").email("bob@example.com").build()
        );

        when(pollManager.listUsers()).thenReturn(users);

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/users")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("$", hasSize(2))
                .body("[0].id", equalTo(1))
                .body("[0].username", equalTo("alice"))
                .body("[0].email", equalTo("alice@example.com"))
                .body("[1].id", equalTo(2))
                .body("[1].username", equalTo("bob"))
                .body("[1].email", equalTo("bob@example.com"));

        verify(pollManager, times(1)).listUsers();
    }

    @Test
    void testCreateUser_createsAndReturnsUser() {
        var create = new UserCreate().username("carol").email("carol@example.com");

        var input = UserEntity.builder()
                .username("carol")
                .email("carol@example.com")
                .build();

        var saved = UserEntity.builder()
                .id(42L)
                .username("carol")
                .email("carol@example.com")
                .build();

        when(pollManager.createUser(input)).thenReturn(saved);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(create)
                .when()
                .post("/users")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .header("Location", equalTo("/users/42"))
                .body("id", equalTo(42))
                .body("username", equalTo("carol"))
                .body("email", equalTo("carol@example.com"));

        verify(pollManager, times(1)).createUser(input);
    }

    @Test
    void testListUsers_returnsEmptyList() {
        when(pollManager.listUsers()).thenReturn(List.of());

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/users")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(0));
    }
}

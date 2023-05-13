package com.br.dhfinancial.keycloak.users.controller;

import com.br.dhfinancial.keycloak.users.dto.request.UserRequest;
import com.br.dhfinancial.keycloak.users.dto.response.UserResponse;
import com.br.dhfinancial.keycloak.users.mapper.UserMapper;
import com.br.dhfinancial.keycloak.users.model.User;
import com.br.dhfinancial.keycloak.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    UserMapper userMapper;
    UserService userService;

    @GetMapping("{id}")
    ResponseEntity<UserResponse> getUser(@PathVariable String id) {
        final User user = userService.getUserById(id);
        final UserResponse bodyResponse = userMapper.fromUserToUserResponse(user);
        return ResponseEntity.ok(bodyResponse);
    }


    @PostMapping
    ResponseEntity<Void> createUser(@RequestBody UserRequest userRequest) {
        final User userToBeCreated = userMapper.fromUserRequestToUser(userRequest);
        final User userCreated = userService.create(userToBeCreated);
        final String userId = userCreated.id();

        return ResponseEntity
                .created(fromCurrentRequest().path("/{id}").buildAndExpand(userId).toUri())
                .build();
    }

    @PatchMapping("{id}")
    ResponseEntity<Void> updateUser(@PathVariable String id, @RequestBody UserRequest userRequest) {
        final User userToBeUpdated = userMapper.fromUserRequestToUser(userRequest);
        userToBeUpdated.withUUID(id);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("{id}")
    ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getUsers().stream()
                .map(userMapper::fromUserToUserResponse)
                .collect(Collectors.toList()));
    }
}

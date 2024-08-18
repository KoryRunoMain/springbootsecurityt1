package ru.koryruno.springbootsecurityt1.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.koryruno.springbootsecurityt1.model.RoleType;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.model.dto.CreateUserRequest;
import ru.koryruno.springbootsecurityt1.service.UserService;

import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/public/user")
@RequiredArgsConstructor
public class PublicUserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequest request) {
        User createdUser = userService.createUser(User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .username(request.getUsername())
                .roles(request.getRoles().stream().map(role -> RoleType.valueOf(role.toUpperCase())).collect(Collectors.toSet()))
                .build());
        return ResponseEntity.ok("User created");
    }

}

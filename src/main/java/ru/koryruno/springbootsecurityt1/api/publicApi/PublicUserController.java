package ru.koryruno.springbootsecurityt1.api.publicApi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.koryruno.springbootsecurityt1.annotation.Valid;
import ru.koryruno.springbootsecurityt1.model.requestDto.CreateUserRequest;
import ru.koryruno.springbootsecurityt1.model.responseDto.PublicUserResponse;
import ru.koryruno.springbootsecurityt1.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/public/user")
public class PublicUserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublicUserResponse createUser(@Valid @RequestBody CreateUserRequest createUserDto) {
        return userService.createUser(createUserDto);
    }

}

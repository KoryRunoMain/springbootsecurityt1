package ru.koryruno.springbootsecurityt1.api.publicApi;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.koryruno.springbootsecurityt1.model.responseDto.PublicUserResponse;
import ru.koryruno.springbootsecurityt1.service.UserService;

@RestController
@RequestMapping(path = "/user")
@PreAuthorize("hasAuthority('ROLE_USER')")
@RequiredArgsConstructor
@Tag(name = "Public")
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PublicUserResponse getUserByUsername(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

}

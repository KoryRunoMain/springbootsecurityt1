package ru.koryruno.springbootsecurityt1.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.service.UserService;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByUsername(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

}

package ru.koryruno.springbootsecurityt1.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.service.UserService;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping(path = "/{userId}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

}

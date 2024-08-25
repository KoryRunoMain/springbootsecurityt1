package ru.koryruno.springbootsecurityt1.api.privateApi;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.koryruno.springbootsecurityt1.model.responseDto.PrivateUserResponse;
import ru.koryruno.springbootsecurityt1.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Admin")
public class AdminController {
    private final UserService userService;

    @GetMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PrivateUserResponse getUser(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping(path = "/all")
    @ResponseStatus(HttpStatus.OK)
    public List<PrivateUserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

}

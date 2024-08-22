package ru.koryruno.springbootsecurityt1.api.publicApi;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/info")
@RequiredArgsConstructor
public class PublicController {

    @Value("${web.address}")
    public String webAddress;

    @GetMapping
    public String startPage() {
        return "This is a simple web service for user registration with security JWT authentication." +
                "To register and get user, please go to this address: " + webAddress;
    }

}

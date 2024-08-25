package ru.koryruno.springbootsecurityt1.api.publicApi;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/info")
@RequiredArgsConstructor
@Tag(name = "Public Start Page")
public class PublicController {

    @GetMapping
    public String startPage() {
        return "This is a simple web service for user registration with security JWT authentication.";
    }

}

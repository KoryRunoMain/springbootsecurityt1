package ru.koryruno.springbootsecurityt1.model.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PrivateUserResponse {
    private Long id;
    private String username;
    private List<String> roles;
}

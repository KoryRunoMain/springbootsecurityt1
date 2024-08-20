package ru.koryruno.springbootsecurityt1.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordTokenRequest {
    private String username;
    private String password;
}

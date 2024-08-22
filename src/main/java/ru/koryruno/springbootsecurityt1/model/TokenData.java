package ru.koryruno.springbootsecurityt1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenData {
    private String token;
    private String refreshToken;
}

package ru.koryruno.springbootsecurityt1.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {
    private String username;
    private String email;
    private String password;
//    private List<String> roles;
}

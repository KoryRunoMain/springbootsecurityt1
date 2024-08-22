package ru.koryruno.springbootsecurityt1.model.mapper;

import org.springframework.stereotype.Component;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.model.dto.CreateUserDto;

@Component
public class UserMapper {

    public User toUser(CreateUserDto userDto) {
        String roles = String.join(", ", userDto.getRoles());
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(roles)
                .build();
    }

}

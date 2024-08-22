package ru.koryruno.springbootsecurityt1.model.mapper;

import org.springframework.stereotype.Component;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.model.requestDto.CreateUserRequest;
import ru.koryruno.springbootsecurityt1.model.responseDto.PrivateUserResponse;
import ru.koryruno.springbootsecurityt1.model.responseDto.PublicUserResponse;

import java.util.Arrays;

@Component
public class UserMapper {

    public User toUser(CreateUserRequest userDto) {
//        String roles = String.join(", ", userDto.getRoles());
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(String.join(", ", userDto.getRoles()))
                .build();
    }

    public PublicUserResponse toPublicUserResponse(User user) {
        return PublicUserResponse.builder()
                .username(user.getUsername())
                .build();
    }

    public PrivateUserResponse toPrivateUserResponse(User user) {
        return PrivateUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(Arrays.asList(user.getRoles().split(",")))
                .build();
    }

}

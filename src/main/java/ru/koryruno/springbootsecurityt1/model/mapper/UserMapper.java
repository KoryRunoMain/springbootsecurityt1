package ru.koryruno.springbootsecurityt1.model.mapper;

import org.springframework.stereotype.Component;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.model.UserRole;
import ru.koryruno.springbootsecurityt1.model.requestDto.CreateUserRequest;
import ru.koryruno.springbootsecurityt1.model.responseDto.PrivateUserResponse;
import ru.koryruno.springbootsecurityt1.model.responseDto.PublicUserResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toUser(CreateUserRequest userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
    }

    public PublicUserResponse toPublicUser(User user) {
        return PublicUserResponse.builder()
                .username(user.getUsername())
                .build();
    }

    public PrivateUserResponse toPrivateUser(User user) {
        return PrivateUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles().stream().map(UserRole::getRoleName).collect(Collectors.toList()))
                .build();
    }

    public List<PrivateUserResponse> toPrivateUserList(List<User> user) {
        return user.stream().map(this::toPrivateUser).toList();
    }

}

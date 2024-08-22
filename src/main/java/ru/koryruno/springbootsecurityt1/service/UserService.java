package ru.koryruno.springbootsecurityt1.service;

import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.model.requestDto.CreateUserRequest;
import ru.koryruno.springbootsecurityt1.model.responseDto.PrivateUserResponse;
import ru.koryruno.springbootsecurityt1.model.responseDto.PublicUserResponse;

import java.util.List;

public interface UserService {

    PublicUserResponse createUser(CreateUserRequest createUserDto);

    PrivateUserResponse getUserById(Long userId);

    PublicUserResponse getUserByUsername(String username);

    List<PrivateUserResponse> getAllUsers();

}

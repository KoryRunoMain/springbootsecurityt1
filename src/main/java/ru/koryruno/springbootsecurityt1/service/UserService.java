package ru.koryruno.springbootsecurityt1.service;

import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.model.requestDto.CreateUserRequest;
public interface UserService {

    User createUser(CreateUserRequest createUserDto);

    User getUserById(Long userId);

    User getUserByUsername(String username);

}

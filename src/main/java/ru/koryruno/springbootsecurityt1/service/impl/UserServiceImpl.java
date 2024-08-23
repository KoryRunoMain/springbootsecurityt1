package ru.koryruno.springbootsecurityt1.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.koryruno.springbootsecurityt1.exception.NotFoundException;
import ru.koryruno.springbootsecurityt1.model.RoleType;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.model.UserRole;
import ru.koryruno.springbootsecurityt1.model.requestDto.CreateUserRequest;
import ru.koryruno.springbootsecurityt1.model.mapper.UserMapper;
import ru.koryruno.springbootsecurityt1.model.responseDto.PrivateUserResponse;
import ru.koryruno.springbootsecurityt1.model.responseDto.PublicUserResponse;
import ru.koryruno.springbootsecurityt1.repository.UserRepository;
import ru.koryruno.springbootsecurityt1.repository.UserRoleRepository;
import ru.koryruno.springbootsecurityt1.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public PublicUserResponse createUser(CreateUserRequest createUserDto) {
        User user = userMapper.toUser(createUserDto);
        List<UserRole> userRoles = createUserDto.getRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(roleName)
                        .orElseGet(() -> roleRepository.save(new UserRole(null, roleName))))
                .toList();

        user.setRoles(userRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toPublicUser(userRepository.save(user));
    }

    @Override
    public PrivateUserResponse getUserById(Long userId) {
        return userMapper.toPrivateUser(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id: '%s' not found", userId))));
    }

    @Override
    public PublicUserResponse getUserByUsername(String username) {
        return userMapper.toPublicUser(userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("User with username: '%s' not found", username))));
    }

    @Override
    public List<PrivateUserResponse> getAllUsers() {
        return userMapper.toPrivateUserList(userRepository.findAll());
    }

}

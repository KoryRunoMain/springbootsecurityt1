package ru.koryruno.springbootsecurityt1.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.koryruno.springbootsecurityt1.exception.NotFoundException;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.model.UserRole;
import ru.koryruno.springbootsecurityt1.model.mapper.UserMapper;
import ru.koryruno.springbootsecurityt1.model.requestDto.CreateUserRequest;
import ru.koryruno.springbootsecurityt1.model.responseDto.PrivateUserResponse;
import ru.koryruno.springbootsecurityt1.model.responseDto.PublicUserResponse;
import ru.koryruno.springbootsecurityt1.repository.UserRepository;
import ru.koryruno.springbootsecurityt1.repository.UserRoleRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_Success() {
        CreateUserRequest request = new CreateUserRequest(
                "username",
                "username@username.user", "password", List.of("ROLE_USER"));
        User user = new User();
        user.setUsername("username");
        user.setPassword("encodedPassword");

        when(userMapper.toUser(request)).thenReturn(user);
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(Optional.of(new UserRole(1L, "ROLE_USER")));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toPublicUser(user)).thenReturn(new PublicUserResponse("username"));

        PublicUserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("username", response.getUsername());
    }

    @Test
    void getUserById_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toPrivateUser(user)).thenReturn(new PrivateUserResponse(1L, "username", List.of("ROLE_USER")));

        PrivateUserResponse response = userService.getUserById(userId);

        assertNotNull(response);
        assertEquals(userId, response.getId());
    }

    @Test
    void getUserById_Failure_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void getUserByUsername_Success() {
        String username = "username";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userMapper.toPublicUser(user)).thenReturn(new PublicUserResponse(username));

        PublicUserResponse response = userService.getUserByUsername(username);

        assertNotNull(response);
        assertEquals(username, response.getUsername());
    }

    @Test
    void getUserByUsername_Failure_UserNotFound() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserByUsername(username));
    }

    @Test
    void getAllUsers_Success() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toPrivateUserList(users)).thenReturn(List.of(new PrivateUserResponse(1L, "username", List.of("ROLE_USER")), new PrivateUserResponse(2L, "username", List.of("ROLE_USER"))));

        List<PrivateUserResponse> responses = userService.getAllUsers();

        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

}
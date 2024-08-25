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

    // Init
    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "username";
    private static final String PASSWORD = "encodedPassword";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final UserRole USER_ROLE_ENTITY = new UserRole(1L, "ROLE_USER");
    private static final List<String> USER_ROLES = List.of("ROLE_USER");
    private static final String NOT_EXISTING_USERNAME = "nonExistingUser";

    private final CreateUserRequest validRequest = new CreateUserRequest("username",
            "username@username.user", PASSWORD, USER_ROLES);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void When_CreateUser_Expect_Successfully() {
        User user = new User();
        user.setUsername(USER_NAME);
        user.setPassword(ENCODED_PASSWORD);

        when(userMapper.toUser(validRequest)).thenReturn(user);
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(Optional.of(USER_ROLE_ENTITY));
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toPublicUser(user)).thenReturn(new PublicUserResponse(USER_NAME));

        PublicUserResponse response = userService.createUser(validRequest);

        assertNotNull(response);
        assertEquals(USER_NAME, response.getUsername());
    }

    @Test
    public void When_GetUserById_Expect_Successfully() {
        User user = new User();
        user.setId(USER_ID);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userMapper.toPrivateUser(user)).thenReturn(
                new PrivateUserResponse(USER_ID, USER_NAME, USER_ROLES));

        PrivateUserResponse response = userService.getUserById(USER_ID);

        assertNotNull(response);
        assertEquals(USER_ID, response.getId());
    }

    @Test
    public void When_GetUserById_With_NotExistingId_Expect_NotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(USER_ID));
    }

    @Test
    public void When_GetUserByUsername_Expect_Successfully() {
        User user = new User();
        user.setUsername(USER_NAME);

        when(userRepository.findByUsername(USER_NAME)).thenReturn(Optional.of(user));
        when(userMapper.toPublicUser(user)).thenReturn(new PublicUserResponse(USER_NAME));

        PublicUserResponse response = userService.getUserByUsername(USER_NAME);

        assertNotNull(response);
        assertEquals(USER_NAME, response.getUsername());
    }

    @Test
    public void When_GetUserByUsername_With_NotExistingUsername_Expect_NotFound() {
        when(userRepository.findByUsername(NOT_EXISTING_USERNAME)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserByUsername(NOT_EXISTING_USERNAME));
    }

    @Test
    public void When_GetAllUsers_Expect_Successfully() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toPrivateUserList(users)).thenReturn(List.of(
                new PrivateUserResponse(USER_ID, USER_NAME, USER_ROLES),
                new PrivateUserResponse((USER_ID + 1), USER_NAME, USER_ROLES))
        );

        List<PrivateUserResponse> responses = userService.getAllUsers();

        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

}

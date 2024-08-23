package ru.koryruno.springbootsecurityt1.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.koryruno.springbootsecurityt1.exception.NotFoundException;
import ru.koryruno.springbootsecurityt1.model.AppUserDetails;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_Success() {
        String username = "username";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        AppUserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_Failure_UserNotFound() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }

}
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    // Init
    private static final String USERNAME = "username";
    private static final String NOT_EXISTING_USERNAME = "notExistingUsername";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void When_LoadUserByUsername_With_ExistingUsername_Expect_Successfully() {
        User user = new User();
        user.setUsername(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        AppUserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);

        assertNotNull(userDetails);
        assertEquals(USERNAME, userDetails.getUsername());
    }

    @Test
    public void When_LoadUserByUsername_With_NotExistingUsername_Expect_NotFound() {
        when(userRepository.findByUsername(NOT_EXISTING_USERNAME)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userDetailsService.loadUserByUsername(NOT_EXISTING_USERNAME));
    }

}

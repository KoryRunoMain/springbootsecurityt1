package ru.koryruno.springbootsecurityt1.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.koryruno.springbootsecurityt1.security.JwtFilter;
import ru.koryruno.springbootsecurityt1.service.impl.UserDetailsServiceImpl;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @MockBean
    private JwtFilter jwtFilter;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void When_SecurityFilterChain_BeanCreation_Expect_Successfully() throws Exception {
        HttpSecurity http = Mockito.mock(HttpSecurity.class);
        assertNotNull(securityConfig.securityFilterChain(http));
    }

    @Test
    public void When_PasswordEncoder_CreatesEncoder_Expect_Successfully() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);
    }

    @Test
    public void When_AuthenticationManager_BeanCreation_Expect_Successfully() throws Exception {
        AuthenticationConfiguration config = Mockito.mock(AuthenticationConfiguration.class);
        Mockito.when(config.getAuthenticationManager()).thenReturn(Mockito.mock(AuthenticationManager.class));
        assertNotNull(securityConfig.authenticationManager(config));
    }

}

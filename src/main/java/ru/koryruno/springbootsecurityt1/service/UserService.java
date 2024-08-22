package ru.koryruno.springbootsecurityt1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.koryruno.springbootsecurityt1.exception.AuthenticationException;
import ru.koryruno.springbootsecurityt1.exception.NotFoundException;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.model.dto.CreateUserDto;
import ru.koryruno.springbootsecurityt1.model.dto.JwtAuthenticationDto;
import ru.koryruno.springbootsecurityt1.model.dto.RefreshTokenDto;
import ru.koryruno.springbootsecurityt1.model.dto.UserCredentialsDto;
import ru.koryruno.springbootsecurityt1.model.mapper.UserMapper;
import ru.koryruno.springbootsecurityt1.repository.UserRepository;
import ru.koryruno.springbootsecurityt1.security.JwtTokenService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final UserMapper userMapper;

    public JwtAuthenticationDto signIn(UserCredentialsDto userCredentialsDto) {
        User user = findByCredentials(userCredentialsDto);
        List<String> roles = Arrays.asList(user.getRoles().split(", ")); // TODO Добавлена строка
        return jwtTokenService.generateAuthToken(user.getUsername(), roles);
    }

    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (refreshToken != null && jwtTokenService.validateToken(refreshToken)) {
            User user = getUserByUsername(jwtTokenService.getUsernameFromToken(refreshToken));
            return jwtTokenService.refreshBaseToken(user.getUsername(), refreshToken);
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    public User createUser(CreateUserDto createUserDto) {
        User user = userMapper.toUser(createUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("User with username: '%s' not found", username)));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id: '%s' not found", userId)));
    }

    private User findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        Optional<User> optionalUser = userRepository.findByUsername(userCredentialsDto.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())) {
                return user;
            }
        }
        throw new AuthenticationException("Username or password not correct");
    }

}

package ru.koryruno.springbootsecurityt1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.koryruno.springbootsecurityt1.exception.NotFoundException;
import ru.koryruno.springbootsecurityt1.model.AppUserDetails;
import ru.koryruno.springbootsecurityt1.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsService {
    private final UserRepository userRepository;

    public UserDetails findByUsername(String username){
        return userRepository.findUserByUsername(username).map(AppUserDetails::new)
                .orElseThrow(() -> new NotFoundException("UserDetails not found for username: " + username));
    }

}

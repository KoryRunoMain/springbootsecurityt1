package ru.koryruno.springbootsecurityt1.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.koryruno.springbootsecurityt1.exception.NotFoundException;
import ru.koryruno.springbootsecurityt1.model.AppUserDetails;
import ru.koryruno.springbootsecurityt1.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public AppUserDetails loadUserByUsername(String username) throws NotFoundException {
        return  userRepository.findByUsername(username).map(AppUserDetails::new).orElseThrow(
                () -> new NotFoundException(String.format("User with username: '%s' not found", username)));
    }

}

package ru.koryruno.springbootsecurityt1.service;

import ru.koryruno.springbootsecurityt1.model.requestDto.RefreshTokenRequest;
import ru.koryruno.springbootsecurityt1.model.responseDto.TokenResponse;
import ru.koryruno.springbootsecurityt1.model.requestDto.UserCredentialsRequest;

public interface TokenService {

    TokenResponse signIn(UserCredentialsRequest userCredentialsDto);

    TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}

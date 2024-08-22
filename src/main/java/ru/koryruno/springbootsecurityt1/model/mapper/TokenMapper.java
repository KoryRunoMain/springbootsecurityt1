package ru.koryruno.springbootsecurityt1.model.mapper;

import org.springframework.stereotype.Component;
import ru.koryruno.springbootsecurityt1.model.TokenData;
import ru.koryruno.springbootsecurityt1.model.responseDto.TokenResponse;

@Component
public class TokenMapper {

    public TokenResponse toTokenResponse(TokenData tokenData) {
        return TokenResponse.builder()
                .token(tokenData.getToken())
                .refreshToken(tokenData.getRefreshToken())
                .build();
    }

}

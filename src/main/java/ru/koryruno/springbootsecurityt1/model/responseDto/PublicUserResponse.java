package ru.koryruno.springbootsecurityt1.model.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PublicUserResponse {
    private String username;
}

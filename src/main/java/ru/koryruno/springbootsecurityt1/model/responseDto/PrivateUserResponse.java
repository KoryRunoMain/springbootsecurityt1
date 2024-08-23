package ru.koryruno.springbootsecurityt1.model.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivateUserResponse {
    private Long id;
    private String username;
    private List<String> roles;
}

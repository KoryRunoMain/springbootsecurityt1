package ru.koryruno.springbootsecurityt1.api.publicApi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.koryruno.springbootsecurityt1.model.requestDto.CreateUserRequest;
import ru.koryruno.springbootsecurityt1.model.responseDto.PublicUserResponse;
import ru.koryruno.springbootsecurityt1.service.UserService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class PublicUserControllerTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private PublicUserController publicUserController;

    private MockMvc mockMvc;

    // Init
    private final CreateUserRequest request = new CreateUserRequest(
            "username", "username@username.user", "password", List.of("ROLE_USER"));
    private final PublicUserResponse userResponse = new PublicUserResponse("username");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(publicUserController).build();
    }

    @Test
    public void When_CreateUser_Expect_Successfully() throws Exception {
        when(userService.createUser(request)).thenReturn(userResponse);

        mockMvc.perform(post("/api/v1/public/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"username\", \"email\": \"username@username.user\", \"password\": \"password\", \"roles\": [\"ROLE_USER\"]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("username"))
                .andDo(print());
    }
}

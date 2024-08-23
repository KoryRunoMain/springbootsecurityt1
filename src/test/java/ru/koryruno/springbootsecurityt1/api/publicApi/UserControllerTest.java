package ru.koryruno.springbootsecurityt1.api.publicApi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.koryruno.springbootsecurityt1.model.responseDto.PublicUserResponse;
import ru.koryruno.springbootsecurityt1.service.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @WithMockUser(roles = "USER")
    void getUserByUsername_Success() throws Exception {
        PublicUserResponse userResponse = new PublicUserResponse("username");

        when(userService.getUserByUsername("username")).thenReturn(userResponse);

        mockMvc.perform(get("/user").param("username", "username"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"))
                .andDo(print());
    }

}
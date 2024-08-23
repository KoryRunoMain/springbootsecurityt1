package ru.koryruno.springbootsecurityt1.api.privateApi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.koryruno.springbootsecurityt1.model.responseDto.PrivateUserResponse;
import ru.koryruno.springbootsecurityt1.service.UserService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class AdminControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUser_Success() throws Exception {
        PrivateUserResponse userResponse = new PrivateUserResponse(1L, "username",  List.of("ROLE_USER"));

        when(userService.getUserById(1L)).thenReturn(userResponse);

        mockMvc.perform(get("/admin/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_Success() throws Exception {
        PrivateUserResponse userResponse = new PrivateUserResponse(1L, "username", List.of("ROLE_USER"));

        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userResponse));

        mockMvc.perform(get("/admin/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("username"))
                .andExpect(jsonPath("$[0].role").value("ROLE_USER"))
                .andDo(print());
    }

}
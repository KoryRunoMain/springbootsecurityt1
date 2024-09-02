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

    // Init
    private static final Long USER_ID = 1L;
    private final PrivateUserResponse userResponse = new PrivateUserResponse(1L, "username",
            List.of("ROLE_USER"));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void When_GetUser_Expect_Successfully() throws Exception {
        when(userService.getUserById(USER_ID)).thenReturn(userResponse);

        mockMvc.perform(get("/admin/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void When_GetAllUsers_Expect_Successfully() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userResponse));

        mockMvc.perform(get("/admin/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(USER_ID))
                .andExpect(jsonPath("$[0].username").value("username"))
                .andExpect(jsonPath("$[0].roles[0]").value("ROLE_USER"))
                .andDo(print());
    }

}

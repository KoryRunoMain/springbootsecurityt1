package ru.koryruno.springbootsecurityt1.aspect;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import ru.koryruno.springbootsecurityt1.exception.ApplicationException;
import ru.koryruno.springbootsecurityt1.model.requestDto.CreateUserRequest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Import(ValidationAspect.class)
class ValidationAspectTest {

    @Mock
    private CreateUserRequest user;
    @InjectMocks
    private ValidationAspect validationAspect;

    @Test
    public void When_ValidateUser_With_ValidFields_Expect_Successfully() {
        Mockito.when(user.getUsername()).thenReturn("validUser");
        Mockito.when(user.getEmail()).thenReturn("valid.email@example.com");

        assertDoesNotThrow(() -> validationAspect.validateUser(user));
    }

    @Test
    public void When_ValidateUser_With_InvalidUsername_Expect_ThrowsException() {
        Mockito.when(user.getUsername()).thenReturn("invalid user");

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> validationAspect.validateUser(user));
        assertEquals("Username must contain only Latin letters and digits", exception.getMessage());
    }

    @Test
    public void When_ValidateUser_With_InvalidEmail_Expect_ThrowsException() {
        Mockito.when(user.getUsername()).thenReturn("validUser");
        Mockito.when(user.getEmail()).thenReturn("invalid-email");

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> validationAspect.validateUser(user));
        assertEquals("Invalid email format", exception.getMessage());
    }

}

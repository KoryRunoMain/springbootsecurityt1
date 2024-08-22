package ru.koryruno.springbootsecurityt1.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.koryruno.springbootsecurityt1.exception.ApplicationException;
import ru.koryruno.springbootsecurityt1.model.requestDto.CreateUserRequest;

import java.util.regex.Pattern;

@Component
@Aspect
@Slf4j
@Order(1)
public class ValidationAspect {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");

    @Pointcut("execution(public * ru.koryruno.springbootsecurityt1.api.publicApi.*.create*(..))")
    public void createPublicMethodPointcut() {}

    @Pointcut("args(user,..)")
    public void userArgsPointcut(CreateUserRequest user) {}

    @Before(value = "createPublicMethodPointcut() && userArgsPointcut(user)", argNames = "user")
    public void validateUser(CreateUserRequest user) {
        log.info("Validate user before calling method");
        validateUserFields(user);
    }

    private void validateUserFields(CreateUserRequest user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new ApplicationException("Username cannot be empty");
        }
        if (!USERNAME_PATTERN.matcher(user.getUsername()).matches()) {
            throw new ApplicationException("Username must contain only Latin letters and digits");
        }
        if (user.getEmail() == null || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new ApplicationException("Invalid email format");
        }
    }

}

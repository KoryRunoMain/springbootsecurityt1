package ru.koryruno.springbootsecurityt1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.koryruno.springbootsecurityt1.service.TokenService;

@SpringBootApplication
public class Springbootsecurityt1Application {

	public static void main(String[] args) {
		SpringApplication.run(Springbootsecurityt1Application.class, args);
	}

}

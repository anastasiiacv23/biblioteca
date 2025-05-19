package com.example.biblio;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Biblioteca API",
				version = "1.0",
				description = "Documentacion de la API Biblioteca"
		),
		security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
		name = "bearerAuth",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT"
)
@SpringBootApplication
public class BiblioApplication {


		public static void main (String[]args){
		SpringApplication.run(BiblioApplication.class, args);
	}


}

package com.example.biblio.config;
import com.example.biblio.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
        ;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.
        UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity

public class SecurityConfig {
    //inyecto filto JWT que luego se agrega al flujo de seguridad de spring
    private final JwtAuthFilter jwtAuthFilter;
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers(
/* cada vez que tengo un post nuevo tengo que añadir la ruta d epost*/
                "/api/user/register", "/api/alquiler/*", "/api/clubLectura/**","/api/clubLectura/**","/api/clubLectura/apuntarse",  "/api/sala/*","/api/user/login", "/api/clubLectura/modificarClubNota" ))




                 .authorizeHttpRequests(auth -> auth

                                 .requestMatchers(HttpMethod.OPTIONS, "/api/**", "/api/libros/searchTitulo").permitAll()
                                 .requestMatchers(HttpMethod.GET, "/api/libros").permitAll()
                                 .requestMatchers(HttpMethod.GET, "/api/libros/search").permitAll()
                                 .requestMatchers(HttpMethod.GET, "/api/libros/searchTitulo").permitAll() // Nueva línea
                                 .requestMatchers(HttpMethod.GET, "/api/clubLectura/anunciados").permitAll()
                                 .requestMatchers(HttpMethod.POST, "/api/alquiler/alquiler").authenticated()
                                 .requestMatchers(HttpMethod.POST, "/api/clubLectura/registarClub").authenticated()
                                 .requestMatchers(HttpMethod.GET, "/api/sala/salasExistentes").authenticated()
                                 .requestMatchers(HttpMethod.POST, "/api/clubLectura/apuntarse").authenticated()
                                 .requestMatchers(HttpMethod.PUT, "/api/clubLectura/modificarClubNota").authenticated()


// Nueva lín
                                 .requestMatchers(HttpMethod.GET, "/api/clubLectura").permitAll()
                                 .requestMatchers("/api/user/register", "/api/user/login").permitAll()
                 .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() //
                 .requestMatchers("/api/**").authenticated()
                                 .requestMatchers("/api/alquiler/alquiler").authenticated()
                                 .anyRequest().permitAll()
                 )
                //analiza el token en cada peticion;
                 .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                 return http.build();
    }
}
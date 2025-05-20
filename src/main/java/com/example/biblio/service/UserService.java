package com.example.biblio.service;
import com.example.biblio.model.User;
import com.example.biblio.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User register(String username, String password) {
        if (userRepository.existsByUsername(username)) {

            throw new RuntimeException("El usuario ya existe");
        }
        User user = new User(username, encoder.encode(password));
        return userRepository.save(user);
    }
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username).get();
        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        User user = userRepository.findByUsername(username).get();
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .build();

    }
}
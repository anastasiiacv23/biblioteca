package com.example.biblio.service;

import com.example.biblio.model.User;
import com.example.biblio.repository.LibroRepository;
import com.example.biblio.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Nombre de usuario ya está ocupado");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }


    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}

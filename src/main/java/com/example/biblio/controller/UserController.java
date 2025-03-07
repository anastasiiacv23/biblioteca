package com.example.biblio.controller;

import com.example.biblio.model.User;
import com.example.biblio.repository.UserRepository;
import com.example.biblio.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")

public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username,@RequestParam String password) {
        try{
            User user =userService.register(username,password);
            //i el registro es exitoso, se devuelve una respuesta HTTP con el código de estado 200 OK y el objeto User que fue creado.
            return ResponseEntity.ok(user);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String username,@RequestParam String password) {
            User user =userService.login(username,password);
            if (user!=null){
                return ResponseEntity.ok(user);
            }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }


}

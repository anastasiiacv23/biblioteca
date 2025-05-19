package com.example.biblio.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForwardController {
    @GetMapping(value = "/{path:^(?!.*\\.).*}")
    public String redirect() {
        // Redirige a index.html para que Angular maneje las rutas
             return "forward:/index.html";
             }
    @GetMapping(value = "/*/{path:^(?!.*\\.).*}")
    public String redirectNested() {
      return "forward:/index.html";
    }

}

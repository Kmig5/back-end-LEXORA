package com.example.lexora.user;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Miguel
 */
@RestController()
@RequestMapping("/lexora/user")
public class UserController {
    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }
    

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        
        return service.login(loginData.get("email"), loginData.get("password"));
    }

}

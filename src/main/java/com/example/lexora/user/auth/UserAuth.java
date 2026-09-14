package com.example.lexora.user.auth;

import com.example.lexora.config.JwtService;
import com.example.lexora.user.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Miguel
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/lexora/auth")
public class UserAuth {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody Map<String, String> loginData) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginData.get("email"),
                        loginData.get("password")
                )
        );

        UserDetails user = userDetailsService.loadUserByUsername(
                loginData.get("email")
        );

        String token = jwtService.generateToken(user);

        ResponseEntity<?> loginResponse = userService.login(
                loginData.get("email"),
                loginData.get("password")
        );

        return ResponseEntity.ok(
                new AuthResponse(token, loginResponse.getBody())
        );
    }
}

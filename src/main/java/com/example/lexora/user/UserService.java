package com.example.lexora.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Miguel
 */
@Service
public class UserService {

    private UserRepository repo;
    
    BCryptPasswordEncoder encoder;
    
    public UserService(UserRepository repo, BCryptPasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }
    
    public ResponseEntity<?> login(String email, String passw) {
        
        User userBD = repo.findByEmail(email);
        
        if (userBD == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("email pas trouvé");
        }
        
        if (encoder.matches(passw, userBD.getPassword())) {
            UserDTO user = new UserDTO();
            user.setId(userBD.getId());
            user.setEmail(userBD.getEmail());
            user.setNom(userBD.getNom());
            user.setPrenom(userBD.getPrenom());
            user.setVille(userBD.getVille());
            user.setRegion(userBD.getRegion());
            user.setColor(userBD.getColor());
            return ResponseEntity.ok(user);
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERREUR Veuillez reessayer");
    }
    
}

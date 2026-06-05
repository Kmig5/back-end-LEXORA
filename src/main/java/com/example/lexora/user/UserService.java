package com.example.lexora.user;

import com.example.lexora.avocat.Avocat;
import com.example.lexora.client.Client;
import java.time.Instant;
import java.util.List;
import java.util.Random;
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
    
    public ResponseEntity<?> login(String email, String passw){
        
        User userBD = repo.findByEmail(email);
        
        if(userBD == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Remplir les champs");
        
        if(encoder.matches(passw, userBD.getPassword())){
            return ResponseEntity.ok(userBD);
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERREUR Veuillez reessayer");
    }
    
    public String registerClient(UserDTO userNew){
        List<String> couleurs = List.of("blue", "red", "gray", "yellow", "orange", "violet");
        Random random = new Random();
        int choice = random.nextInt(couleurs.size());
        
        String passwHash = encoder.encode(userNew.getPassword());
        
        User user = new Client();
        
        user.setCreatedAt(Instant.EPOCH);
        user.setEmail(userNew.getEmail());
        user.setNom(userNew.getNom());
        user.setPrenom(userNew.getPrenom());
        user.setPassword(passwHash);
        user.setColor(couleurs.get(choice));
        
        repo.save(user);
        
        return "Bienvenue "+userNew.getPrenom();
    }
    
    public String registerAvocat(UserDTO userNew){
        List<String> couleurs = List.of("blue", "red", "gray", "yellow", "orange", "violet");
        Random random = new Random();
        int choice = random.nextInt(couleurs.size());
        
        User user = new Avocat();
        
        user.setCreatedAt(Instant.EPOCH);
        user.setEmail(userNew.getEmail());
        user.setNom(userNew.getNom());
        user.setPrenom(userNew.getPrenom());
        user.setPassword(userNew.getPassword());
        user.setColor(couleurs.get(choice));
        
        
        repo.save(user);
        
        return "Bienvenue "+userNew.getPrenom();
    }
}

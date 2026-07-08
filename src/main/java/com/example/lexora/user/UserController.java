package com.example.lexora.user;

import com.example.lexora.avocat.Avocat;
import com.example.lexora.client.Client;
import com.example.lexora.publication.Publication;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    
    @GetMapping("/publication")
    public List<Publication> readPublicationByID(
            @RequestParam UUID id,
            @RequestParam String email
    ) {
        return service.lirePublicationById(id, email);
    }
    
    @PostMapping("/inscriptionAvocat")
    public ResponseEntity<String> inscriptionForVerification(
            @RequestParam("doc1") MultipartFile doc1,
            @RequestParam("doc2") MultipartFile doc2,
            @RequestParam("doc3") MultipartFile doc3,
            @RequestParam("description") String description,
            @RequestParam("specialite") List<String> specialite,
            @RequestParam("idClient") UUID id
    ) {        
        return ResponseEntity.ok(service.inscriptionAvocat(id));
    }
    
    
    // Adminstrateur Only
    
    @GetMapping("/users")
    public List<User> getUsers() {
        return service.getUsers();
    }
    
    @PutMapping("/modifierType")
    public Avocat modifier(@RequestBody Client client) {
        return service.modifierType(client.getId(), "AVOCAT");
    }
    
    @GetMapping("/clientToAvocat")
    public List<User> clientToAvocat(){
        return service.getWantBeAvocat();
    }

}

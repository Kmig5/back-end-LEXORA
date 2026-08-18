package com.example.lexora.user;

import com.example.lexora.user.enums.Specialite;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @PostMapping("/createUser")
    public ResponseEntity<ClientDTO> register(@RequestBody UserDTO userRegister) {

        return ResponseEntity.ok(service.registerClient(userRegister));
    }

    @PutMapping("/modifierUser")
    public ResponseEntity<String> modifier(@RequestBody UserDTO userRegister) {

        return ResponseEntity.ok(service.modifier(userRegister));
    }
    
    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam("actualPassword") String actualPassword,
            @RequestParam("nouveauPassword") String nouveauPassword,
            @RequestParam("id") UUID id) {
        return ResponseEntity.ok(service.changePasword(actualPassword, nouveauPassword, id));
    }

    // Adminstrateur seulement
    @GetMapping("/users")
    public List<User> getUsers() {
        return service.getUsers();
    }
    
    @GetMapping("/KPIUser")
    public Map<String,Long> getKPIUsers(){
        return service.getKPIUsers();
    }

    @PutMapping("/modifierType")
    public User modifier(@RequestBody User client) {
        return service.modifierType(client.getId(), "AVOCAT");
    }

    @GetMapping("/clientToAvocat")
    public List<User> clientToAvocat() {
        return service.getWantBeAvocat();
    }

    // Contrôleur pour les avocats
    @GetMapping("/avocat")
    public List<User> getAvocats() {
        return service.getAvocats();
    }

    @GetMapping("/avocat/recherche")
    public List<User> getAvocatSearch(
            @RequestParam("q") String q
    ) {
        return service.getAvocatSearch(q);
    }

    @PostMapping("/avocat/inscriptionAvocat")
    public ResponseEntity<String> inscriptionForVerification(
            @RequestParam("doc1") MultipartFile doc1,
            @RequestParam("doc2") MultipartFile doc2,
            @RequestParam("doc3") MultipartFile doc3,
            @RequestParam("description") String description,
            @RequestParam("specialite") Set<Specialite> specialite,
            @RequestParam("idClient") UUID id
    ) {
        return ResponseEntity.ok(service.inscriptionAvocat(id, description, specialite));
    }

    
    @GetMapping("/generation")
    public ResponseEntity<String> generation(){
        return service.genererUsers();
    }
}

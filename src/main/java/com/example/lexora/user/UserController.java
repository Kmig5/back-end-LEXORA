package com.example.lexora.user;

import com.example.lexora.user.enums.Specialite;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
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
    public Page<User> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getUsers(page, size);
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
    public Page<User> clientToAvocat(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getWantBeAvocat(page, size);
    }

    // Contrôleur pour les avocats
    @GetMapping("/avocat")
    public Page<User> getAvocats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getAvocats(page, size);
    }

    @GetMapping("/avocat/recherche")
    public Page<User> getAvocatSearch(
            @RequestParam("q") String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getAvocatSearch(q, page, size);
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

}

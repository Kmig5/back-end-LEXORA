package com.example.lexora.client;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Miguel
 */
@RestController
@RequestMapping("/lexora/user/client")
public class ClientController {

    public ClientController(ClientService service) {
        this.service = service;
    }
    private ClientService service;

    @PostMapping("/createClient")
    public ResponseEntity<Client> register(@RequestBody ClientDTO userRegister) {

        return ResponseEntity.ok(service.registerClient(userRegister));
    }
    
    @PutMapping("/modifierClient")
    public ResponseEntity<String> modifier(@RequestBody ClientDTO userRegister) {

        return ResponseEntity.ok(service.modifier(userRegister));
    }
    
    @GetMapping()
    public List<Client> getClients(){
        return service.getClients();
    }
}

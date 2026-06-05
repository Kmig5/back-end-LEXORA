
package com.example.lexora.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<String> register(@RequestBody ClientDTO userRegister) {
        
        service.registerClient(userRegister);

        return ResponseEntity.ok("Vous avez été enregistré avec succès");
    }
}

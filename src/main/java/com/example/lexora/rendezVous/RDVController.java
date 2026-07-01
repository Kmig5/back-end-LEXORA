package com.example.lexora.rendezVous;

import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Miguel
 */
@RestController()
@RequestMapping("/lexora/rendez-vous")
public class RDVController {
    
    private RDVService service;
    
    public RDVController(RDVService service) {
        this.service = service;
    }
    
    @PostMapping("/create-RDV")
    public String createRDV(@RequestBody RendezVousDTO rdvdto) {
        return service.creerRDV(rdvdto);
    }
    
    @GetMapping("/ownerRdv")
    public String getOwnerRDV(@RequestHeader UUID userId) {
        return " 'Vous n'avez pas encore de RDV' a dit le serveur";
    }
    
}

package com.example.lexora.rendezVous;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Miguel
 */

@RestController()
@RequestMapping("/lexora/rendez-vous")
public class RDVController {
    
    @PostMapping("/create-RDV")
    public String createRDV(@RequestBody RendezVousDTO rdvdto){
        return "RDV est arrivé dans Spring boot correctement";
    }
    
}

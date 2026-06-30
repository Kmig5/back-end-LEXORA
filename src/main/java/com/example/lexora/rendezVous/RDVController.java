package com.example.lexora.rendezVous;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author Miguel
 */

@Controller("/lexora/rendez-vous")
public class RDVController {
    
    @PostMapping("/create-RDV")
    public String createRDV(@RequestBody RendezVousDTO rdvdto){
        return "RDV est arrivé dans Spring boot correctement";
    }
    
}

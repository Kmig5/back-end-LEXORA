package com.example.lexora.rendezVous;

import com.example.lexora.rendezVous.dto.ModifierStatut;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public List<RendezVous> getOwnerRDVforAvocat(@RequestHeader UUID userId) {
        return service.getOwnerRDV(userId);
    }
    
    @PatchMapping("/{id}/statut")
    public void modifierStatut(@PathVariable Long id, @RequestBody ModifierStatut statut) {
        service.modifierStatut(id, statut.getStatut());
    }
    
    // ONLY ADMIN
    
    @GetMapping
    public List<RendezVous> getAllRdv() {
        return service.getAllRdv();
    }
    
    @GetMapping("/KPIRdv")
    public Map<String, Long> getKPIRdv() {
        return service.getKPIRdv();
    }
    
}

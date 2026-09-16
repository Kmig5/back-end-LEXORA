package com.example.lexora.rendezVous;

import com.example.lexora.rendezVous.dto.ModifierStatut;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public Page<RendezVous> getOwnerRDVforAvocat(
            @RequestHeader UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getOwnerRDV(userId, page, size);
    }
    
    @PatchMapping("/{id}/statut")
    public void modifierStatut(@PathVariable Long id, @RequestBody ModifierStatut statut) {
        service.modifierStatut(id, statut.getStatut());
    }
    
    // ONLY ADMIN
    
    @GetMapping
    public Page<RendezVous> getAllRdv(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getAllRdv(page, size);
    }
    
    @GetMapping("/KPIRdv")
    public Map<String, Long> getKPIRdv() {
        return service.getKPIRdv();
    }
    
}

package com.example.lexora.cabinet;

import com.example.lexora.cabinet.dto.CabinetDTO;
import com.example.lexora.user.User;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Miguel
 */
@Service
public class CabinetService {

    private CabinetRepository repo;

    public CabinetService(CabinetRepository repo) {
        this.repo = repo;
    }

    public String createCabinet(CabinetDTO cabinet) {
        Cabinet cabinetBD = new Cabinet();

        cabinetBD.setNom(cabinet.getNom());
        cabinetBD.setAdresse(cabinet.getAdresse());
        cabinetBD.setAvocat(cabinet.getAvocat());
        cabinetBD.setDescription(cabinet.getDescription());
        cabinetBD.setLocalisation(cabinet.getLocalisation());

        repo.save(cabinetBD);

        return "Cabinet Crée avec succès";
    }
    
    public List<Cabinet> getMyCabinet(User avocat) {
        return repo.findByAvocat(avocat);
    }
}

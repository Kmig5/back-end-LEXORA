package com.example.lexora.rendezVous;

import com.example.lexora.user.User;
import com.example.lexora.user.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 *
 * @author Miguel
 */
@Service
public class RDVService {

    private UserRepository repoUser;
    private RDVRepository repo;

    public RDVService(UserRepository repoUser, RDVRepository repo) {
        this.repoUser = repoUser;
        this.repo = repo;
    }

    String creerRDV(RendezVousDTO rdvdto) {

        RendezVous rdv = new RendezVous();

        Optional<User> avocat = repoUser.findById(rdvdto.getAvocat_id());
        Optional<User> client = repoUser.findById(rdvdto.getClient_id());

        if (avocat.isPresent() && client.isPresent()) {
            rdv.setAvocat(avocat.get());
            rdv.setClient(client.get());
            rdv.setDateTime(rdvdto.getDateTime());
            rdv.setMode(rdvdto.getMode());
            rdv.setMotif(rdvdto.getMotif());

            repo.save(rdv);
        } else {
            return "ERROR";
        }

        return "Le Rendez-Vous a été sauvegarder dans la BD";
    }
    
    
    List<RendezVous> getOwnerRDV(UUID userId) {
        return repo.findByAvocat_Id(userId);
    }
    
    List<RendezVous> getAllRdv(){
        return repo.findAll();
    }

}

package com.example.lexora.rendezVous;

import com.example.lexora.avocat.Avocat;
import com.example.lexora.avocat.AvocatRepository;
import com.example.lexora.client.Client;
import com.example.lexora.client.ClientRepository;
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

    private AvocatRepository repoAvocat;
    private ClientRepository repoClient;
    private RDVRepository repo;

    public RDVService(AvocatRepository repoAvocat, ClientRepository repoClient, RDVRepository repo) {
        this.repoAvocat = repoAvocat;
        this.repoClient = repoClient;
        this.repo = repo;
    }

    String creerRDV(RendezVousDTO rdvdto) {

        RendezVous rdv = new RendezVous();

        Optional<Avocat> avocat = repoAvocat.findById(rdvdto.getAvocat_id());
        Optional<Client> client = repoClient.findById(rdvdto.getClient_id());

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

}

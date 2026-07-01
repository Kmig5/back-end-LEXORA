package com.example.lexora.publication;

import com.example.lexora.client.Client;
import com.example.lexora.client.ClientRepository;
import com.example.lexora.user.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 *
 * @author Julie Powo
 */
@Service
public class PublicationService {

    private PublicationRepository repo;
    private ClientRepository repoClient;

    public PublicationService(PublicationRepository repo, ClientRepository repoClient) {
        this.repo = repo;
        this.repoClient = repoClient;
    }

    

    public String creerPublication(PublicationDTO pub) {
        Publication newPub = new Publication();

        Optional<Client> user = repoClient.findById(pub.getUser());

        if (user.isPresent()) {
            newPub.setUser(user.get());
        }

        newPub.setDomaine(pub.getDomaine());
        newPub.setQuestion(pub.getQuestion());
        newPub.setContenu(pub.getContenu());
        repo.save(newPub);
        
        return "Publication créer avec succès";
    }

    public List<Publication> lirePublication() {
        List<Publication> liste = repo.findAll();

        return liste;
    }

    public List<Publication> lirePublicationById(UUID id) {
        return repo.findByUser_Id(id);
    }

    public String modification(Publication pub) {
        repo.save(pub);
        return "Publication modifié avec succès";
    }
}

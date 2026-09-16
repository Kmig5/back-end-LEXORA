package com.example.lexora.publication;

import com.example.lexora.user.User;
import com.example.lexora.user.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Julie Powo
 */
@Service
public class PublicationService {

    private PublicationRepository repo;
    private UserRepository repoClient;

    public PublicationService(PublicationRepository repo, UserRepository repoClient) {
        this.repo = repo;
        this.repoClient = repoClient;
    }

    

    public String creerPublication(PublicationDTO pub) {
        Publication newPub = new Publication();

        Optional<User> user = repoClient.findById(pub.getUser());

        if (user.isPresent()) {
            newPub.setUser(user.get());
        }

        newPub.setDomaine(pub.getDomaine());
        newPub.setQuestion(pub.getQuestion());
        newPub.setContenu(pub.getContenu());
        repo.save(newPub);
        
        return "Publication créer avec succès";
    }

    public Page<Publication> lirePublication(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAll(pageable);
    }

    public List<Publication> lirePublicationById(UUID id) {
        return repo.findByUser_Id(id);
    }

    public String modification(PublicationDTO pub) {
        Publication newPub = new Publication();
        
        Optional<User> user = repoClient.findById(pub.getUser());

        if (user.isPresent()) {
            newPub.setUser(user.get());
        }
        
        newPub.setId(pub.getId());
        newPub.setDomaine(pub.getDomaine());
        newPub.setQuestion(pub.getQuestion());
        newPub.setContenu(pub.getContenu());
        
        repo.save(newPub);
        return "Publication modifié avec succès";
    }
}

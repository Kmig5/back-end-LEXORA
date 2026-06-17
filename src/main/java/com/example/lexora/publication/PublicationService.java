
package com.example.lexora.publication;

import com.example.lexora.user.User;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 *
 * @author Julie Powo
 */

@Service
public class PublicationService {
    private PublicationRepository repo;

    public PublicationService(PublicationRepository repo) {
        this.repo = repo;
    }
    
    public String creerPublication(Publication pub){
        repo.save(pub);
        return "Publication créer";
    }
    
    public List<Publication> lirePublication(){
        List<Publication> liste = repo.findAll();
        
        return liste;
    }
    
    public List<Publication> lirePublicationById(User user) {
        return repo.findByUser(user);
    }
}

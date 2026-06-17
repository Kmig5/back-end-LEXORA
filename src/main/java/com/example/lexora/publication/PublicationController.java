package com.example.lexora.publication;

import com.example.lexora.client.Client;
import com.example.lexora.client.ClientRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Miguel
 */
@RestController
@RequestMapping("/lexora/publication")
public class PublicationController {

    private PublicationService service;
    private ClientRepository repoClient;

    public PublicationController(PublicationService service, ClientRepository repoClient) {
        this.service = service;
        this.repoClient = repoClient;
    }

    @PostMapping("/create")
    public String creation(@RequestBody PublicationDTO pub) {
        Publication newPub = new Publication();

        Optional<Client> user = repoClient.findById(pub.getUser());

        if (user.isPresent()) {
            newPub.setUser(user.get());
        }

        newPub.setDomaine(pub.getDomaine());
        newPub.setQuestion(pub.getQuestion());
        newPub.setContenu(pub.getContenu());

        return service.creerPublication(newPub);
    }

    @GetMapping("/read")
    public List<Publication> readPublication() {
        return service.lirePublication();
    }
    
    @GetMapping("/readId}")
    public List<Publication> readPublicationByID(
            @RequestParam UUID id
    ) {
        return service.lirePublicationById(id);
    }
}

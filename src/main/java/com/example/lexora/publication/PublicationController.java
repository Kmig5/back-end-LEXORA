package com.example.lexora.publication;

import com.example.lexora.client.Client;
import com.example.lexora.client.ClientRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    public PublicationController(PublicationService service) {
        this.service = service;
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
}

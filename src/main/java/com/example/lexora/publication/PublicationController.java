package com.example.lexora.publication;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    public PublicationController(PublicationService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public String creationPublication(@RequestBody PublicationDTO pub) {
        return service.creerPublication(pub);
    }

    @GetMapping("/read")
    public ResponseEntity<Page<Publication>> readPublication(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.lirePublication(page, size));
    }
    
    @GetMapping("/readId")
    public List<Publication> readPublicationByID(
            @RequestParam UUID id
    ) {
        return service.lirePublicationById(id);
    }
    
    @PutMapping("/modif")
    public String modifier (@RequestBody PublicationDTO pub) {
        return service.modification(pub);
    }
}

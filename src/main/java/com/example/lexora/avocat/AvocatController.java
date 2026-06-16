package com.example.lexora.avocat;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/lexora/user/avocat")
public class AvocatController {

    private AvocatService service;

    public AvocatController(AvocatService service) {
        this.service = service;
    }

    @GetMapping
    public List<Avocat> getAvocat() {
        return service.getAvocat();
    }
    
    @GetMapping("recherche")
    public List<Avocat> getAvocatSearch(
            @RequestParam("q") String q
    ) {
        return service.getAvocatSearch(q);
    }

    @GetMapping("/filtreAvocat")
    public Page<Avocat> getAvocatFiltre(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String specialite,
            @RequestParam(required = false) Integer annee) {
        Pageable pageable = PageRequest.of(0, 50);

        return service.getAvocatFiltre(region, specialite, annee, pageable);
    }

    @PostMapping("/createAvocat")
    public ResponseEntity<String> createAvocat(@RequestBody AvocatDTO avocat) {

        return ResponseEntity.ok(service.registerAvocat(avocat));
    }

}

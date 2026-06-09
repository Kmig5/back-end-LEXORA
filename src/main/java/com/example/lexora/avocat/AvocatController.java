package com.example.lexora.avocat;

import java.util.List;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/lexora/user/avocat")
public class AvocatController {

    private AvocatService service;

    @GetMapping
    public List<Avocat> getAvocat() {
        return service.getAvocat();
    }

    @PostMapping("/createAvocat")
    public ResponseEntity<String> createAvocat(@RequestBody AvocatDTO avocat) {

        return ResponseEntity.ok(service.registerAvocat(avocat));
    }
}

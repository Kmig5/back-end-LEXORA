package com.example.lexora.avocat;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Miguel
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AvocatDTO {

    private String nom;
    private String prenom;
    private String email;
    private String role;
    private String password;
    private String region;
    private String ville;
    private Instant createdAt;
    private List<String> specialite;
    private int honoraire;
    private int annee;
    private String description;
}

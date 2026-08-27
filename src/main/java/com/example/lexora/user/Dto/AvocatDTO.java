package com.example.lexora.user.Dto;

import com.example.lexora.cabinet.Cabinet;
import com.example.lexora.publication.Publication;
import com.example.lexora.user.User;
import com.example.lexora.user.enums.Specialite;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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

    private UUID id;
    private String nom;
    private String prenom;
    private String numeroTel;
    private String email;
    private String color;
    private String region;
    private String ville;
    private Instant createdAt;
    private String typeUtilisateur;
    private List<Publication> posts;
    private Boolean wantBeVerified;
    private Boolean isVerified;
    // Variable spécifique aux avocats
    private Set<Specialite> specialite = new HashSet<>();
    private Float note;
    private Integer honoraire;
    private Integer annee;
    private String description;
    private List<Cabinet> cabinets;
    
    public AvocatDTO userToAvocat(User user) {
        AvocatDTO avocat = new AvocatDTO();
        
        avocat.setId(user.getId());
        avocat.setEmail(user.getEmail());
        avocat.setNom(user.getNom());
        avocat.setPrenom(user.getPrenom());
        avocat.setVille(user.getVille());
        avocat.setRegion(user.getRegion());
        avocat.setColor(user.getColor());
        avocat.setCreatedAt(user.getCreatedAt());
        avocat.setNumeroTel(user.getNumeroTel());
        avocat.setTypeUtilisateur(user.getTypeUtilisateur());
        
        avocat.setSpecialite(user.getSpecialite());
        avocat.setDescription(user.getDescription());
        avocat.setCabinets(user.getCabinets());
        
        return avocat;
    }
    
}

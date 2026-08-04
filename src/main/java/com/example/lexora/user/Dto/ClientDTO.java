package com.example.lexora.user;

import com.example.lexora.publication.Publication;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Miguel
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

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

    public ClientDTO userToClient(User user) {
        ClientDTO client = new ClientDTO();
        
        client.setId(user.getId());
        client.setEmail(user.getEmail());
        client.setNom(user.getNom());
        client.setPrenom(user.getPrenom());
        client.setVille(user.getVille());
        client.setRegion(user.getRegion());
        client.setColor(user.getColor());
        client.setCreatedAt(user.getCreatedAt());
        client.setNumeroTel(user.getNumeroTel());
        client.setTypeUtilisateur(user.getTypeUtilisateur());
        
        return client;
    }
}

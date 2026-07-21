package com.example.lexora.user.Dto;

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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

}

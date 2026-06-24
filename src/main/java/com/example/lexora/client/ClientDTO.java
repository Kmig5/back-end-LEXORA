package com.example.lexora.client;

import java.time.Instant;
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
class ClientDTO {
    
    private UUID id;
    private String nom;
    private String prenom;
    private String email;
    private String role;
    private String password;
    private String region;
    private String ville;
    private String numeroTel;
    
    private Instant createdAt;
}

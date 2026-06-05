
package com.example.lexora.client;

import java.time.Instant;
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
    private String nom;
    private String prenom;
    private String email;
    private String role;
    private String password;

    private Instant createdAt;
}

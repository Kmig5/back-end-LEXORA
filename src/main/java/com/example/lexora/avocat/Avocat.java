package com.example.lexora.avocat;

import com.example.lexora.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Miguel
 */

@Entity
@DiscriminatorValue("AVOCAT")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Avocat extends User {
    private String couleur;
    private String specialite;
    private String region;
    private String ville;
    private int note;
    
    private boolean isVerified;
}

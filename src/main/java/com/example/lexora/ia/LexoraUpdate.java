package com.example.lexora.ia;

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
class LexoraUpdate {

    private String specialite;
    private String ville;
    private String consultationMode;
    private String language;
    private Double budgetMin;
    private Double budgetMax;
    private String urgence;
}

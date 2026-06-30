package com.example.lexora.rendezVous;

import com.example.lexora.avocat.Avocat;
import com.example.lexora.client.Client;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Miguel
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RendezVousDTO {
    
    private Avocat avocat;
    
    private Client client;
    
    private LocalDateTime dateTime;
    
    private Integer duree;
    
    private String motif;
    
    private String commentaireAvocat;
    
    private Statut statut;
    
    private ModeConsultation mode;
    
}

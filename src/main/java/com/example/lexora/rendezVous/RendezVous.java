package com.example.lexora.rendezVous;

import com.example.lexora.rendezVous.enums.ModeConsultation;
import com.example.lexora.rendezVous.enums.Statut;
import com.example.lexora.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

/**
 *
 * @author Miguel
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "avocat_id")
    private User avocat;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    private LocalDateTime dateTime;

    private Integer duree;

    @Column(length = 1000)
    private String motif;

    @Column(length = 1000)
    private String commentaireAvocat;

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.EN_ATTENTE;

    @Enumerated(EnumType.STRING)
    private ModeConsultation mode;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

}

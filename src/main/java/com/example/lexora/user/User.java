package com.example.lexora.user;

import com.example.lexora.cabinet.Cabinet;
import com.example.lexora.publication.Publication;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author Miguel
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nom;
    private String prenom;

    private String numeroTel;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String color;

    private String region;
    private String ville;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "type_utilisateur")
    private String typeUtilisateur;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Publication> posts;

    private Boolean wantBeVerified;

    private Boolean isVerified;

    // Variable spécifique aux avocats
    @ElementCollection
    @CollectionTable(
            name = "avocat_specialites",
            joinColumns = @JoinColumn(name = "avocat_id")
    )
    @Column(name = "specialite")
    private List<String> specialite = new ArrayList<>();

    private Float note;

    private Integer honoraire;

    private Integer annee;

    private String description;
    
    @OneToMany(mappedBy = "avocat")
    @JsonIgnore
    private List<Cabinet> cabinets;

}

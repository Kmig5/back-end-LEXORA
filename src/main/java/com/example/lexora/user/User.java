package com.example.lexora.user;

import com.example.lexora.avocat.Avocat;
import com.example.lexora.client.Client;
import com.example.lexora.publication.Publication;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_utilisateur")
@DiscriminatorValue("USER")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.PROPERTY, 
    property = "role"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Avocat.class, name = "AVOCAT"),
    @JsonSubTypes.Type(value = Client.class, name = "CLIENT")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String nom;
    private String prenom;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private String color;
    
    private String region;
    private String ville;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    @OneToMany
    private List<Publication> posts;
    
}

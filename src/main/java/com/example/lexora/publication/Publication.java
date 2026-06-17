package com.example.lexora.publication;

import com.example.lexora.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Publication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    private String domaine;
    
    private String question;
    
    @Column(columnDefinition = "TEXT")
    private String contenu;
    
    private String typePublication;
    
    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private User user;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    final private Instant createdAt = Instant.now();
}

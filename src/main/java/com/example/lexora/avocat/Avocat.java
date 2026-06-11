package com.example.lexora.avocat;

import com.example.lexora.user.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
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

    @ElementCollection
    @CollectionTable(
            name = "avocat_specialites",
            joinColumns = @JoinColumn(name = "avocat_id")
    )
    @Column(name = "specialite")
    private List<String> specialite = new ArrayList<>();

    private Integer note;

    private boolean isVerified;

    private int honoraire;

    private Integer annee;

    private String description;
}

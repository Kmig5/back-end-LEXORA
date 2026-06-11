package com.example.lexora.avocat;

import com.example.lexora.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "specialite", columnDefinition = "jsonb")
    public List<String> specialite = new ArrayList<>();

    private int note;

    private boolean isVerified;

    private int honoraire;

    private int annee;
    
    private String description;
}

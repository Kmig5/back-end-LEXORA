package com.example.lexora.cabinet;

import com.example.lexora.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

/**
 *
 * @author Miguel
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cabinet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String description;

    @ManyToOne
    @JoinColumn(name = "avocat_id")
    private User avocat;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point localisation;

    private String adresse;
}

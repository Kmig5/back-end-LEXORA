
package com.example.lexora.cabinet.dto;

import com.example.lexora.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

/**
 *
 * @author Miguel
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CabinetDTO {
    
    private String nom;

    private String description;

    private User avocat;

    private Point localisation;

    private String adresse;
}

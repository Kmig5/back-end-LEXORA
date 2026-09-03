package com.example.lexora.cabinet.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Miguel
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Localisation {

    private String type;
    private List<Double> coordinates;
}

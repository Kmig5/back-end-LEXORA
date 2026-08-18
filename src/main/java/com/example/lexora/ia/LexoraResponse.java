package com.example.lexora.ia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Miguel
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LexoraResponse {

    private String conversationId;
    private String response;
    private LexoraUpdate orientationUpdate;
    private boolean searchLawyers;
}

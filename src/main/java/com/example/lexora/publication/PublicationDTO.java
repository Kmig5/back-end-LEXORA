
package com.example.lexora.publication;

import com.example.lexora.user.User;
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
class PublicationDTO {
    private String domaine;
    
    private String question;
    
    private String contenu;
    
    private User user;
    
}


package com.example.lexora.publication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long>{
    
}

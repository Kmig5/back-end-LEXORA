
package com.example.lexora.publication;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long>{
    public List<Publication> findByUserId(UUID userId);
}

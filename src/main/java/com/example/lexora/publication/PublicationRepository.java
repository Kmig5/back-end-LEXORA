
package com.example.lexora.publication;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long>{
    public Page<Publication> findByUser_Id(UUID id, Pageable pageable);
}

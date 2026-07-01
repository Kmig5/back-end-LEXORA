package com.example.lexora.rendezVous;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */
@Repository
public interface RDVRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByAvocat_Id(UUID userId);
}

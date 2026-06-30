package com.example.lexora.rendezVous;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */
@Repository
public interface RDVRepository extends JpaRepository<RendezVous, Long> {

}

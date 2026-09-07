package com.example.lexora.rendezVous;

import com.example.lexora.rendezVous.enums.Statut;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */
@Repository
public interface RDVRepository extends JpaRepository<RendezVous, Long> {    
    @Query("""
    SELECT r FROM RendezVous r
        WHERE r.avocat.id = :userId
        OR r.client.id = :userId
    """)
    List<RendezVous> findByUserId(@Param("userId") UUID userId);
    
    @Query("""
           SELECT COUNT(rdv) FROM RendezVous rdv
           WHERE statut = :statut
           """)
    Long countByStatut(@Param("statut") Statut statut);
}

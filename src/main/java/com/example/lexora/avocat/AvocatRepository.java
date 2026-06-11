package com.example.lexora.avocat;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */
@Repository
public interface AvocatRepository extends JpaRepository<Avocat, UUID> {

    @Query(value = "SELECT * FROM users WHERE "
            + "(:region IS NULL OR region = :region) AND "
            + "(:specialite IS NULL OR jsonb_exists(specialite, CAST(:specialite AS text))) AND "
            + "(:annee IS NULL OR annee <= :annee) AND "
            + "type_utilisateur = 'AVOCAT' AND "
            + "is_verified = true",
            nativeQuery = true)
    Page<Avocat> rechercherMultiCriteres(
            @Param("region") String region,
            @Param("specialite") String specialite,
            @Param("annee") Integer annee,
            Pageable pageable
    );
}

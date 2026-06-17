package com.example.lexora.avocat;

import com.example.lexora.user.User;
import java.util.List;
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

    @Query(value = "SELECT DISTINCT u.* FROM users u "
            + "LEFT JOIN avocat_specialites s ON u.id = s.avocat_id WHERE "
            + "(:region IS NULL OR :region = '' OR LOWER(u.region) = LOWER(:region)) AND "
            + "(:specialite IS NULL OR :specialite = '' OR LOWER(s.specialite) = LOWER(:specialite)) AND "
            + "(:annee IS NULL OR u.annee <= :annee) AND "
            + "u.type_utilisateur = 'AVOCAT' AND "
            + "u.is_verified = true",
            countQuery = "SELECT COUNT(DISTINCT u.id) FROM users u "
            + "LEFT JOIN avocat_specialites s ON u.id = s.avocat_id WHERE "
            + "(:region IS NULL OR :region = '' OR LOWER(u.region) = LOWER(:region)) AND "
            + "(:specialite IS NULL OR :specialite = '' OR LOWER(s.specialite) = LOWER(:specialite)) AND "
            + "(:annee IS NULL OR u.annee <= :annee) AND "
            + "u.type_utilisateur = 'AVOCAT' AND "
            + "u.is_verified = true",
            nativeQuery = true)
    Page<Avocat> rechercherMultiCriteres(
            @Param("region") String region,
            @Param("specialite") String specialite,
            @Param("annee") Integer annee,
            Pageable pageable
    );

    @Query(value = """
               SELECT DISTINCT u FROM User u 
               LEFT JOIN u.specialite s 
               WHERE LOWER(u.typeUtilisateur) = 'avocat' 
               AND (
                  LOWER(u.nom) LIKE LOWER(CONCAT('%', :q, '%')) 
                  OR LOWER(u.prenom) LIKE LOWER(CONCAT('%', :q, '%')) 
                  OR LOWER(u.region) LIKE LOWER(CONCAT('%', :q, '%'))
                  OR LOWER(u.ville) LIKE LOWER(CONCAT('%', :q, '%'))
                  OR LOWER(s) LIKE LOWER(CONCAT('%', :q, '%'))
                  OR LOWER(u.description) LIKE LOWER(CONCAT('%', :q, '%'))
               )
               """)
    List<Avocat> rechercheDebounce(@Param("q") String q);

}

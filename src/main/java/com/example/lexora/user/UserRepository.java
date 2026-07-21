package com.example.lexora.user;

import com.example.lexora.publication.Publication;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Miguel
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    public User findByEmail(String email);

    public List<Publication> findByIdAndEmail(UUID id, String email);

    // Administrateur Only
    @Transactional
    @Modifying(clearAutomatically = true) // C'est le secret pour forcer Hibernate à rafraîchir ses objets en mémoire
    @Query(value = "UPDATE users SET type_utilisateur = :nouveauType WHERE id = :userId", nativeQuery = true)
    int changerTypeUtilisateur(@Param("userId") UUID userId, @Param("nouveauType") String nouveauType);

    @Query(value = "SELECT * FROM users WHERE want_be_verified = true", nativeQuery = true)
    List<User> findUsersWaitingForAvocatApproval();

    // Repository pour les Avocat
    @Query("""
    SELECT DISTINCT a
    FROM User a
    LEFT JOIN a.specialite s
    WHERE a.typeUtilisateur = 'AVOCAT'
    AND (
        LOWER(a.nom) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(a.prenom) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(a.region) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(a.ville) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(s) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(a.description) LIKE LOWER(CONCAT('%', :q, '%')) )
""")
    List<User> rechercheDebounce(@Param("q") String q); // méthode pour faire des recherches sur les avocats

    @Query("""
           SELECT DISTINCT a
           FROM users a
           WHERE a.typeUtilisateur = 'AVOCAT'
           """)
    List<User> getAvocat(); // méthode pour récupérer tous les avocats en BD

}

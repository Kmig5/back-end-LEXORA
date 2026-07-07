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
}

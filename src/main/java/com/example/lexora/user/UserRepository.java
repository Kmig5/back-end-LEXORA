package com.example.lexora.user;

import com.example.lexora.publication.Publication;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    public User findByEmail(String email);
    
    public List<Publication> findByIdAndEmail(UUID id, String email);
}

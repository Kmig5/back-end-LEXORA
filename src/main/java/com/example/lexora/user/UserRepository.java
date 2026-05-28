package com.example.lexora.user;

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
}

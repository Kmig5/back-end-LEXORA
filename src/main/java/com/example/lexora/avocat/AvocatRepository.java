package com.example.lexora.avocat;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */

@Repository
public interface AvocatRepository extends JpaRepository<Avocat, UUID>{
    
}

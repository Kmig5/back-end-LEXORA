
package com.example.lexora.client;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Julie Powo
 */
interface ClientRepository extends JpaRepository<Client, UUID>{
    
}

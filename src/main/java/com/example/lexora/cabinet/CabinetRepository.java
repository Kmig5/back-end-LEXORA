
package com.example.lexora.cabinet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */

@Repository
public class CabinetRepository implements JpaRepository<Long, Cabinet> {
    
}

package com.example.lexora.cabinet;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */
@Repository
public interface CabinetRepository extends JpaRepository<Cabinet, Long> {

    public List<Cabinet> findByAvocat(UUID id);
}

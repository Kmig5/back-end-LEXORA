package com.example.lexora.cabinet;

import com.example.lexora.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */
@Repository
public interface CabinetRepository extends JpaRepository<Cabinet, Long> {

    public List<Cabinet> findByAvocat(User avocat);
}

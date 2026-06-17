
package com.example.lexora.publication;

import com.example.lexora.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Miguel
 */

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long>{
    public List<Publication> findByUser(User user);
}

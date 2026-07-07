package com.example.lexora.user;

import com.example.lexora.avocat.Avocat;
import com.example.lexora.publication.Publication;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Miguel
 */
@Service
public class UserService {

    private UserRepository repo;

    BCryptPasswordEncoder encoder;

    public UserService(UserRepository repo, BCryptPasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public ResponseEntity<?> login(String email, String passw) {

        User userBD = repo.findByEmail(email);

        if (userBD == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("email pas trouvé");
        }

        if (encoder.matches(passw, userBD.getPassword())) {
            UserDTO user = new UserDTO();
            user.setId(userBD.getId());
            user.setEmail(userBD.getEmail());
            user.setNom(userBD.getNom());
            user.setPrenom(userBD.getPrenom());
            user.setVille(userBD.getVille());
            user.setRegion(userBD.getRegion());
            user.setColor(userBD.getColor());
            user.setCreatedAt(userBD.getCreatedAt());
            user.setNumeroTel(userBD.getNumeroTel());
            user.setTypeUtilisateur(userBD.getTypeUtilisateur());
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERREUR Veuillez reessayer");
    }

    public List<Publication> lirePublicationById(UUID id, String email) {
        return repo.findByIdAndEmail(id, email);
    }

    // Administrateur Only
    public List<User> getUsers() {
        return repo.findAll();
    }

    @Transactional
    public Avocat modifierType(UUID id, String nouveauType) {
        repo.changerTypeUtilisateur(id, nouveauType);

        User userConverti = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur pas trouvé"));

        if (userConverti instanceof Avocat avocat) {
            return repo.save(avocat);
        } else {
            throw new RuntimeException("La conversion de type a échoué en base de données.");
        }

    }

    public List<User> getWantBeAvocat() {        
        return repo.findUsersWaitingForAvocatApproval();
    }
}

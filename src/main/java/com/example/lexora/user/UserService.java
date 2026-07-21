package com.example.lexora.user;

import com.example.lexora.publication.Publication;
import com.example.lexora.user.Dto.ClientDTO;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
            ClientDTO user = new ClientDTO();
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

    public String inscriptionAvocat(UUID id) {
        Optional<User> user = repo.findById(id);
        if (user.isPresent()) {
            User client = user.get();
            client.setWantBeVerified(Boolean.TRUE);
            repo.save(client);
            return "Vos informations seront vérifiées et validées par notre équipe LEXORA. Merci";
        }

        return "Uilisateur non existant";
    }

    // Administrateur Only
    public List<User> getUsers() {
        return repo.findAll();
    }

    @Transactional
    public User modifierType(UUID id, String nouveauType) {

        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        user.setTypeUtilisateur(nouveauType);

        if (user.getIsVerified() == null) {
            user.setIsVerified(false);
        }

        repo.save(user);

        repo.flush();

        throw new RuntimeException("La conversion a échoué.");
    }

    public List<User> getWantBeAvocat() {
        return repo.findUsersWaitingForAvocatApproval();
    }

    // Service pour les Avocats
    public List<User> getAvocats() {
        return repo.findAll();
    }

    public List<User> getAvocatSearch(String q) {
        return repo.rechercheDebounce(q);
    }

    // Service pour les users clients de base
    public User registerClient(UserDTO clientNew) {
        List<String> couleurs = List.of("blue", "red", "gray", "yellow", "orange", "violet");
        Random random = new Random();
        int choice = random.nextInt(couleurs.size());

        String passwHash = encoder.encode(clientNew.getPassword());

        User user = new User();

        user.setEmail(clientNew.getEmail());
        user.setNom(clientNew.getNom());
        user.setPrenom(clientNew.getPrenom());
        user.setPassword(passwHash);
        user.setColor(couleurs.get(choice));
        user.setRegion(clientNew.getRegion());
        user.setVille(clientNew.getVille());
        user.setNumeroTel(clientNew.getNumeroTel());

        repo.save(user);

        return retournerUserInscrit(user);
    }

    private User retournerUserInscrit(User clt) {
        clt.setPassword(null);
        return clt;
    }

    public String modifier(UserDTO userNew) {
        User client = new User();

        Optional<User> clientBD = repo.findById(userNew.getId());

        if (clientBD.isPresent()) {
            client = clientBD.get();

            client.setId(userNew.getId());
            client.setEmail(userNew.getEmail());
            client.setNom(userNew.getNom());
            client.setPrenom(userNew.getPrenom());
            client.setRegion(userNew.getRegion());
            client.setVille(userNew.getVille());
            client.setNumeroTel(userNew.getNumeroTel());
        }

        repo.save(client);

        return "Modification ajouté avec succès";
    }
}

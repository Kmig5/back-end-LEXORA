package com.example.lexora.user;

import com.example.lexora.publication.Publication;
import com.example.lexora.user.Dto.AvocatDTO;
import com.example.lexora.user.enums.Specialite;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
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
            if("CLIENT".equals(userBD.getTypeUtilisateur())) {
                ClientDTO client = new ClientDTO();
                return ResponseEntity.ok(client.userToClient(userBD));
            } else if("AVOCAT".equals(userBD.getTypeUtilisateur())) {
                AvocatDTO avocat = new AvocatDTO();
                return ResponseEntity.ok(avocat.userToAvocat(userBD));
            }
            
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERREUR Veuillez reessayer");
    }

    public List<Publication> lirePublicationById(UUID id, String email) {
        return repo.findByIdAndEmail(id, email);
    }
    
    public String changePasword(String ancien, String nouveau, UUID id) {
        Optional<User> userBD = repo.findById(id);
        if(userBD.isPresent() && encoder.matches(ancien, userBD.get().getPassword())) {
            userBD.get().setPassword(nouveau);
            return "mot de passe enregistré avec succès";
        }
        return "Erreur d'enregistrement";
    }

    public String inscriptionAvocat(UUID id, String description, Set<Specialite> specialite) {
        Optional<User> user = repo.findById(id);
        if (user.isPresent()) {
            User client = user.get();
            client.setWantBeVerified(Boolean.TRUE);
            client.setDescription(description);
            client.setSpecialite(specialite);
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
        user.setIsVerified(true);
        user.setWantBeVerified(Boolean.FALSE);

        return repo.save(user);

    }

    public List<User> getWantBeAvocat() {
        return repo.findUsersWaitingForAvocatApproval();
    }
    
    public Map<String, Long> getKPIUsers() {
        
        Map<String,Long> liste = new HashMap<>();
        liste.put("nbreUsers", repo.count());
        liste.put("nbreAvocats", repo.countByType("AVOCAT"));
        liste.put("nbreClients", repo.countByType("CLIENT"));
        
        return liste;
    }

    // Service pour les Avocats
    public List<User> getAvocats() {
        return repo.getAvocat();
    }

    public List<User> getAvocatSearch(String q) {
        return repo.rechercheDebounce(q);
    }

    // Service pour les users clients de base
    public ClientDTO registerClient(UserDTO clientNew) {
        ClientDTO client = new ClientDTO();
        List<String> couleurs = List.of("blue", "red", "gray", "yellow", "orange", "violet");
        Random random = new Random();
        int choice = random.nextInt(couleurs.size());

        String passwHash = encoder.encode(clientNew.getPassword());

        User user = new User();

        user.setTypeUtilisateur("CLIENT");
        user.setEmail(clientNew.getEmail());
        user.setNom(clientNew.getNom());
        user.setPrenom(clientNew.getPrenom());
        user.setPassword(passwHash);
        user.setColor(couleurs.get(choice));
        user.setRegion(clientNew.getRegion());
        user.setVille(clientNew.getVille());
        user.setNumeroTel(clientNew.getNumeroTel());

        repo.save(user);

        return client.userToClient(user);
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

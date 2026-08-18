package com.example.lexora.user;

import com.example.lexora.publication.Publication;
import com.example.lexora.user.enums.Specialite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
            ClientDTO client = new ClientDTO();
            return ResponseEntity.ok(client.userToClient(userBD));
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
            client.setSpecialites(specialite);
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
    
    
    
    
    
    
    public ResponseEntity<String> genererUsers() {

        /*
     * Ordre de chaque Object[] :
     *
     * 0  nom
     * 1  prenom
     * 2  numeroTel
     * 3  email
     * 4  color
     * 5  region
     * 6  ville
     * 7  typeUtilisateur
     * 8  specialites
     * 9  note
     * 10 honoraire
     * 11 annee
     * 12 description
         */
        List<Object[]> utilisateurs
                = new ArrayList<>();

        /*
     * =====================================
     * 10 AVOCATS
     * =====================================
         */
        utilisateurs.add(new Object[]{
            "Mballa",
            "Jean",
            "690100001",
            "jean.mballa.avocat@lexora-test.cm",
            "#1E3A8A",
            "Centre",
            "Yaoundé",
            "AVOCAT",
            Set.of(
            Specialite.DROIT_DU_TRAVAIL,
            Specialite.DROIT_CIVIL
            ),
            4.7f,
            25000,
            12,
            "Avocat spécialisé en droit du travail et en droit civil."
        });

        utilisateurs.add(new Object[]{
            "Njoya",
            "Amina",
            "690100002",
            "amina.njoya.avocat@lexora-test.cm",
            "#D4AF37",
            "Littoral",
            "Douala",
            "AVOCAT",
            Set.of(
            Specialite.DROIT_DES_AFFAIRES,
            Specialite.DROIT_OHADA
            ),
            4.5f,
            30000,
            9,
            "Avocate spécialisée en droit des affaires et commercial."
        });

        utilisateurs.add(new Object[]{
            "Essomba",
            "Patrick",
            "690100003",
            "patrick.essomba.avocat@lexora-test.cm",
            "#2563EB",
            "Centre",
            "Yaoundé",
            "AVOCAT",
            Set.of(
            Specialite.DROIT_PENAL,
            Specialite.DROIT_CIVIL
            ),
            4.8f,
            35000,
            15,
            "Avocat intervenant en droit pénal et en droit civil."
        });

        utilisateurs.add(new Object[]{
            "Kamga",
            "Vanessa",
            "690100004",
            "vanessa.kamga.avocat@lexora-test.cm",
            "#7C3AED",
            "Ouest",
            "Bafoussam",
            "AVOCAT",
            Set.of(
            Specialite.DROIT_DE_LA_FAMILLE,
            Specialite.DROITS_HUMAINS
            ),
            4.6f,
            20000,
            8,
            "Avocate spécialisée en droit de la famille et des personnes."
        });

        utilisateurs.add(new Object[]{
            "Abanda",
            "Samuel",
            "690100005",
            "samuel.abanda.avocat@lexora-test.cm",
            "#059669",
            "Centre",
            "Yaoundé",
            "AVOCAT",
            Set.of(
            Specialite.DROIT_FONCIER_ET_IMMOBILIER,
            Specialite.DROIT_ADMINISTRATIF
            ),
            4.4f,
            30000,
            10,
            "Avocat spécialisé en droit foncier, immobilier et contractuel."
        });

        utilisateurs.add(new Object[]{
            "Ngono",
            "Carine",
            "690100006",
            "carine.ngono.avocat@lexora-test.cm",
            "#DB2777",
            "Sud",
            "Ebolowa",
            "AVOCAT",
            Set.of(
            Specialite.DROIT_ADMINISTRATIF,
            Specialite.DROIT_FISCAL_ET_DOUANIER
            ),
            4.3f,
            25000,
            7,
            "Avocate spécialisée en droit administratif et fiscal."
        });

        utilisateurs.add(new Object[]{
            "Fokou",
            "Brice",
            "690100007",
            "brice.fokou.avocat@lexora-test.cm",
            "#EA580C",
            "Littoral",
            "Douala",
            "AVOCAT",
            Set.of(
            Specialite.DROIT_BANCAIRE_ET_FINANCIER,
            Specialite.DROIT_DES_ASSURANCES
            ),
            4.2f,
            40000,
            11,
            "Avocat spécialisé en droit bancaire et des assurances."
        });

        utilisateurs.add(new Object[]{
            "Tchoumi",
            "Grâce",
            "690100008",
            "grace.tchoumi.avocat@lexora-test.cm",
            "#0891B2",
            "Centre",
            "Yaoundé",
            "AVOCAT",
            Set.of(
            Specialite.DROIT_DU_NUMERIQUE,
            Specialite.PROPRIETE_INTELLECTUELLE
            ),
            4.9f,
            45000,
            6,
            "Avocate spécialisée en droit du numérique et en propriété intellectuelle."
        });

        utilisateurs.add(new Object[]{
            "Nana",
            "Yannick",
            "690100009",
            "yannick.nana.avocat@lexora-test.cm",
            "#4F46E5",
            "Est",
            "Bertoua",
            "AVOCAT",
            Set.of(
            Specialite.DROIT_DE_L_ENVIRONNEMENT,
            Specialite.DROIT_ADMINISTRATIF
            ),
            4.1f,
            20000,
            5,
            "Avocat spécialisé en droit de l'environnement et administratif."
        });

        utilisateurs.add(new Object[]{
            "Mbarga",
            "Estelle",
            "690100010",
            "estelle.mbarga.avocat@lexora-test.cm",
            "#BE123C",
            "Nord",
            "Garoua",
            "AVOCAT",
            Set.of(
            Specialite.DROIT_INTERNATIONAL,
            Specialite.DROIT_DES_AFFAIRES
            ),
            4.6f,
            35000,
            13,
            "Avocate spécialisée en droit international des affaires."
        });

        /*
     * =====================================
     * 15 CLIENTS
     * =====================================
         */
        utilisateurs.add(new Object[]{
            "Fobissie",
            "Kurtis",
            "690200001",
            "kurtis.fobissie@lexora-test.cm",
            "#1E3A8A",
            "Centre",
            "Yaoundé",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Manga",
            "Junior",
            "690200002",
            "junior.manga@lexora-test.cm",
            "#D4AF37",
            "Littoral",
            "Douala",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Atangana",
            "Marie",
            "690200003",
            "marie.atangana@lexora-test.cm",
            "#2563EB",
            "Centre",
            "Yaoundé",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Fotso",
            "Alain",
            "690200004",
            "alain.fotso@lexora-test.cm",
            "#7C3AED",
            "Ouest",
            "Bafoussam",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Biya",
            "Sandra",
            "690200005",
            "sandra.biya@lexora-test.cm",
            "#059669",
            "Sud",
            "Ebolowa",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Etoa",
            "Kevin",
            "690200006",
            "kevin.etoa@lexora-test.cm",
            "#DB2777",
            "Centre",
            "Yaoundé",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Mbeki",
            "Nathalie",
            "690200007",
            "nathalie.mbeki@lexora-test.cm",
            "#EA580C",
            "Est",
            "Bertoua",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Owona",
            "David",
            "690200008",
            "david.owona@lexora-test.cm",
            "#0891B2",
            "Centre",
            "Obala",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Talla",
            "Christelle",
            "690200009",
            "christelle.talla@lexora-test.cm",
            "#4F46E5",
            "Ouest",
            "Dschang",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Biloa",
            "Steve",
            "690200010",
            "steve.biloa@lexora-test.cm",
            "#BE123C",
            "Littoral",
            "Douala",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Messi",
            "Audrey",
            "690200011",
            "audrey.messi@lexora-test.cm",
            "#0369A1",
            "Centre",
            "Yaoundé",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Ndom",
            "Franck",
            "690200012",
            "franck.ndom@lexora-test.cm",
            "#15803D",
            "Nord",
            "Garoua",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Mouna",
            "Prisca",
            "690200013",
            "prisca.mouna@lexora-test.cm",
            "#A21CAF",
            "Adamaoua",
            "Ngaoundéré",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Zogo",
            "Lionel",
            "690200014",
            "lionel.zogo@lexora-test.cm",
            "#C2410C",
            "Centre",
            "Yaoundé",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        utilisateurs.add(new Object[]{
            "Kouam",
            "Diane",
            "690200015",
            "diane.kouam@lexora-test.cm",
            "#4338CA",
            "Ouest",
            "Bafoussam",
            "CLIENT",
            Set.<Specialite>of(),
            null,
            null,
            null,
            null
        });

        /*
     * Un seul encodage BCrypt pour les 25 comptes.
         */
        String passwordEncode
                = encoder.encode("Admin1@");

        int utilisateursCrees = 0;
        int utilisateursIgnores = 0;

        /*
     * =====================================
     * CRÉATION ET ENREGISTREMENT
     * =====================================
         */
        for (Object[] information : utilisateurs) {

            String email
                    = (String) information[3];


            User user = new User();

            user.setNom(
                    (String) information[0]
            );

            user.setPrenom(
                    (String) information[1]
            );

            user.setNumeroTel(
                    (String) information[2]
            );

            user.setEmail(email);

            user.setPassword(passwordEncode);

            user.setColor(
                    (String) information[4]
            );

            user.setRegion(
                    (String) information[5]
            );

            user.setVille(
                    (String) information[6]
            );

            user.setTypeUtilisateur(
                    (String) information[7]
            );

            @SuppressWarnings("unchecked")
            Set<Specialite> specialites
                    = (Set<Specialite>) information[8];

            user.setSpecialites(
                    new HashSet<>(specialites)
            );

            user.setNote(
                    (Float) information[9]
            );

            user.setHonoraire(
                    (Integer) information[10]
            );

            user.setAnnee(
                    (Integer) information[11]
            );

            user.setDescription(
                    (String) information[12]
            );

            /*
         * Tous les comptes sont déjà vérifiés.
             */
            user.setWantBeVerified(false);
            user.setIsVerified(true);

            /*
         * posts et cabinets restent vides.
         * createdAt sera rempli automatiquement
         * par @CreatedDate si l'audit JPA est activé.
             */
            repo.save(user);
            utilisateursCrees++;
        }

        return ResponseEntity.ok(
                utilisateursCrees
                + " utilisateur(s) créé(s), "
                + utilisateursIgnores
                + " utilisateur(s) déjà existant(s)."
        );
    }
}

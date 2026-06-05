package com.example.lexora.client;


import java.time.Instant;
import java.util.List;
import java.util.Random;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Miguel
 */
@Service
class ClientService {

    private ClientRepository repo;

    BCryptPasswordEncoder encoder;

    public ClientService(ClientRepository repo, BCryptPasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public String registerClient(ClientDTO userNew) {
        List<String> couleurs = List.of("blue", "red", "gray", "yellow", "orange", "violet");
        Random random = new Random();
        int choice = random.nextInt(couleurs.size());

        String passwHash = encoder.encode(userNew.getPassword());

        Client user = new Client();

        user.setCreatedAt(Instant.EPOCH);
        user.setEmail(userNew.getEmail());
        user.setNom(userNew.getNom());
        user.setPrenom(userNew.getPrenom());
        user.setRole(userNew.getRole());
        user.setPassword(passwHash);
        user.setColor(couleurs.get(choice));

        repo.save(user);

        return "Bienvenue " + userNew.getPrenom();
    }
}

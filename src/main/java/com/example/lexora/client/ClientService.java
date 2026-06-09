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

    public String registerClient(ClientDTO clientNew) {
        List<String> couleurs = List.of("blue", "red", "gray", "yellow", "orange", "violet");
        Random random = new Random();
        int choice = random.nextInt(couleurs.size());

        String passwHash = encoder.encode(clientNew.getPassword());

        Client client = new Client();

        client.setCreatedAt(Instant.EPOCH);
        client.setEmail(clientNew.getEmail());
        client.setNom(clientNew.getNom());
        client.setPrenom(clientNew.getPrenom());
        client.setPassword(passwHash);
        client.setColor(couleurs.get(choice));
        client.setRegion(clientNew.getRegion());
        client.setVille(clientNew.getVille());

        repo.save(client);

        return "Bienvenue " + clientNew.getPrenom();
    }
}

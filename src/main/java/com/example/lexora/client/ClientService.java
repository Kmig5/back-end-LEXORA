package com.example.lexora.client;

import java.util.List;
import java.util.Optional;
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

    public Client registerClient(ClientDTO clientNew) {
        List<String> couleurs = List.of("blue", "red", "gray", "yellow", "orange", "violet");
        Random random = new Random();
        int choice = random.nextInt(couleurs.size());

        String passwHash = encoder.encode(clientNew.getPassword());

        Client client = new Client();

        client.setEmail(clientNew.getEmail());
        client.setNom(clientNew.getNom());
        client.setPrenom(clientNew.getPrenom());
        client.setPassword(passwHash);
        client.setColor(couleurs.get(choice));
        client.setRegion(clientNew.getRegion());
        client.setVille(clientNew.getVille());
        client.setNumeroTel(clientNew.getNumeroTel());

        repo.save(client);

        return retournerUserInscrit(client);
    }
    
    private Client retournerUserInscrit(Client clt){
        clt.setPassword(null);
        return clt;
    }

    public String modifier(ClientDTO clientNew) {
        Client client = new Client();

        Optional<Client> clientBD = repo.findById(clientNew.getId());

        if (clientBD.isPresent()) {
            client = clientBD.get();

            client.setId(clientNew.getId());
            client.setEmail(clientNew.getEmail());
            client.setNom(clientNew.getNom());
            client.setPrenom(clientNew.getPrenom());
            client.setRegion(clientNew.getRegion());
            client.setVille(clientNew.getVille());
            client.setNumeroTel(clientNew.getNumeroTel());
        }

        repo.save(client);

        return "Modification ajouté avec succès";
    }
    
    public List<Client> getClients(){
        return repo.findAll();
    }
}

package com.example.lexora.avocat;

import com.example.lexora.user.User;
import com.example.lexora.user.UserDTO;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Miguel
 */
@Service
public class AvocatService {

    @Autowired
    private AvocatRepository repo;

    BCryptPasswordEncoder encoder;

    public AvocatService(AvocatRepository repo, BCryptPasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public List<Avocat> getAvocat() {
        List<Avocat> liste = null;
        return liste = repo.findAll();
    }

    public String registerAvocat(AvocatDTO avocatDto) {
        List<String> couleurs = List.of("blue", "red", "gray", "yellow", "orange", "violet");
        Random random = new Random();

        String passwHash = encoder.encode(avocatDto.getPassword());
        int choice = random.nextInt(couleurs.size());

        Avocat avocat = new Avocat();

        avocat.setCreatedAt(Instant.EPOCH);
        avocat.setEmail(avocatDto.getEmail());
        avocat.setNom(avocatDto.getNom());
        avocat.setPrenom(avocatDto.getPrenom());
        avocat.setPassword(passwHash);
        avocat.setColor(couleurs.get(choice));
        avocat.setRegion(avocatDto.getRegion());
        avocat.setVille(avocatDto.getVille());
        avocat.setSpecialite(avocatDto.getSpecialite());

        repo.save(avocat);

        return "Bienvenue " + avocatDto.getPrenom();
    }
}

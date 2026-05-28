package com.example.lexora.avocat;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Miguel
 */

@Service
public class AvocatService {
    @Autowired
    private AvocatRepository repo;
    
    public List<Avocat> getAvocat(){
        List<Avocat> liste = null;
        return liste = repo.findAll();
    }
    
    public String postAvocat(Avocat avocat){
        repo.save(avocat);
        return "Vous avez été enregistré(e) avec succès";
    }
}

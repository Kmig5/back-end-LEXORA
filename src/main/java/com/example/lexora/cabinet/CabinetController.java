package com.example.lexora.cabinet;

import com.example.lexora.cabinet.dto.CabinetDTO;
import com.example.lexora.user.User;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Miguel
 */
@RestController()
@RequestMapping("/lexora/avocat/cabinet")
public class CabinetController {

    private CabinetService service;

    public CabinetController(CabinetService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public void creerCabinet(@RequestBody CabinetDTO dataCabinet) {
        service.createCabinet(dataCabinet);
    }
    
    @GetMapping("/my-cabinet")
    public List<Cabinet> getCabinet(User avocat) {
        return service.getMyCabinet(avocat);
    }    
}

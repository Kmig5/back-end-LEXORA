
package com.example.lexora.cabinet;

import com.example.lexora.cabinet.dto.CabinetDTO;
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
    
    @PostMapping("/createCabinet")
    public void creerCabinet(@RequestBody CabinetDTO dataCabinet ) {
        
    }
}

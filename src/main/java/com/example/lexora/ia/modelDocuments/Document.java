package com.example.lexora.ia.modelDocuments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Miguel
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Document {

    private MultipartFile document;
    private String type;

}

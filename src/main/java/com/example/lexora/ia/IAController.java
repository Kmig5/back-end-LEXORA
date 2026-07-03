package com.example.lexora.ia;

import com.example.lexora.ia.modelDocuments.Document;
import com.example.lexora.ia.modelDocuments.Documents;
import java.io.IOException;
import org.apache.tika.exception.TikaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

@RestController
@RequestMapping("/lexora/ia")
@CrossOrigin(origins = "*")
public class IAController {
    
    private static final String PROMPTSYSTEM =
            """
            Tu es Lexora IA, assistant juridique spécialisé dans le droit camerounais.
                                        
                                        Tu aides les utilisateurs à comprendre leurs droits, obligations et procédures.
                                        
                                        Règles :
                                        - Répondre avec clarté et précision.
                                        - Ne jamais inventer une loi, un article ou une jurisprudence.
                                        - Signaler explicitement les incertitudes.
                                        - Si des informations manquent, poser des questions.
                                        - Ne jamais garantir l'issue d'une procédure.
                                        - Lorsqu'un document est fourni, l'analyser intégralement et identifier :
                                          * nature du document
                                          * obligations
                                          * droits
                                          * délais
                                          * montants
                                          * risques juridiques
                                        - Pour un contrat, fournir : résumé, parties, obligations, droits, clauses importantes, risques et conseils.
                                        - Tu peux toi même aussi déviner qu'il s'agit bien d'un document si le texte est ressemblant à celui-ci
                                        - À la fin de chaque réponse, proposer des précisions complémentaires.
            """;

    @PostMapping("/question")
    public ResponseEntity<String> repondre(
            @RequestParam String question,
            @ModelAttribute Documents documents) throws IOException, TikaException {

        StringBuilder contenu = new StringBuilder();

        if (documents != null && documents.getDocuments() != null && !documents.getDocuments().isEmpty()) {

            int nombre = 1;
            contenu.append("""
                ===========================
                DOCUMENTS FOURNIS
                ===========================
            """);
            for (Document doc : documents.getDocuments()) {
                if ("document".equals(doc.getType())) {
                    contenu.append("voici le contenu du document ")
                            .append(nombre)
                            .append("il s'agit d'un fichier envoyé par l'utilisateur")
                            .append(IAServices.extraireTexteDocument(doc))
                            .append("\n");
                    nombre++;
                } else if ("image".equals(doc.getType())) {
                    // si le document est une image il faut extraire le contenu
                }
            }
        }

        StringBuilder PROMPTUSER = new StringBuilder();
        StringBuilder PROMPTDOCUMENT = new StringBuilder();
        StringBuilder prompt = new StringBuilder();

        PROMPTDOCUMENT.append(contenu);
        PROMPTUSER.append(question);

        try {
            prompt.append("SYSTEM PROMPT \n")
                    .append(PROMPTSYSTEM)
                    .append("USER PROMPT")
                    .append(PROMPTUSER)
                    .append(PROMPTDOCUMENT);
            
            if (documents != null && documents.getDocuments() != null && !documents.getDocuments().isEmpty()) {
                return ResponseEntity.ok(IAServices.callAI(prompt.toString(), "mistral-large-latest"));
            }
            
            System.out.println(prompt.length());

            return ResponseEntity.ok(IAServices.callAI(prompt.toString()));

        } catch (HttpStatusCodeException e) {
            System.err.println("ERREUR HTTP MICROSOFT");
            System.err.println("Code HTTP : " + e.getStatusCode());
            System.err.println("Message renvoyé : " + question);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur d'authentification ou de clé auprès de Microsoft : " + e.getStatusCode());
        } catch (Exception e) {
            System.err.println("CRASH INTERNE JAVA");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur technique interne : " + e.getMessage());
        }
    }

}

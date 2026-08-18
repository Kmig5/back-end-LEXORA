package com.example.lexora.ia;

import com.example.lexora.ia.modelDocuments.Document;
import com.example.lexora.ia.modelDocuments.Documents;
import java.io.IOException;
import org.apache.tika.exception.TikaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lexora/ia")
@CrossOrigin(origins = "*")
public class IAController {

    private static final int TAILLE_MAX_DOCUMENT = 100_000;

    @PostMapping("/question")
    public ResponseEntity<LexoraResponse> repondre(
            @RequestParam String question,
            @RequestParam(required = false, defaultValue = "") String contexte,
            @RequestParam(required = false) String conversationId,
            @ModelAttribute Documents documents)
            throws IOException, TikaException {

        verifierQuestion(question);

        String messageComplet = construireMessage(
                question,
                contexte,
                documents,
                conversationId
        );

        LexoraResponse reponse = IAServices.callAgent(messageComplet, conversationId);

        return ResponseEntity.ok(reponse);
    }

    private String construireMessage(
            String question,
            String contexte,
            Documents documents,
            String conversationId)
            throws IOException, TikaException {

        StringBuilder message = new StringBuilder();

        message.append("QUESTION DE L'UTILISATEUR :\n")
                .append(question.trim())
                .append("\n");

        if ((conversationId == null
                || conversationId.isBlank())
                && contexte != null
                && !contexte.isBlank()) {

            message.append("\n")
                    .append("===========================\n")
                    .append("CONTEXTE CONVERSATIONNEL\n")
                    .append("===========================\n")
                    .append(contexte.trim())
                    .append("\n");
        }

        ajouterDocuments(message, documents);

        return message.toString();
    }

    private void ajouterDocuments(
            StringBuilder message,
            Documents documents)
            throws IOException, TikaException {

        if (documents == null
                || documents.getDocuments() == null
                || documents.getDocuments().isEmpty()) {

            return;
        }

        message.append("\n")
                .append("===========================\n")
                .append("DOCUMENTS FOURNIS\n")
                .append("===========================\n");

        int numeroDocument = 1;

        for (Document document
                : documents.getDocuments()) {

            if (document == null
                    || document.getType() == null) {
                continue;
            }

            if ("document".equalsIgnoreCase(
                    document.getType())) {

                String texteExtrait
                        = IAServices.extraireTexteDocument(
                                document
                        );

                if (texteExtrait == null
                        || texteExtrait.isBlank()) {

                    message.append("\nDOCUMENT ")
                            .append(numeroDocument)
                            .append(" : aucun texte ")
                            .append("n'a pu être extrait.\n");

                    numeroDocument++;
                    continue;
                }

                String texteLimite
                        = limiterTailleDocument(
                                texteExtrait
                        );

                message.append("\n")
                        .append("---------------------------\n")
                        .append("DOCUMENT ")
                        .append(numeroDocument)
                        .append("\n")
                        .append("---------------------------\n")
                        .append("Ce contenu provient d'un ")
                        .append("fichier envoyé par ")
                        .append("l'utilisateur.\n\n")
                        .append(texteLimite)
                        .append("\n");

                numeroDocument++;
            }

            /*
             * Le traitement OCR des images va être ajouté ici.
             *
             * else if ("image".equalsIgnoreCase(
             *         document.getType())) {
             *
             *     String texteImage =
             *             serviceOcr.extraireTexte(document);
             *
             *     message.append(texteImage);
             * }
             */
        }
    }

    private String limiterTailleDocument(
            String texte) {

        if (texte.length() <= TAILLE_MAX_DOCUMENT) {
            return texte;
        }

        return texte.substring(
                0,
                TAILLE_MAX_DOCUMENT
        ) + "\n\n[DOCUMENT TRONQUÉ PAR LE BACKEND]";
    }

    private void verifierQuestion(
            String question) {

        if (question == null
                || question.isBlank()) {

            throw new IllegalArgumentException(
                    "La question ne peut pas être vide."
            );
        }

        if (question.length() > 10_000) {
            throw new IllegalArgumentException(
                    "La question est trop longue."
            );
        }
    }

    @ExceptionHandler(
            IllegalArgumentException.class
    )
    public ResponseEntity<String>
            gererRequeteInvalide(
                    IllegalArgumentException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler(
            MistralApiException.class
    )
    public ResponseEntity<String>
            gererErreurMistral(
                    MistralApiException exception) {

        System.err.println(
                "ERREUR API MISTRAL"
        );

        exception.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(
                        "Impossible d'obtenir une réponse "
                        + "de l'assistant Lexora : "
                        + exception.getMessage()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String>
            gererErreurInterne(
                    Exception exception) {

        System.err.println(
                "ERREUR INTERNE LEXORA"
        );

        exception.printStackTrace();

        return ResponseEntity
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(
                        "Erreur technique interne : "
                        + exception.getMessage()
                );
    }
}

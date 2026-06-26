package com.example.lexora.ia;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

@RestController
@RequestMapping("/lexora/ia")
@CrossOrigin(origins = "*")
public class IAController {

    @PostMapping("/question")
    public ResponseEntity<String> repondre(@RequestParam String question) {
        try {
            String prompt = """
                            Instructions
                            
                            Edit
                            # Objectif
                            Fournir une assistance juridique fiable en se basant sur les lois et r\u00e8glements en vigueur au Cameroun.
                            Tu t'appelles désormais "Lexora IA"
                            
                            ## Directives g\u00e9n\u00e9rales
                            - R\u00e9pondre avec pr\u00e9cision et clart\u00e9 en utilisant un langage juridique compr\u00e9hensible.
                            - Ne pas fournir d\u2019avis juridique contraignant, mais des informations g\u00e9n\u00e9rales et des orientations.
                            - Toujours se r\u00e9f\u00e9rer aux textes l\u00e9gaux camerounais en vigueur.
                            - Maintenir un ton professionnel, neutre et respectueux.
                            - Ne jamais inventer de lois ou d\u2019articles inexistants.
                            - n'envoie pas des (*) et des (#) et des caractères spéciaux qui servent à rien
                            
                            ## Comp\u00e9tences
                            - Connaissance approfondie des codes et lois camerounais (Code civil, Code p\u00e9nal, Code du travail, etc.).
                            - Capacit\u00e9 \u00e0 expliquer des concepts juridiques complexes en termes simples.
                            - Capacit\u00e9 \u00e0 orienter vers les proc\u00e9dures l\u00e9gales appropri\u00e9es.
                            
                            ## Instructions \u00e9tape par \u00e9tape
                            1. Analyser la demande : Identifier le domaine juridique concern\u00e9 (droit civil, p\u00e9nal, travail, commercial, etc.).
                            2. Rechercher la base l\u00e9gale : Utiliser les textes camerounais pertinents pour r\u00e9pondre.
                            3. Fournir une r\u00e9ponse claire : Expliquer la r\u00e8gle de droit applicable et, si n\u00e9cessaire, indiquer les d\u00e9marches \u00e0 suivre.
                            4. Proposer des ressources compl\u00e9mentaires : R\u00e9f\u00e9rencer les textes officiels ou sites gouvernementaux.
                            
                            ## Gestion des erreurs
                            - Si la question est hors du champ juridique camerounais, informer poliment l\u2019utilisateur et sugg\u00e9rer de consulter un avocat local.
                            - Si les informations sont insuffisantes, demander des pr\u00e9cisions avant de r\u00e9pondre.
                            
                            ## Exemples d\u2019interaction
                            - Utilisateur : Quels sont les d\u00e9lais pour contester un licenciement au Cameroun ?
                            - Agent : Selon le Code du travail camerounais, l\u2019action en contestation doit \u00eatre introduite dans un d\u00e9lai de X jours apr\u00e8s la notification. Voulez-vous que je vous indique la proc\u00e9dure d\u00e9taill\u00e9e ?
                            ## Termes non standards
                            - OHADA : Organisation pour l\u2019Harmonisation en Afrique du Droit des Affaires.
                            
                            ## Suivi et cl\u00f4ture
                            - Toujours demander si l\u2019utilisateur souhaite plus de d\u00e9tails ou des exemples pratiques.
                            - Clore la conversation en rappelant que les informations fournies sont \u00e0 titre indicatif et qu\u2019il est conseill\u00e9 de consulter un professionnel pour des cas sp\u00e9cifiques.
                            R\u00e9ponds \u00e0 cette requ\u00eate : """ + question;

            

            return ResponseEntity.ok(IAServices.callAI(prompt));

        } catch (HttpStatusCodeException e) {
            // INTERCEPTE L'ERREUR POUR LIRE LE TEXTE DU SERVEUR SANS PLANTER
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

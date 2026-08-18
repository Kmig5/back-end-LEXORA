package com.example.lexora.ia;

import com.example.lexora.ia.modelDocuments.Document;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IAServices {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String CONVERSATIONS_URL = "https://api.mistral.ai/v1/conversations";
    private static String API_KEY;
    private static String AGENT_ID;

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .callTimeout(150, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    @Value("${api.key}")
    public void setApiKey(String apiKey) {
        IAServices.API_KEY = apiKey;
    }

    @Value("${mistral.agent.id}")
    public void setAgentId(String agentId) {
        IAServices.AGENT_ID = agentId;
    }

    public static LexoraResponse demarrerConversation(String message) {
        verifierConfiguration();
        verifierMessage(message);
        JSONObject bodyJson = new JSONObject();

        bodyJson.put("agent_id", AGENT_ID);
        bodyJson.put("inputs", message);
        bodyJson.put("store", true);
        bodyJson.put("stream", false);

        JSONObject reponseMistral = executerRequete(CONVERSATIONS_URL, bodyJson);

        return convertirReponseMistral(reponseMistral, null);
    }

    public static LexoraResponse continuerConversation(String message, String conversationId) {
        verifierConfiguration();
        verifierMessage(message);

        if (conversationId == null || conversationId.isBlank()) {
            throw new IllegalArgumentException(
                    "Le conversationId est obligatoire pour continuer une conversation."
            );
        }

        JSONObject bodyJson = new JSONObject();

        bodyJson.put("inputs", message);
        bodyJson.put("store", true);
        bodyJson.put("stream", false);

        String url = CONVERSATIONS_URL
                + "/"
                + conversationId.trim();

        JSONObject reponseMistral = executerRequete(url, bodyJson);

        return convertirReponseMistral(reponseMistral, conversationId);
    }

    /**
     * Méthode principale appelée par le contrôleur. Elle démarre ou continue
     * automatiquement la conversation.
     */
    public static LexoraResponse callAgent(
            String message,
            String conversationId) {

        if (conversationId == null || conversationId.isBlank()) {
            return demarrerConversation(message);
        }

        return continuerConversation(message, conversationId);
    }

    private static JSONObject executerRequete(String url, JSONObject bodyJson) {
        RequestBody requestBody = RequestBody.create(bodyJson.toString(), JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            String contenuReponse = response.body() != null ? response.body().string() : "";
            if (!response.isSuccessful()) {
                System.err.println("//////// ERREUR MISTRAL ////////");
                System.err.println("URL : " + url);
                System.err.println("Code HTTP : " + response.code());
                System.err.println("Réponse : " + contenuReponse);
                System.err.println("////////////////////////////////");

                throw new MistralApiException(
                        response.code(),
                        contenuReponse
                );
            }

            if (contenuReponse.isBlank()) {
                throw new MistralApiException(response.code(), " Mistral a retourné une réponse vide."
                );
            }
            return new JSONObject(contenuReponse);

        } catch (IOException exception) {
            throw new MistralApiException(
                    503,
                    "Impossible de communiquer avec le serveur Mistral.",
                    exception
            );
        }
    }

    private static LexoraResponse convertirReponseMistral(JSONObject reponseMistral,
            String ancienConversationId) {

        String conversationId = reponseMistral.optString("conversation_id", ancienConversationId);

        if (conversationId == null || conversationId.isBlank()) {
            throw new MistralApiException(
                    502,
                    "La réponse Mistral ne contient aucun conversation_id."
            );
        }
        JSONArray outputs = reponseMistral.optJSONArray("outputs");
        if (outputs == null || outputs.length() == 0) {
            throw new MistralApiException(
                    502,
                    "La réponse Mistral ne contient aucun output."
            );
        }
        String contenuAssistant = extraireContenuAssistant(outputs);
        JSONObject jsonAssistant = convertirContenuEnJson(contenuAssistant);

        String texteReponse = jsonAssistant.optString("response", "");

        if (texteReponse.isBlank()) {
            throw new MistralApiException(
                    502,
                    "La réponse structurée ne contient pas le champ response."
            );
        }
        JSONObject orientationJson = jsonAssistant.optJSONObject("orientationUpdate");

        if (orientationJson == null) {
            throw new MistralApiException(
                    502,
                    "La réponse structurée ne contient pas orientationUpdate."
            );
        }

        boolean searchLawyers = jsonAssistant.optBoolean("searchLawyers", false);

        LexoraUpdate orientation = convertirOrientation(orientationJson);

        LexoraResponse resultat = new LexoraResponse();
        resultat.setConversationId(conversationId);
        resultat.setResponse(markdownToHtml(texteReponse));
        resultat.setOrientationUpdate(orientation);
        resultat.setSearchLawyers(searchLawyers);

        return resultat;
    }

    private static String extraireContenuAssistant(JSONArray outputs) {
        for (int i = 0; i < outputs.length(); i++) {
            JSONObject output = outputs.optJSONObject(i);
            if (output == null || !output.has("content")) {
                continue;
            }

            Object content = output.opt("content");

            if (content instanceof String) {
                String texte = ((String) content).trim();
                if (!texte.isBlank()) {
                    return texte;
                }
            }

            if (content instanceof JSONArray) {
                JSONArray parties = (JSONArray) content;
                StringBuilder resultat = new StringBuilder();
                for (int j = 0; j < parties.length(); j++) {
                    Object partieObjet = parties.opt(j);
                    if (partieObjet instanceof String) {
                        resultat.append(partieObjet);
                        continue;
                    }

                    if (partieObjet instanceof JSONObject) {
                        JSONObject partie = (JSONObject) partieObjet;
                        if (partie.has("text")) {
                            resultat.append(partie.optString("text", ""));
                        } else if (partie.has("content")) {
                            resultat.append(partie.optString("content", ""));
                        }
                    }
                }

                if (!resultat.toString().isBlank()) {
                    return resultat.toString();
                }
            }

            if (content instanceof JSONObject) {
                return content.toString();
            }
        }

        throw new MistralApiException(
                502,
                "Impossible d'extraire le contenu textuel de la réponse Mistral."
        );
    }

    private static JSONObject convertirContenuEnJson(String contenuAssistant) {
        if (contenuAssistant == null || contenuAssistant.isBlank()) {
            throw new MistralApiException(
                    502,
                    "Le contenu retourné par l'agent est vide."
            );
        }

        String contenuNettoye = contenuAssistant.trim();

        if (contenuNettoye.startsWith("```")) {

            contenuNettoye = contenuNettoye.replaceFirst("^```(?:json)?\\s*", "");
            contenuNettoye = contenuNettoye.replaceFirst("\\s*```$", "");
        }

        try {
            return new JSONObject(contenuNettoye);
        } catch (JSONException exception) {
            throw new MistralApiException(
                    502,
                    "L'agent n'a pas retourné le JSON attendu. Contenu reçu : " + contenuNettoye,
                    exception
            );
        }
    }

    private static LexoraUpdate convertirOrientation(JSONObject json) {
        LexoraUpdate orientation = new LexoraUpdate();

        orientation.setSpecialite(valeurStringNullable(json, "specialite"));
        orientation.setVille(valeurStringNullable(json, "ville"));
        orientation.setConsultationMode(valeurStringNullable(json, "consultationMode"));
        orientation.setLanguage(valeurStringNullable(json, "language"));
        orientation.setBudgetMin(valeurDoubleNullable(json, "budgetMin"));
        orientation.setBudgetMax(valeurDoubleNullable(json, "budgetMax"));
        orientation.setUrgence(valeurStringNullable(json, "urgence"));

        return orientation;
    }

    private static String valeurStringNullable(JSONObject json, String propriete) {
        if (!json.has(propriete) || json.isNull(propriete)) {
            return null;
        }

        String valeur = json.optString(propriete, null);

        if (valeur == null || valeur.isBlank()) {
            return null;
        }

        return valeur;
    }

    private static Double valeurDoubleNullable(JSONObject json, String propriete) {
        if (!json.has(propriete) || json.isNull(propriete)) {
            return null;
        }
        Object valeur = json.opt(propriete);

        if (valeur instanceof Number) {
            return ((Number) valeur).doubleValue();
        }

        try {
            return Double.valueOf(valeur.toString());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private static void verifierConfiguration() {
        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException(
                    "La clé API Mistral n'est pas configurée."
            );
        }

        if (AGENT_ID == null || AGENT_ID.isBlank()) {
            throw new IllegalStateException(
                    "L'identifiant de l'agent Mistral n'est pas configuré."
            );
        }
    }

    private static void verifierMessage(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException(
                    "Le message envoyé à l'agent ne peut pas être vide."
            );
        }
    }

    public static String markdownToHtml(String markdownText) {
        if (markdownText == null || markdownText.isBlank()) {
            return "<p>Pas de réponse disponible.</p>";
        }
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        
        return renderer.render(parser.parse(markdownText));
    }

    public static String extraireTexteDocument(Document doc) throws IOException, TikaException {
        Tika tika = new Tika();
        return tika.parseToString(doc.getDocument().getInputStream());
    }
}

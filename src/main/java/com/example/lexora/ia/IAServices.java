package com.example.lexora.ia;

import com.example.lexora.ia.modelDocuments.Document;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IAServices {

    private static String API_KEY;
    private static final String URL = "https://api.mistral.ai/v1/chat/completions";

    @Value("${api.key}")
    public void setApiKey(String apiKey) {
        IAServices.API_KEY = apiKey;
    }

    public static String callAI(String prompt) {
        OkHttpClient client = new OkHttpClient();

        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);

        JSONArray messages = new JSONArray();
        messages.put(message);

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("model", "mistral-small");
        bodyJson.put("messages", messages);

        RequestBody body = RequestBody.create(
                bodyJson.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "<p>Erreur lors de la communication avec l'API Mistral.</p>";
            }

            String reponseBruteJson = response.body().string();
            
            // 🗺️ Extraction chirurgicale du contenu du texte de l'IA
            JSONObject jsonObjetPrincipal = new JSONObject(reponseBruteJson);
            JSONArray choices = jsonObjetPrincipal.getJSONArray("choices");
            JSONObject premierChoix = choices.getJSONObject(0);
            JSONObject messageIA = premierChoix.getJSONObject("message");
            String texteMarkdownUnique = messageIA.getString("content");

            // ✅ On convertit UNIQUEMENT la réponse textuelle en HTML
            return markdownToHtml(texteMarkdownUnique);

        } catch (IOException e) {
            e.printStackTrace();
            return "<p>Erreur de connexion au serveur.</p>";
        } catch (Exception e) {
            e.printStackTrace();
            return "<p>Erreur lors de l'analyse de la réponse de l'IA.</p>";
        }
    }

    public static String markdownToHtml(String markdownText) {
        if (markdownText == null || markdownText.isEmpty()) {
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

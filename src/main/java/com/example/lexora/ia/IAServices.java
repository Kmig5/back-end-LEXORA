package com.example.lexora.ia;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Miguel
 */
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
            return markdownToHtml(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur API";
        }
    }

    public static String markdownToHtml(String markdownText) {
        if (markdownText == null || markdownText.isEmpty()) {
            return "pas de réponse";
        }
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // Transforme "**Texte**" en "<strong>Texte</strong>" et "# Titre" en "<h1>Titre</h1>"
        return renderer.render(parser.parse(markdownText));
    }
}

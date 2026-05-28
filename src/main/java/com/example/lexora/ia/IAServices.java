
package com.example.lexora.ia;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Miguel
 */
public class IAServices {
    private static final String API_KEY = "HswkxQw32gAuzD1nLx4ML3KOCc955iSu";
    private static final String URL = "https://api.mistral.ai/v1/chat/completions";
    
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
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur API";
        }
    }
}


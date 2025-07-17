package io.github.stardew.mini.client;

import io.github.stardew.mini.Model.Message;
import com.google.gson.Gson;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;

public class ClientRequestSender {

    private static final String BASE_URL = "http://localhost:8080/api/game/";
    private static final Gson gson = new Gson();

    public static Message<?> sendPost(String gameId, String controllerName, String methodName , String type, Map<String, Object> params) {
        try {
            URL url = new URL(BASE_URL + gameId + "/" + controllerName + "/" + methodName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            Message<Map<String, Object>> message = new Message<>(0, "Client Request", params, Message.MessageType.REQUEST);
            message.setType(type);

            String json = gson.toJson(message);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder responseJson = new StringBuilder();
            while (scanner.hasNext()) {
                responseJson.append(scanner.nextLine());
            }

            scanner.close();
            connection.disconnect();

            return gson.fromJson(responseJson.toString(), Message.class);

        } catch (Exception e) {
            e.printStackTrace();
            return new Message<>(500, "Client request failed: " + e.getMessage());
        }
    }
    public static Message<?> sendGet(String gameId, String controllerName, String methodName, Map<String, String> queryParams) {
        try {
            StringBuilder urlBuilder = new StringBuilder(BASE_URL)
                .append(gameId)
                .append("/")
                .append(controllerName)
                .append("/")
                .append(methodName);

            if (queryParams != null && !queryParams.isEmpty()) {
                urlBuilder.append("?");
                for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                    urlBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
                }
                // Remove last '&'
                urlBuilder.setLength(urlBuilder.length() - 1);
            }

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();

            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                StringBuilder responseJson = new StringBuilder();
                while (scanner.hasNext()) {
                    responseJson.append(scanner.nextLine());
                }

                return gson.fromJson(responseJson.toString(), Message.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Message<>(500, "Client GET request failed: " + e.getMessage());
        }
    }

}

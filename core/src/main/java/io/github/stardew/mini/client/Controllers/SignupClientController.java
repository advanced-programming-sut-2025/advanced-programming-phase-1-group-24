package io.github.stardew.mini.client.Controllers;

import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.client.NetworkClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SignupClientController {
    private static final String CONTROLLER = "signup";
    private static final String REGISTER_METHOD = "register";
    private static final String PICK_QUESTION = "pickQuestion";

    private final NetworkClient networkClient;
    private final String gameId = "";

    public SignupClientController(NetworkClient networkClient) {
        this.networkClient = networkClient;
        //this.gameId = gameId;
    }

    public CompletableFuture<Message<?>> register(
        String username,
        String password,
        String confirm,
        String nickname,
        String email,
        String gender
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("confirm", confirm);
        params.put("nickname", nickname);
        params.put("email", email);
        params.put("gender", gender);

        return networkClient.sendPost(
            gameId,
            CONTROLLER,
            REGISTER_METHOD,
            params,
            username
        );
    }

    public CompletableFuture<Message<?>> pickQuestion(
        String question,
        String answer,
        String confirm,
        String username
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("question", question);
        params.put("answer", answer);
        params.put("confirm", confirm);

        return networkClient.sendPost(
            gameId,
            CONTROLLER,
            PICK_QUESTION,
            params,
            username
        );
    }
}

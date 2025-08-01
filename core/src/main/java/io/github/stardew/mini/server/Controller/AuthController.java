package io.github.stardew.mini.server.Controller;

import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.server.ServerApp;
import io.github.stardew.mini.server.security.AuthUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class AuthController {
    /**
     * Throws IllegalArgumentException on bad creds,
     * otherwise returns a signed JWT.
     */
    public String login(String username, String password) {
        User u = ServerApp.getInstance().getUserByUsername(username);
        if (u == null || !u.getPassword().equals(hashSHA256(password))) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return AuthUtil.generateToken(username);
    }

    // io.github.stardew.mini.server.Controller.AuthController

    public Message<String> signup(Map<String,Object> body) {
        // بگیرد username/password/email/nickname/gender از body
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        String email    = (String) body.get("email");
        String nickname = (String) body.get("nickname");
        boolean gender  = "Female".equalsIgnoreCase((String)body.get("gender"));

        ServerApp app = ServerApp.getInstance();

        if (app.userExists(username)) {
            return new Message<>(400, "username already taken", null, Message.MessageType.RESPONSE);
        }

        if (!SignupMenuController.isValidUsername(username)) {
            return new Message<>(400, "invalid username format", null, Message.MessageType.RESPONSE);
        }
        if (!SignupMenuController.isValidEmail(email)) {
            return new Message<>(400, "invalid email format", null, Message.MessageType.RESPONSE);
        }
        if (!SignupMenuController.isStrongPassword(password)) {
            return new Message<>(400, "Pass is not strong", null, Message.MessageType.RESPONSE);
        }


        String hashed = hashSHA256(password);
        User newUser = new User(username, hashed, nickname, email, gender);
        ServerApp.getInstance().addUser(newUser);
        ServerApp.getInstance().saveUsers();
        System.out.println("New user created");

        Message<String> ok = Message.ok("signed up");
        ok.setUsername(username);    // ← این رو اضافه کن
        return ok;

    }




    private String hashSHA256(String input) {
        // copy your existing SHA256 code here
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
                StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    hexString.append(String.format("%02x", b));
                }
                return hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

    }
}

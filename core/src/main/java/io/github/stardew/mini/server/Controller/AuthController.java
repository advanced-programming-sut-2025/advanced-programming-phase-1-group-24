package io.github.stardew.mini.server.Controller;

import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.server.ServerApp;
import io.github.stardew.mini.server.security.AuthUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

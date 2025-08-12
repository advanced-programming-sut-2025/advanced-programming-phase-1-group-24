package io.github.stardew.mini.common.Model.SaveGame;


import io.github.stardew.mini.common.Model.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDatabase {
    private static final GameDatabase instance = new GameDatabase();
    private static final String DB_URL = "jdbc:sqlite:game_saves.db";

    public static void initDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS GameSaves (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    save_name TEXT NOT NULL,
                    data TEXT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;
            stmt.execute(sql);
        }
    }
    public static GameDatabase getInstance() {
        return instance;
    }
    public static void saveGameToDatabase(Game game) throws Exception {
        String saveData = GameSaver.serializeAndCompressGame(game); // Base64 string

        String sql = "INSERT INTO GameSaves (save_name, data) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, game.getNetworkId());
            pstmt.setString(2, saveData);
            pstmt.executeUpdate();
        }
    }

    public  List<String> listSavedGames() throws SQLException {
        List<String> saves = new ArrayList<>();
        String sql = "SELECT save_name FROM GameSaves ORDER BY created_at DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                saves.add(rs.getString("save_name"));
            }
        }
        return saves;
    }

    public static Game loadGameFromDatabase(String saveName) throws Exception {
        String sql = "SELECT data FROM GameSaves WHERE save_name = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, saveName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String base64 = rs.getString("data");
                    byte[] compressedBytes = java.util.Base64.getDecoder().decode(base64);
                    // Assuming you add this method to GameSaver:
                    return GameSaver.loadSingleGameFromCompressedBytes(compressedBytes);
                } else {
                    throw new Exception("No save found with name: " + saveName);
                }
            }
        }
    }
}

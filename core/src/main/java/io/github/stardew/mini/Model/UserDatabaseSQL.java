package io.github.stardew.mini.Model;


import java.sql.*;
import java.util.ArrayList;

public class UserDatabaseSQL {

    private static final String DB_URL = "jdbc:sqlite:data/users.db";

    static {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        username TEXT PRIMARY KEY,
                        password TEXT,
                        nickname TEXT,
                        email TEXT,
                        gender BOOLEAN,
                        played_games INTEGER,
                        max_money_in_games INTEGER,
                        security_question TEXT,
                        security_answer TEXT
                    )
                """);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void saveUsers(ArrayList<User> users) {
        if (users == null || users.isEmpty()) return;

        for (User user : users) {
            saveUser(user);
        }
    }

    public static void saveUser(User user) {
        String sql = """
            INSERT INTO users (username, password, nickname, email, gender, played_games, max_money_in_games, security_question, security_answer)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT(username) DO UPDATE SET
                password=excluded.password,
                nickname=excluded.nickname,
                email=excluded.email,
                gender=excluded.gender,
                played_games=excluded.played_games,
                max_money_in_games=excluded.max_money_in_games,
                security_question=excluded.security_question,
                security_answer=excluded.security_answer
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNickname());
            pstmt.setString(4, user.getEmail());
            pstmt.setBoolean(5, user.isGender());
            pstmt.setInt(6, user.getPlayedGames());
            pstmt.setInt(7, user.getMaxMoneyInGames());
            pstmt.setString(8, user.getSecurityQuestion());
            pstmt.setString(9, user.getSecurityAnswer());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("nickname"),
                    rs.getString("email"),
                    rs.getBoolean("gender")
                );
                user.setPlayedGames(rs.getInt("played_games"));
                user.setMaxMoneyInGames(rs.getInt("max_money_in_games"));
                user.setSecurityQuestion(rs.getString("security_question"));
                user.setSecurityAnswer(rs.getString("security_answer"));

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}

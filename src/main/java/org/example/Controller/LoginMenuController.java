package org.example.Controller;


import com.google.gson.Gson;
import org.example.Model.App;
import org.example.Model.Menus.LoginMenuCommands;
import org.example.Model.Result;
import org.example.Model.User;
import org.example.Model.UserDatabase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenuController implements MenuController {


    public Result register(Matcher matcher) {
        App app = App.getInstance();
        String username = matcher.group("username");
        String password = matcher.group("password");
        String confirm = matcher.group("confirm");
        String nickname = matcher.group("nickname");
        String email = matcher.group("email");
        String genderString = matcher.group("gender");
        boolean gender = genderString.equalsIgnoreCase("male") ? false : true;

        // Check username duplication
        for (User user : app.getUsers()) {
            if (user.getUsername().equals(username)) {
                return new Result(false, "username is already taken! try adding numbers or -");
            }
        }

        // Check username format
        if (!username.matches("^[A-Za-z][A-Za-z0-9-]{3,9}$")) {
            return new Result(false, "username format is invalid!");
        }

        // Check email format
        if (!isValidEmail(email)) {
            return new Result(false, "email format is invalid!");
        }

        // Handle random password
        boolean isRandomPassword = password.equals("random");
        if (!isRandomPassword) {
            if (!password.equals(confirm)) {
                return new Result(false, "password and confirmation do not match!");
            }
            if (!isStrongPassword(password)) {
                return new Result(false, "password is weak! it must contain lowercase, uppercase, digit, and special character, and be at least 8 chars");
            }
        } else {
            // generate a strong random password
            password = generateStrongRandomPassword();
            // show password to user (you may want to prompt confirmation in a real app)
            System.out.println("Generated Password: " + password);
            // Optionally, wait for confirmation here before continuing
        }

        // Hash the password before storing
        String hashedPassword = hashSHA256(password);
        User newUser = new User(username, hashedPassword, nickname, email, gender);

        // Save user
        app.getUsers().add(newUser);
        UserDatabase.saveUsers(app.getUsers());
        app.setLoggedInUser(newUser); // not added yet until question is picked

        // Show security questions
        StringBuilder questionsList = new StringBuilder("choose a security question:\n");
        List<String> questions = app.getSecurityQuestions();
        for (int i = 0; i < questions.size(); i++) {
            questionsList.append((i + 1)).append(". ").append(questions.get(i)).append("\n");
        }

        return new Result(true, questionsList.toString());
    }


    public static String hashSHA256(String input) {
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

    public static boolean isStrongPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()+=\\[\\]{}|\\\\:;\"'<>,.?/].*");
    }

    public static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9._-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$") &&
                !email.contains("..") &&
                !email.matches(".*[!#%^&*()+={}\\[\\]|\\\\:;\"',<>?].*");
    }
    public static String generateStrongRandomPassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*()_+=[]{}|:<>?";
        String all = upper + lower + digits + special;

        SecureRandom rand = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(upper.charAt(rand.nextInt(upper.length())));
        password.append(lower.charAt(rand.nextInt(lower.length())));
        password.append(digits.charAt(rand.nextInt(digits.length())));
        password.append(special.charAt(rand.nextInt(special.length())));

        for (int i = 0; i < 8; i++) {
            password.append(all.charAt(rand.nextInt(all.length())));
        }

        return password.toString();
    }

    public Result login(Matcher matcher) {
        String username = matcher.group("username");
        String enteredPassword = matcher.group("password");
        boolean stayLoggedIn = matcher.group(3) != null;

        App app = App.getInstance();
        List<User> users = app.getUsers();
        User matchedUser = null;

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                matchedUser = user;
                break;
            }
        }

        if (matchedUser == null) {
            return new Result(false, "username does not exist!");
        }

        String hashedInput = hashSHA256(enteredPassword);
        if (!matchedUser.getPassword().equals(hashedInput)) {
            return new Result(false, "incorrect password!");
        }

        // Login successful
        app.setLoggedInUser(matchedUser);
        if (stayLoggedIn) {
            //app.setStayLoggedIn(true);
            saveLoggedInUserToFile(matchedUser); // ✅ save user
        } else {
            //app.setStayLoggedIn(false);
            clearLoggedInUserFile(); // ✅ delete file if user doesn't want to stay logged in
        }


        return new Result(true, "you are now in main menu!");
    }

    public Result pickQuestion(Matcher matcher) {
        App app = App.getInstance();
        User currentUser = app.getLoggedInUser();

        if (currentUser == null) {
            return new Result(false, "no pending user registration found!");
        }

        int questionIndex;
        try {
            questionIndex = Integer.parseInt(matcher.group("questionNumber")) - 1;
        } catch (NumberFormatException e) {
            return new Result(false, "question number must be a valid integer.");
        }

        List<String> questions = app.getSecurityQuestions();
        if (questionIndex < 0 || questionIndex >= questions.size()) {
            return new Result(false, "invalid question number!");
        }

        String answer = matcher.group("answer");
        String confirm = matcher.group("answerConfirm");

        if (!answer.equals(confirm)) {
            return new Result(false, "answer and confirmation do not match!");
        }

        currentUser.setSecurityQuestion(questions.get(questionIndex));
        currentUser.setSecurityAnswer(answer);

        // Finally save the user
        app.getUsers().add(currentUser);
        UserDatabase.saveUsers(app.getUsers());
        app.setLoggedInUser(null); // clear pending user

        return new Result(true, "user registered successfully. you are now in login menu!");
    }
    public static void saveLoggedInUserToFile(User user) {
        File file = new File("data/logged_in_user.json");
        file.getParentFile().mkdirs(); // ensures the 'data' folder exists

        try (Writer writer = new FileWriter(file)) {
            new Gson().toJson(user, writer);
        } catch (IOException e) {
            e.printStackTrace(); // or log the error
        }
    }

    public static void clearLoggedInUserFile() {
        File file = new File("data/logged_in_user.json");
        if (file.exists()) file.delete();
    }

    public Result forgetPassword(Matcher matcher, Scanner scanner) {
         return new Result(true,"salam (:");
    }


//    public Result createUser(String username, String nickname, String password, String confirmPassword, String email, String gender){}
//    public Result pickQuestion(int question, String answer, String confirmAnswer){}
//    public Result login(String username, String password, boolean stayLoggedIn){}
//    public Result forgetPassword(String username, String answer){}

}

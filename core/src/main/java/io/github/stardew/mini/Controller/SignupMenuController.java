package io.github.stardew.mini.Controller;

import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.Model.UserDatabase;
import io.github.stardew.mini.View.SignupMenuView;

import java.util.List;
import java.util.regex.Matcher;

import static io.github.stardew.mini.Controller.LoginMenuController.*;

public class SignupMenuController implements MenuController {
SignupMenuView view;
    public void setView(SignupMenuView view) {
        this.view = view;
    }
    public Result register(Matcher matcher) {
        MainApp app = MainApp.getInstance();
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
        if (!isValidUsername(username)) {
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

}

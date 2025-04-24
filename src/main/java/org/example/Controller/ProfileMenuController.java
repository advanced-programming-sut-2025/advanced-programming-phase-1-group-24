package org.example.Controller;


import org.example.Model.App;
import org.example.Model.Menus.ProfileMenuCommands;
import org.example.Model.Result;
import org.example.Model.User;
import org.example.Model.UserDatabase;

public class ProfileMenuController implements MenuController {

    ProfileMenuCommands command;

    public Result changeUsername(String username) {
        App app = App.getInstance();
        User user = app.getLoggedInUser();

        if (user == null)
            return new Result(false, "please login first!");

        if (user.getUsername().equals(username))
            return new Result(false, "new username is the same as current one!");

        if (!LoginMenuController.isValidUsername(username))
            return new Result(false, "username format is invalid!");

        if (app.getUserByUsername(username) != null)
            return new Result(false, "this username is already taken!");

        user.setUsername(username);
        UserDatabase.saveUsers(app.getUsers());
        return new Result(true, "username changed successfully!");
    }




    public Result changeEmail(String email) {
        App app = App.getInstance();
        User user = app.getLoggedInUser();

        if (user == null)
            return new Result(false, "please login first!");

        if (user.getEmail().equals(email))
            return new Result(false, "new email is the same as current one!");

        if (!LoginMenuController.isValidEmail(email))
            return new Result(false, "email format is invalid!");

        user.setEmail(email);
        UserDatabase.saveUsers(app.getUsers());
        return new Result(true, "email changed successfully!");
    }


    public Result changePassword(String newPassword, String oldPassword) {
        App app = App.getInstance();
        User user = app.getLoggedInUser();

        if (user == null)
            return new Result(false, "please login first!");
        String hashedOldPassword = LoginMenuController.hashSHA256(oldPassword);
        if (!user.getPassword().equals(hashedOldPassword))
            return new Result(false, "current password is incorrect!");

        if (oldPassword.equals(newPassword))
            return new Result(false, "new password is the same as current one!");

        if (!LoginMenuController.isStrongPassword(newPassword)) {
            return new Result(false, "password is weak! it must contain lowercase, uppercase, digit, and special character, and be at least 8 chars");
        }
        String hashedNewPassword = LoginMenuController.hashSHA256(newPassword);
        user.setPassword(hashedNewPassword);
        UserDatabase.saveUsers(app.getUsers());
        return new Result(true, "password changed successfully!");
    }


    public Result changeNickname(String nickname) {
        App app = App.getInstance();
        User user = app.getLoggedInUser();

        if (user == null)
            return new Result(false, "please login first!");

        if (user.getNickname().equals(nickname))
            return new Result(false, "new nickname is the same as current one!");

        user.setNickname(nickname);
        UserDatabase.saveUsers(app.getUsers());
        return new Result(true, "nickname changed successfully!");
    }

    public Result showUserInfo() {
        App app = App.getInstance();
        User user = app.getLoggedInUser();

        if (user == null)
            return new Result(false, "please login first!");

        String info = String.format(
                "username: %s\nemail: %s\nnickname: %s",
                user.getUsername(), user.getEmail(), user.getNickname()
        );

        return new Result(true, info);
    }
//    public Result changeUsername(String newUsername) {
//    }
//
//    public Result changeNickname(String newNickname) {
//    }
//
//    public Result changeEmail(String newEmail) {
//    }
//
//    public Result changePassword(String newPassword, String oldPassword) {
//    }
//
//    public Result showUserInfo() {
//    }
}


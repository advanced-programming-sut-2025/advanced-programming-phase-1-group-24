package org.example.View;


import org.example.Controller.ProfileMenuController;
import org.example.Model.Menus.LoginMenuCommands;
import org.example.Model.Menus.ProfileMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class ProfileMenu implements AppMenu {
    ProfileMenuController controller = new ProfileMenuController();

    public void handleCommand(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = ProfileMenuCommands.USER_INFO.getMatcher(input)) != null) {
            System.out.println(controller.showUserInfo());
        } else if ((matcher = ProfileMenuCommands.CHANGE_USERNAME.getMatcher(input)) != null) {
            System.out.println(controller.changeUsername(matcher.group("username")));
        } else if ((matcher = ProfileMenuCommands.CHANGE_NICKNAME.getMatcher(input)) != null) {
            System.out.println(controller.changeNickname(matcher.group("nickname")));
        } else if ((matcher = ProfileMenuCommands.CHANGE_EMAIL.getMatcher(input)) != null) {
            System.out.println(controller.changeEmail(matcher.group("email")));
        } else if ((matcher = ProfileMenuCommands.CHANGE_PASSWORD.getMatcher(input)) != null) {
            System.out.println(controller.changePassword(matcher.group("newPassword"), matcher.group("oldPassword")));
        } else if ((matcher = ProfileMenuCommands.EXIT.getMatcher(input)) != null) {
            controller.menuExit();
        } else if ((matcher = ProfileMenuCommands.MENU_ENTER.getMatcher(input)) != null) {
            System.out.println(controller.enterMenu(matcher.group("menuName")));
        } else if ((matcher = ProfileMenuCommands.SHOW_MENU.getMatcher(input)) != null) {
            System.out.println(controller.showCurrentMenu());
        } else {
            System.out.println("invalid command");
        }

    }
}

package io.github.stardew.mini.View;


import com.badlogic.gdx.Screen;
import io.github.stardew.mini.Controller.MainMenuController;
import io.github.stardew.mini.Model.Menus.MainMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenuView implements AppMenuView, Screen {
    MainMenuController controller = new MainMenuController();
public MainMenuView(MainMenuController controller) {
    this.controller = controller;
    controller.setView(this);
}

    public void handleCommand(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = MainMenuCommands.LOGOUT.getMatcher(input)) != null) {
            controller.userLogout();
        } else if ((matcher = MainMenuCommands.EXIT.getMatcher(input)) != null) {
            controller.menuExit();
        } else if ((matcher = MainMenuCommands.MENU_ENTER.getMatcher(input)) != null) {
            System.out.println(controller.enterMenu(matcher.group("menuName")));
        } else if ((matcher = MainMenuCommands.SHOW_MENU.getMatcher(input)) != null) {
            System.out.println(controller.showCurrentMenu());
        } else {
            System.out.println("invalid command");
        }


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

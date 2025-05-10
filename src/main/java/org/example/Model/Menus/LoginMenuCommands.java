package org.example.Model.Menus;

import java.util.regex.Matcher;

public enum LoginMenuCommands implements Commands {
    FORGET_PASSWORD("^forget\\s+password\\s+-u\\s+(?<username>\\S+)$"),
    LOGIN("^login\\s+-u\\s+(?<username>\\S+)\\s+-p\\s+(?<password>\\S+)\\s*(?<loginFlag>–stay-logged-in)?$"),
    PICK_QUESTION("^pick\\s+question\\s+-q\\s+(?<questionNumber>.+)\\s+-a\\s+(?<answer>.+)\\s+-c\\s+(?<answerConfirm>.+)$"),
    REGISTER("^register\\s+-u\\s+(?<username>.+)\\s+-p\\s+(?<password>random|\\S+)\\s*(?<confirm>\\S*)\\s+-n\\s+(?<nickname>.+)\\s+-e\\s+(?<email>.+)\\s+-g\\s+(?<gender>.+)$"),
    MENU_ENTER(Commands.MENU_ENTER),
    SHOW_MENU(Commands.SHOW_MENU),
    EXIT(Commands.EXIT);

    private final String regex;

    LoginMenuCommands(String regex) {
        this.regex = regex;
    }

    @Override
    public String getRegex() {
        return regex;
    }

}

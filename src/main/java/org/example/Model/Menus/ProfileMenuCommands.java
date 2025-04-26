package org.example.Model.Menus;

public enum ProfileMenuCommands implements Commands {
    CHANGE_USERNAME("^change\\s+username\\s+-u\\s+(?<username>\\S+)$"),
    CHANGE_NICKNAME("^change\\s+nickname\\s+-u\\s+(?<nickname>\\S+)"),
    CHANGE_EMAIL("^change\\s+email\\s+-e\\s+(?<email>\\S+)$"),
    CHANGE_PASSWORD("^change\\s+password\\s+-p\\s+(?<newPassword>\\S+)\\s+-o\\s+(?<oldPassword>\\S+)$"),
    USER_INFO("^user\\s+info$"),
    MENU_ENTER(Commands.MENU_ENTER),
    SHOW_MENU(Commands.SHOW_MENU),
    EXIT(Commands.EXIT);


    private final String regex;
    ProfileMenuCommands(String regex) {
        this.regex = regex;
    }

    @Override
    public String getRegex() {
        return regex;
    }
}

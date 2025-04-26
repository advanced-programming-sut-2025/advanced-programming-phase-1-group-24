package org.example.Model.Menus;

public enum MainMenuCommands implements Commands {
    LOGOUT("^user\\s+logout$"),
    MENU_ENTER(Commands.MENU_ENTER),
    SHOW_MENU(Commands.SHOW_MENU),
    EXIT(Commands.EXIT);

    private final String regex;
    MainMenuCommands(String regex) {
        this.regex = regex;
    }

    @Override
    public String getRegex() {
        return regex;
    }
}

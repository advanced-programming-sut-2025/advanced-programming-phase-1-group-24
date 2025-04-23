package org.example.Model.Menus;

public enum ProfileMenuCommands implements Commands {
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

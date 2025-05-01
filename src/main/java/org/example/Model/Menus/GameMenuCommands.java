package org.example.Model.Menus;

public enum GameMenuCommands implements Commands {
    CHEAT_UNLIMITED_ENERGY("^energy\\s+unlimited$"),
    CHEAT_ENERGY("^energy\\s+set\\s+-v\\s+(?<value>-?\\d+)$"),
    ENERGY("^energy\\s+show$"),
    CHEAT_WEATHER("^cheat\\s+weather\\s+set\\s+(?<weather>\\S+)$"),
    WEATHER_FORECAST("^weather\\s+forecast$"),
    WEATHER("^weather$"),
    CHEAT_ADVANCE_TIME("^cheat\\s+advance\\s+time\\s+(?<number>-?\\d+)h$"),
    CHEAT_ADVANCE_DATE("^cheat\\s+advance\\s+date\\s+(?<number>-?\\d+)d$"),
    NEXT_TURN("^next\\s+turn$"),
    TERMINATE_GAME("^force\\s+terminate$"),
    CHOOSE_MAP("^game\\s+map\\s+(?<mapNumber>\\d+)$"),
    EXIT_GAME("^exit\\s+game$"),
    LOAD_GAME("^load\\s+game$"),
    NEW_GAME("^game\\s+new\\s+-u\\s+(?<users>.+)$"),
    MENU_ENTER(Commands.MENU_ENTER),
    SHOW_MENU(Commands.SHOW_MENU),
    EXIT(Commands.EXIT);


    private final String regex;

    GameMenuCommands(String regex) {
        this.regex = regex;
    }

    @Override
    public String getRegex() {
        return regex;
    }

}

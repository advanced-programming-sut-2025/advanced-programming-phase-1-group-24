package org.example.Model.Menus;

public enum GameMenuCommands implements Commands {
    CHEAT_ADVANCE_TIME("^cheat\\s+advance\\s+time\\s+(?<number>-?\\d+)h$"),
    CHEAT_ADVANCE_DATE("^cheat\\s+advance\\s+date\\s+(?<number>-?\\d+)d$"),
    NEXT_TURN("^next\\s+turn$"),
    TERMINATE_GAME("^force\\s+terminate$"),
    CHOOSE_MAP("^game\\s+map\\s+(?<mapNumber>\\d+)$"),
    EXIT_GAME("^exit\\s+game$"),
    LOAD_GAME("^load\\s+game$"),
    NEW_GAME("^game\\s+new\\s+-u\\s+(?<users>.+)$"),
    PRINT_GAME("^print\\s+map\\s+-l\\s+(?<x>\\d+),\\s+(?<y>\\d+)\\s+-s\\s+(?<size>\\d+)$"),
    HELP_READ_MAP("help\\s+reading\\s+map"),
    WALK("^walk\\s+-l\\s+(?<x>\\d+),\\s+(?<y>\\d+)$"),
    CAFTINFO("^craftinfo\\s+-n\\s+(?<craftname>.+)$"),
    TREEINFO("^treeinfo\\s+-n\\s+(?<treename>.+)$"),
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

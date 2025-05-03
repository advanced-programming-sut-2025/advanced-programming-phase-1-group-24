package org.example.Model.Menus;

public enum GameMenuCommands implements Commands {
    //inventory commands
    SHOW_INVENTORY("^\\s*inventory\\s+show\\s*$"),
    INVENTORY_TRASH("^\\s*inventory\\s+trash\\s+-i\\s+(?<itemName>.+?)(?:\\s+-n\\s+(?<number>\\d+))?$"),
    EQUIP_TOOL("^\\s*tools\\s+equip\\s+(?<toolName>.+)\\s*$"),
    SHOW_CURRENT_TOOL("^\\s*tools\\s+show\\s+current\\s*$"),
    SHOW_AVAILABLE_TOOLS("^\\s*tools\\s+show\\s+available\\s*$"),
    TOOL_UPGRADE("^\\s*tools\\s+upgrade\\s+(?<toolName>.+)\\s*$"),
    USE_TOOL("^\\s*tools\\s+use\\s+-d\\s+(?<direction>.+)\\s*$"),
    FISH("^\\s*fishing\\s+-p\\s+(?<fishingPole>.+)\\s*$"),
    CHEAT_ADD_ITEM("^\\s*cheat\\s+add\\s+item\\s+-n\\s+(?<itemName>.+)\\s+-c\\s+(?<count>\\d+)\\s*$"),

    SELL_ANIMAL("^sell\\s+animal\\s+-n\\s+(?<name>\\S+)$"),
    COLLECT_PRODUCTS("^collect\\s+produce\\s+-n\\s+(?<name>\\S+)$"),
    SHOW_PRODUCTS("^produces$"),
    FEED_HAY("^feed\\s+hay\\s+-n\\s+(?<name>.+)$"),
    SHEPHERD_ANIMALS("^shepherd\\s+animals\\s+-n\\s+(?<name>\\S+)\\s+-l\\s+<(?<x>\\d+)\\s+,\\s+(?<y>\\d+)>$"),
    ANIMALS_INFO("^animals$"),
    CHEAT_ANIMAL_FRIENDSHIP("^cheat\\s+set\\s+friendship\\s+-n\\s+(?<name>\\S+)\\s+-c\\s+(?<amount>-?\\d+)$"),
    PET("^pet\\s+-n\\s+(?<name>\\S+)$"),
    CHeat_THOR("^cheat\\s+Thor\\s+-l\\s+<(?<x>\\d+),(?<y>\\d+)>$"),
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

package org.example.Model.Menus;

public enum GameMenuCommands implements Commands {
    //////////
    FRIEND_SHIP("^friendships$"),
    SEND_GIFT("^gift\\s+-u\\s+(?<username>\\S+)\\s+-i\\s+(?<item>.+)\\s+-a\\s+(?<amount>\\d+)$"),
    LIST_GIFT("^gift\\s+list$"),
    RATE_GIFTS("^gift\\s+rate\\s+-i\\s+(?<gift>\\d+)\\s+-r\\s+(?<rate>\\d+)$"),
    GIFT_HISTORY("^gift\\s+history\\s+-u\\s+(?<username>\\S+)$"),
    FLOWER_SEND("^flower\\s+-u\\s+(?<username>\\S+)$"),
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
     ///animal commands
    SELL_ANIMAL("^sell\\s+animal\\s+-n\\s+(?<name>\\S+)$"),
    COLLECT_PRODUCTS("^collect\\s+produce\\s+-n\\s+(?<name>\\S+)$"),
    SHOW_PRODUCTS("^produces$"),
    FEED_HAY("^feed\\s+hay\\s+-n\\s+(?<name>.+)$"),
    SHEPHERD_ANIMALS("^shepherd\\s+animals\\s+-n\\s+(?<name>\\S+)\\s+-l\\s+<(?<x>\\d+)\\s+,\\s+(?<y>\\d+)>$"),
    ANIMALS_INFO("^animals$"),
    CHEAT_ANIMAL_FRIENDSHIP("^cheat\\s+set\\s+friendship\\s+-n\\s+(?<name>\\S+)\\s+-c\\s+(?<amount>-?\\d+)$"),
    PET("^pet\\s+-n\\s+(?<name>\\S+)$"),

    CHEAT_ADD_MONEY("^cheat\\s+add\\s+(?<count>\\d+)\\s+dollars$"),
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
    PLANT("^plant\\s+-s\\s+(?<seedName>.+)\\s+-d\\s+(?<direction>.+)$"),
    SHOWPLANT("^showplant\\s+-l\\s+(?<x>\\d+),\\s+(?<y>\\d+)$"),
    FERTALISE("^fertilize\\s+-f\\s+(?<fertilizer>.+)\\s+-d\\s+(?<direction>.+)$"),
    BUILDGREENHOUSE("^build\\s+greenhouse$"),
    TALK("^talk\\s+-u\\s+(?<username>.+)\\s+-m\\s+(?<message>.+)$"),
    SHOW_TALK_HISTORY("^talk\\s+history\\s+-u\\s+(?<username>.+)$"),
    HUG("^hug\\s+-u\\s+(?<username>.+)$"),
    ASK_MARRIAGE("^ask\\s+marriage\\s+-u\\s+(?<username>.+)\\s+-r\\s+(?<ring>.+)$"),
    RESPOND("^respond\\s+(?<response>(accept|reject))\\s+-u\\s+(?<username>.+)$"),
    START_TRADE("start\\s+trade"),
    CHEAT_WALK("cheat\\s+walk\\s+(?<x>\\d+),\\s+(?<y>\\d+)$"),
    CHEAT_SET_SKILL("^set\\s+skill\\s+(?<skill>\\S+)\\s+(?<number>\\d+)$"),
    CHEAT_SET_LEVEL("^set\\s+level\\s+(?<level>\\d+)\\s+-u\\s+(?<username>.+)$"),
    MEET_NPC("^\\s*meet\\s+NPC\\s+(?<npcName>.+)\\s*$"),
    GIFT_NPC("^\\s*gift\\s+NPC\\s+(?<npcName>.+)\\s+-i\\s+(?<item>.+)\\s*$"),
    NPC_FRIENDSHIP_LIST("^\\s*friendship\\s+NPC\\s+list\\s*$"),
    NPC_QUEST_LIST("^\\s*quests\\s+list\\s*$"),
    DO_MISSION("^\\s*quests\\s+finish\\s+-i\\s+(?<index>\\d)\\s*$"),
    PUT_FOOD_IN_FRIDGE("^\\s*cooking\\s+refrigerator\\s+put\\s+(?<item>.+)\\s*$"),
    PICK_FOOD_FROM_FRIDGE("^\\s*cooking\\s+refrigerator\\s+pick\\s+(?<item>.+)\\s*$"),
    SHOW_COOKING_RECIPES("^\\s*cooking\\s+show\\s+recipes\\s*$"),
    COOK("^\\s*cooking\\s+prepare\\s+(?<recipe>.+)\\s*$"),
    EAT("^\\s*eat\\s+(?<food>.+)\\s*$"),
    ARTISAN_USE("^\\s*artisan\\s+use\\s+-a\\s+(?<artisanName>[^-\\s][^-]*?)\\s*(?:-i1\\s+(?<itemName1>[^-\\s][^-]*?))?\\s*(?:-i2\\s+(?<itemName2>[^-\\s][^-]*?))?\\s*$"),
    ARTISAN_GET("^\\s*artisan\\s+get\\s+(?<artisanName>.+)\\s*$"),
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

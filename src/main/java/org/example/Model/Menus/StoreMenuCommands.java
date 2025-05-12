package org.example.Model.Menus;

public enum StoreMenuCommands implements Commands {
    BUILD_HABITAT("^build\\s+-a\\s+(?<name>.+)\\s+-l\\s+<(?<x>\\d+)\\s*,\\s*(?<y>\\d+)>$"),
    BUY_ANIMAL("^buy\\s+animal\\s+-a\\s+(?<animal>\\S+)\\s+-n\\s+(?<name>\\S+)$"),
    PURCHASE("^purchase\\s+(?<product>.+?)(?:\\s+-n\\s+(?<count>\\d+))?$"),
    SHIPPING_BIN("^sell\\s+(?<product>.+?)(?:\\s+-n\\s+(?<count>\\d+))?$"),
    SHOW_ALL_PRODUCTS("^show\\s+all\\s+products$"),
    SHOW_ALL_AVAILABLE_PRODUCTS("^show\\s+all\\s+available\\s+products$"),
    UPGRADE_TOOL("^tools\\s+upgrade\\s+(?<tool>.+)$"),
    ;

    private final String regex;

    StoreMenuCommands(String regex) {
        this.regex = regex;
    }

    @Override
    public String getRegex() {
        return regex;
    }

}

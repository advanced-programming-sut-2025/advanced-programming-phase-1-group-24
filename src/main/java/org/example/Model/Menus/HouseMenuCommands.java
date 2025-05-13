package org.example.Model.Menus;

public enum HouseMenuCommands implements Commands{
    SHOW_RECIPIES("^crafting\\s+show\\s+recipes$"),
    CRAFT("^crafting\\s+craft\\s+(?<itemName>.+)$"),
    PLACE_ITEM("^place\\s+item\\s+-n\\s+(?<itemName>.+)\\s+-d\\s+(?<direction>.+)$");
    private final String regex;

    HouseMenuCommands(String regex) {
        this.regex = regex;
    }

    @Override
    public String getRegex() {
        return regex;
    }
}

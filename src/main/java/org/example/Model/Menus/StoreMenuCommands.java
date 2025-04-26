package org.example.Model.Menus;

public enum StoreMenuCommands implements Commands{
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

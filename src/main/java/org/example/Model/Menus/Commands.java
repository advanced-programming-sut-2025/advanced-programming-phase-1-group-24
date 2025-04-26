package org.example.Model.Menus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Commands {

    ;
    String SHOW_MENU = "^show\\s+current\\s+menu$";
    ;
    String MENU_ENTER = "^menu\\s+enter\\s+(?<menuName>\\S+)$";
    ;
    String EXIT = "^menu\\s+exit$";


    String getRegex();

    default Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(getRegex()).matcher(input);
        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }
}


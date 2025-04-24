package org.example.View;

import org.example.Model.App;
import org.example.Model.Menus.Menu;
import java.util.Scanner;

public class AppView {

    public void run() {
        Scanner scanner = new Scanner(System.in);
        do {
            App.getInstance().getCurrentMenu().checkCommand(scanner);
        } while (App.getInstance().getCurrentMenu() != Menu.ExitMenu);
    }
}

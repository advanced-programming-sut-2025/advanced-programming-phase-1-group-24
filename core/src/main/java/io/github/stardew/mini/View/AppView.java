package io.github.stardew.mini.View;

import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Menus.Menu;
import java.util.Scanner;

public class AppView {

    public void run() {
        Scanner scanner = new Scanner(System.in);
        do {
            MainApp.getInstance().getCurrentMenu().checkCommand(scanner);
        } while (MainApp.getInstance().getCurrentMenu() != Menu.ExitMenu);
    }
}

package io.github.stardew.mini.client.View;

import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.common.Model.Menus.Menu;

import java.util.Scanner;

public class AppView {

    public void run() {
        Scanner scanner = new Scanner(System.in);
        do {
            MainApp.getInstance().getCurrentMenu().checkCommand(scanner);
        } while (MainApp.getInstance().getCurrentMenu() != Menu.ExitMenu);
    }
}

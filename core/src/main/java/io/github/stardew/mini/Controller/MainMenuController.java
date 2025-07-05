package io.github.stardew.mini.Controller;


import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Menus.MainMenuCommands;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.View.MainMenuView;

import static io.github.stardew.mini.Controller.LoginMenuController.clearLoggedInUserFile;

public class MainMenuController implements MenuController {
    private MainMenuView view;

    public void setView(MainMenuView view) {
        this.view = view;
    }

    // Logout and return to login screen
    public void userLogout() {
        clearLoggedInUserFile();
        MainApp app = MainApp.getInstance();
        app.setLoggedInUser(null);
        app.setCurrentMenu(Menu.LoginMenu);
        // switch screen
        app.setScreen(new io.github.stardew.mini.View.LoginMenuView(
            new LoginMenuController(), io.github.stardew.mini.Model.Assets.GameAssetManager.skin));
    }

    // Navigate to Profile Menu
//    public void goToProfileMenu() {
//        MainApp app = MainApp.getInstance();
//        app.setCurrentMenu(Menu.ProfileMenu);
//        app.setScreen(new io.github.stardew.mini.View.ProfileMenuView(
//            new ProfileMenuController(), io.github.stardew.mini.Model.GameAssetManager.skin));
//    }

    // Navigate to Pre-Game Menu
//    public void goToPreGameMenu() {
//        MainApp app = MainApp.getInstance();
//        app.setCurrentMenu(Menu.PreGameMenu);
//        app.setScreen(new io.github.stardew.mini.View.PreGameMenuView(
//            new PreGameMenuController(), io.github.stardew.mini.Model.GameAssetManager.skin));
//    }
}

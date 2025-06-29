package io.github.stardew.mini.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import io.github.stardew.mini.StardewMini;
import io.github.stardew.mini.view.SettingMenu;

public class SettingController {
    private final StardewMini game;
    private final MainController mainController;
    private SettingMenu settingMenu;

    public SettingController(MainController mainController, StardewMini game) {
        this.mainController = mainController;
        this.game = game;
    }

    public void init() {
        settingMenu = new SettingMenu(this);
    }

    public void run() {
        game.setScreen(settingMenu);
    }

    public void goToMain() {
        mainController.run();
        settingMenu.dispose();
    }

    public void saveSettings(float value) {
        mainController.setMusicVolume(value);
    }
}

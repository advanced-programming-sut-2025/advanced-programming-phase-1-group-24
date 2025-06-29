package io.github.stardew.mini.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import io.github.stardew.mini.StardewMini;
import io.github.stardew.mini.view.MainMenu;

public class MainController {
    private final StardewMini game;
    private MainMenu mainMenu;
    private Music backgroundMusic;

    private static final String PREFS_NAME = "StardewMiniSettings";
    private static final String MUSIC_VOLUME_KEY = "volume";
    private static final float DEFAULT_VOLUME = 0.5f;

    public MainController(StardewMini game) {
        this.game = game;
    }


    public void loadMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/theme1.mp3"));

        // Load saved music volume from Preferences
        float savedVolume = loadMusicVolume();
        assert backgroundMusic != null;
        backgroundMusic.setVolume(savedVolume);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    private float loadMusicVolume() {
        return Gdx.app.getPreferences(PREFS_NAME).getFloat(MUSIC_VOLUME_KEY, DEFAULT_VOLUME);
    }

    public void init() {
        loadMusic();
        mainMenu = new MainMenu(this);
    }

    public void run() {
        mainMenu.createUI();
        game.setScreen(mainMenu);
    }

    public void goToGame() {
        GameController gameController = new GameController(game, this);
        gameController.init();
        gameController.run();
        mainMenu.hide();
    }

    public void goToSettings() {
        SettingController settingController = new SettingController(this, game);
        settingController.init();
        settingController.run();
        mainMenu.hide();
    }

    public void exit() {
        backgroundMusic.stop();
        backgroundMusic.dispose();
        game.setScreen(null);
        mainMenu.dispose();
        Gdx.app.exit();
    }

    public void setMusicVolume(float value) {
        backgroundMusic.setVolume(value);
    }
}

package io.github.stardew.mini.Model.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class GameAssetManager {
    private static GameAssetManager gameAssetManager;
    public static Skin skin;
    public static Texture menuBackground;

    public static final int TILE_SIZE = 100;

    public static TextureAtlas playerAtlas;
    public static final ArrayList<Animation<TextureRegion>> playerAnimations = new ArrayList<>();

    // Textures for tiles (load later)
    public static Texture LIGHT_GREEN_FLOOR;
    public static Texture DARK_GREEN_FLOOR;
    public static Texture FLOORING_01;
    public static Texture FLOORING_03;
    public static Texture FLOORING_09;
    public static Texture FLOORING_10;
    public static Texture FLOORING_17;
    public static Texture FLOORING_26;
    public static Texture FLOORING_28;
    public static Texture FLOORING_29;
    public static Texture FLOORING_52;
    public static Texture FLOORING_55;
    public static Texture FLOORING_71;
    public static Texture FLOORING_84;
    public static Texture FLOORING_86;
    public static Texture GREENHOUSE;
    public static TextureRegion greenhouseTexture;


    // Animals initial textures
    public static Texture White_Chicken_Texture;
    public static Texture White_Cow_Texture;
    public static Texture Duck_Texture;
    public static Texture Pig_Texture;
    public static Texture Rabbit_Texture;
    public static Texture Goat_Texture;
    public static Texture Dinosaur_Texture;
    public static Texture Sheep_Texture;

    public static GameAssetManager getGameAssetManager() {
        if (gameAssetManager == null) {
            gameAssetManager = new GameAssetManager();
        }
        return gameAssetManager;
    }

    public static void load() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        menuBackground = new Texture(Gdx.files.internal("menu_bg.png"));

        LIGHT_GREEN_FLOOR = new Texture(Gdx.files.internal("Flooring/Flooring_44.png"));
        DARK_GREEN_FLOOR = new Texture(Gdx.files.internal("Flooring/Flooring_50.png"));
        FLOORING_01 = new Texture(Gdx.files.internal("Flooring/Flooring_01.png"));
        FLOORING_03 = new Texture(Gdx.files.internal("Flooring/Flooring_03.png"));
        FLOORING_09 = new Texture(Gdx.files.internal("Flooring/Flooring_09.png"));
        FLOORING_10 = new Texture(Gdx.files.internal("Flooring/Flooring_10.png"));
        FLOORING_17 = new Texture(Gdx.files.internal("Flooring/Flooring_17.png"));
        FLOORING_26 = new Texture(Gdx.files.internal("Flooring/Flooring_26.png"));
        FLOORING_28 = new Texture(Gdx.files.internal("Flooring/Flooring_28.png"));
        FLOORING_29 = new Texture(Gdx.files.internal("Flooring/Flooring_29.png"));
        FLOORING_52 = new Texture(Gdx.files.internal("Flooring/Flooring_52.png"));
        FLOORING_55 = new Texture(Gdx.files.internal("Flooring/Flooring_55.png"));
        FLOORING_71 = new Texture(Gdx.files.internal("Flooring/Flooring_71.png"));
        FLOORING_84 = new Texture(Gdx.files.internal("Flooring/Flooring_84.png"));
        FLOORING_86 = new Texture(Gdx.files.internal("Flooring/Flooring_86.png"));
        GREENHOUSE = new Texture(Gdx.files.internal("Greenhouse/greenhouse.png"));
        greenhouseTexture = new TextureRegion(new Texture(Gdx.files.internal("Greenhouse/greenhouse.png")));

        playerAtlas = new TextureAtlas(Gdx.files.internal("game/character/sprites_player.atlas"));

        for (int i = 14; i > 9; i--) {
            Array<TextureRegion> walkFrames = new Array<>();
            if (i == 14) {
                for (int j = 0; j < 4; j++) {
                    String region = "player_" + 13 + "_" + 0;
                    walkFrames.add(playerAtlas.findRegion(region));
                }
            } else {
                for (int j = 0; j < 4; j++) {
                    String region = "player_" + i + "_" + j;
                    walkFrames.add(playerAtlas.findRegion(region));
                }
            }
            playerAnimations.add(new Animation<>(0.15f, walkFrames, Animation.PlayMode.LOOP));
        }
        // NEW: Load petting animation (assuming frames are named "player_pet_0_0" to "player_pet_0_3")
        Array<TextureRegion> petFrames = new Array<>();
        for (int j = 0; j < 4; j++) {
            String region = "player_pet_0_" + j;  // Format: "player_pet_[row]_[frame]"
            petFrames.add(playerAtlas.findRegion(region));
        }
        Animation<TextureRegion> petAnim = new Animation<>(0.15f, petFrames, Animation.PlayMode.NORMAL); // PlayMode.NORMAL for one-time playback
        playerAnimations.add(petAnim);  // Add to your animations list

        loadAnimals();

        createCustomStyles();
    }

    private static void createCustomStyles() {
        // Create custom button style if needed
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.newDrawable("button-normal", Color.LIGHT_GRAY);
        buttonStyle.down = skin.newDrawable("button-normal", Color.DARK_GRAY);
        buttonStyle.font = skin.getFont("font");
        buttonStyle.font.getData().setScale(3f);
        skin.add("default", buttonStyle);

        //creating custom font
        BitmapFont customFont = new BitmapFont(Gdx.files.internal("font/myfont.fnt"));
        skin.add("custom-font", customFont);

        // Create custom-Label
        Label.LabelStyle customLabelStyle = new Label.LabelStyle();
        customLabelStyle.font = skin.getFont("custom-font");
        customLabelStyle.fontColor = Color.WHITE;
        skin.add("custom-label", customLabelStyle);

        //create custom text field
        TextField.TextFieldStyle customTextField = new TextField.TextFieldStyle();
        customTextField.font = skin.getFont("custom-font");
        customTextField.fontColor = Color.WHITE;
        skin.add("custom-textField", customTextField);

        //create custom window
        // Get the default style and override the title font
        Window.WindowStyle baseStyle = skin.get(Window.WindowStyle.class);

        // Clone the style to avoid modifying the default globally
        Window.WindowStyle customWindow = new Window.WindowStyle(
            skin.getFont("custom-font"), // your custom font
            baseStyle.titleFontColor,
            baseStyle.background
        );
        customWindow.titleFontColor = Color.GOLD;
        skin.add("custom-window",customWindow);

        // Create another button style with custom font
        TextButton.TextButtonStyle buttonStyle2 = new TextButton.TextButtonStyle();
        buttonStyle2.up = skin.newDrawable("button-normal", Color.LIGHT_GRAY);
        buttonStyle2.down = skin.newDrawable("button-normal", Color.DARK_GRAY);
        buttonStyle2.font = skin.getFont("custom-font"); // Use your custom font
        buttonStyle2.font.getData().setScale(0.9f);
        skin.add("custom-button", buttonStyle2);
    }

    public static void loadAnimals() {
        White_Chicken_Texture = new Texture(Gdx.files.internal("Animals/White_Chicken.png"));
        White_Cow_Texture = new Texture(Gdx.files.internal("Animals/White_Cow.png"));
        Duck_Texture = new Texture(Gdx.files.internal("Animals/Duck.png"));
        Pig_Texture = new Texture(Gdx.files.internal("Animals/Pig.png"));
        Rabbit_Texture = new Texture(Gdx.files.internal("Animals/Rabbit.png"));
        Goat_Texture = new Texture(Gdx.files.internal("Animals/Goat.png"));
        Dinosaur_Texture = new Texture(Gdx.files.internal("Animals/Dinosaur.png"));
        Sheep_Texture = new Texture(Gdx.files.internal("Animals/Sheep.png"));
    }

    public static void dispose() {
        if (skin != null) skin.dispose();
        if (menuBackground != null) menuBackground.dispose();

        if (LIGHT_GREEN_FLOOR != null) LIGHT_GREEN_FLOOR.dispose();
        if (DARK_GREEN_FLOOR != null) DARK_GREEN_FLOOR.dispose();
        if (FLOORING_01 != null) FLOORING_01.dispose();
        if (FLOORING_03 != null) FLOORING_03.dispose();
        if (FLOORING_09 != null) FLOORING_09.dispose();
        if (FLOORING_10 != null) FLOORING_10.dispose();
        if (FLOORING_17 != null) FLOORING_17.dispose();
        if (FLOORING_26 != null) FLOORING_26.dispose();
        if (FLOORING_29 != null) FLOORING_29.dispose();
        if (FLOORING_52 != null) FLOORING_52.dispose();
        if (FLOORING_55 != null) FLOORING_55.dispose();
        if (FLOORING_71 != null) FLOORING_71.dispose();
        if (FLOORING_84 != null) FLOORING_84.dispose();
        if (FLOORING_86 != null) FLOORING_86.dispose();
        if (GREENHOUSE != null) GREENHOUSE.dispose();
    }

    public static Texture getBackground() {
        return menuBackground;
    }
}

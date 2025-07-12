package io.github.stardew.mini.Model.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import io.github.stardew.mini.Model.Avatar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameAssetManager {
    private static GameAssetManager gameAssetManager;
    public static Skin skin;
    public static Texture menuBackground;

    public static final int TILE_SIZE = 100;

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
    public static Texture burntTile;
    public static Texture GREENHOUSE;
    public static TextureRegion greenhouseTexture;
    public static Texture pixel;
    public static Texture snowOverlay;
    public static Texture stormOverlay;
    public static Texture dropTexture;
    public static TextureRegion[] dropFrames = new TextureRegion[11];
    public static Animation<TextureRegion> dropAnimation;
    public static Texture crowSheet;
    public static TextureRegion[] crowFrames;
    public static Animation<TextureRegion> crowAnimation;

    public static Texture CLOCK_ALL;
    public static TextureRegion CLOCK_MAIN;
    public static TextureRegion CLOCK_ARROW;
    public static TextureRegion[] ClOCK_MANNERS = new TextureRegion[12];


    // Animals initial textures
    public static Texture White_Chicken_Texture;
    public static Texture White_Cow_Texture;
    public static Texture Duck_Texture;
    public static Texture Pig_Texture;
    public static Texture Rabbit_Texture;
    public static Texture Goat_Texture;
    public static Texture Dinosaur_Texture;
    public static Texture Sheep_Texture;
    //Habitat Textures
    public static Texture Barn;
    public static Texture Big_Barn;
    public static Texture Deluxe_Barn;
    public static Texture Coop;
    public static Texture Big_Coop;
    public static Texture Deluxe_Coop;
    public static Texture Shipping_Bin;
    public static BitmapFont customFont;
    public static Texture abigailTexture;
    public static TextureRegion[][] abigailFrames;
    public static ArrayList<Animation<TextureRegion>> abigailAnimations = new ArrayList<>();
    public static Texture alexTexture;
    public static TextureRegion[][] alexFrames;
    public static ArrayList<Animation<TextureRegion>> alexAnimations = new ArrayList<>();
    public static Texture haleyTexture;
    public static TextureRegion[][] haleyFrames;
    public static ArrayList<Animation<TextureRegion>> haleyAnimations = new ArrayList<>();
    public static Texture shaneTexture;
    public static TextureRegion[][] shaneFrames;
    public static ArrayList<Animation<TextureRegion>> shaneAnimations = new ArrayList<>();
    public static Texture alexxTexture;
    public static Texture abigaillTexture;
    public static Texture haleyyTexture;
    public static Texture shaneeTexture;

    public static Texture SECRET_HEART ;

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
        burntTile = new Texture(Gdx.files.internal("Flooring/Flooring_46.png"));
        GREENHOUSE = new Texture(Gdx.files.internal("Greenhouse/greenhouse.png"));
        greenhouseTexture = new TextureRegion(new Texture(Gdx.files.internal("Greenhouse/greenhouse.png")));
        snowOverlay = new Texture(Gdx.files.internal("Weather/Snow.png"));
        stormOverlay = new Texture(Gdx.files.internal("Weather/Storm.png"));
        dropTexture = new Texture(Gdx.files.internal("Weather/Rain.png"));
        TextureRegion[][] tmp = TextureRegion.split(dropTexture, dropTexture.getWidth() / 11, dropTexture.getHeight());
        alexxTexture = new Texture(Gdx.files.internal("assets/Villagers/Alex.png"));
        haleyyTexture = new Texture(Gdx.files.internal("assets/Villagers/Haley.png"));
        shaneeTexture = new Texture(Gdx.files.internal("assets/Villagers/Shane.png"));
        abigaillTexture = new Texture(Gdx.files.internal("assets/Villagers/Abigail.png"));
        // Only one column
        System.arraycopy(tmp[0], 0, dropFrames, 0, 11);

        dropAnimation = new Animation<>(0.05f, dropFrames);
        dropAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        crowSheet = new Texture("Birds.png"); // renamed file accordingly
        TextureRegion[][] tmp1 = TextureRegion.split(crowSheet, 16, 16);

        crowFrames = new TextureRegion[2];

        crowFrames[0] = tmp1[4][2];
        crowFrames[1] = tmp1[4][3];

        crowAnimation = new Animation<>(0.1f, crowFrames);

        loadAnimals();

        createCustomStyles();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();

        loadClock();

        loadPlayer();
    }

    private static void loadPlayer() {
        alexTexture = new Texture(Gdx.files.internal("game/character/Alex.png"));
        haleyTexture = new Texture(Gdx.files.internal("game/character/Haley.png"));
        shaneTexture = new Texture(Gdx.files.internal("game/character/Shane.png"));
        abigailTexture = new Texture(Gdx.files.internal("game/character/Abigail.png"));

        alexFrames = TextureRegion.split(alexTexture, 16, 32);
        haleyFrames = TextureRegion.split(haleyTexture, 16, 32);
        shaneFrames = TextureRegion.split(shaneTexture, 16, 32);
        abigailFrames = TextureRegion.split(abigailTexture, 16, 32);

        alexAnimations = generatePlayerAnimations(alexFrames, 0);
        haleyAnimations = generatePlayerAnimations(haleyFrames, 1);
        shaneAnimations = generatePlayerAnimations(shaneFrames, 0);
        abigailAnimations = generatePlayerAnimations(abigailFrames, 0);
    }

    private static void loadClock() {
        CLOCK_ALL = new Texture("Clock/Clock_All.png");
        CLOCK_MAIN = new TextureRegion(CLOCK_ALL, 0, 0, 72, 59);
        CLOCK_ARROW = new TextureRegion(CLOCK_ALL, 72, 0, 8, 18);
        for (int i = 0; i < 12; i++) {
            ClOCK_MANNERS[i] = new TextureRegion(CLOCK_ALL, 80 + i % 4 * 13, i / 4 * 9, 13, 9);
        }
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
        customFont = new BitmapFont(Gdx.files.internal("font/myfont.fnt"));
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
        skin.add("custom-window", customWindow);

        // Create another button style with custom font
        TextButton.TextButtonStyle buttonStyle2 = new TextButton.TextButtonStyle();
        buttonStyle2.up = skin.newDrawable("button-normal", Color.LIGHT_GRAY);
        buttonStyle2.down = skin.newDrawable("button-normal", Color.DARK_GRAY);
        buttonStyle2.font = skin.getFont("custom-font"); // Use your custom font
        buttonStyle2.font.getData().setScale(0.9f);
        skin.add("custom-button", buttonStyle2);


        //Create custom select box
        SelectBox.SelectBoxStyle customStyle = new SelectBox.SelectBoxStyle();
        // Font
        customStyle.font = skin.getFont("custom-font");
        customStyle.fontColor = Color.WHITE;
        // Safe drawable references based on your skin
        customStyle.background = skin.getDrawable("selectBox");
        customStyle.backgroundOpen = skin.getDrawable("selectDown");
        // List style (dropdown appearance)
        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = skin.getFont("custom-font");
        listStyle.fontColorSelected = Color.WHITE;
        listStyle.fontColorUnselected = Color.LIGHT_GRAY;
        listStyle.selection = skin.getDrawable("selection");
        customStyle.listStyle = listStyle;
        // Scroll style (required, even if minimal)
        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.vScroll = skin.getDrawable("scrollVertical");
        scrollStyle.vScrollKnob = skin.getDrawable("scrollKnobVertical");
        customStyle.scrollStyle = scrollStyle;
        // Add to skin
        skin.add("custom-selectbox", customStyle);
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

        Barn = new Texture(Gdx.files.internal("Habitat/Barn.png"));
        Big_Barn = new Texture(Gdx.files.internal("Habitat/Big_Barn.png"));
        Deluxe_Barn = new Texture(Gdx.files.internal("Habitat/Deluxe_Barn.png"));
        Coop = new Texture(Gdx.files.internal("Habitat/Coop.png"));
        Big_Coop = new Texture(Gdx.files.internal("Habitat/Big_Coop.png"));
        Deluxe_Coop = new Texture(Gdx.files.internal("Habitat/Deluxe_Coop.png"));

        Shipping_Bin = new Texture(Gdx.files.internal("Habitat/Shipping_Bin.png"));

        SECRET_HEART =  new Texture("Heart/Secret_Heart.png");

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
        if (burntTile != null) burntTile.dispose();
        if (GREENHOUSE != null) GREENHOUSE.dispose();
        if (snowOverlay != null) snowOverlay.dispose();
        if (stormOverlay != null) stormOverlay.dispose();
        if (dropTexture != null) dropTexture.dispose();
        if (crowSheet != null) crowSheet.dispose();
        if (abigaillTexture != null) abigaillTexture.dispose();
        if (alexxTexture != null) alexxTexture.dispose();
        if(haleyyTexture != null) haleyyTexture.dispose();
        if (shaneeTexture != null) shaneeTexture.dispose();

        disposeAnimals();
    }

    private static void disposeAnimals() {

        if (White_Chicken_Texture != null) White_Chicken_Texture.dispose();
        if (White_Cow_Texture != null) White_Cow_Texture.dispose();
        if (Duck_Texture != null) Duck_Texture.dispose();
        if (Pig_Texture != null) Pig_Texture.dispose();
        if (Rabbit_Texture != null) Rabbit_Texture.dispose();
        if (Goat_Texture != null) Goat_Texture.dispose();
        if (Dinosaur_Texture != null) Dinosaur_Texture.dispose();

        if(Barn != null) Barn.dispose();
        if(Big_Barn != null) Big_Barn.dispose();
        if(Deluxe_Barn != null) Deluxe_Barn.dispose();
        if(Coop != null) Coop.dispose();
        if(Big_Coop != null) Big_Coop.dispose();
        if(Deluxe_Coop != null) Deluxe_Coop.dispose();

        if(SECRET_HEART != null) SECRET_HEART.dispose();
    }

    public static Texture getBackground() {
        return menuBackground;
    }

    public enum Direction {
        DOWN, RIGHT, LEFT, UP
    }

    public static ArrayList<Animation<TextureRegion>> generatePlayerAnimations(TextureRegion[][] miniTextures, int offset) {
        ArrayList<Animation<TextureRegion>> animations = new ArrayList<Animation<TextureRegion>>();

        Map<Direction, Integer> directions = new HashMap<Direction, Integer>();
        directions.put(Direction.LEFT, 1 + offset);
        directions.put(Direction.RIGHT, offset);
        directions.put(Direction.DOWN, 3 + offset);
        directions.put(Direction.UP, 2 + offset);
        for (Direction direction : Direction.values()) {
            int row = directions.get(direction);
            Animation<TextureRegion> animation = new Animation<TextureRegion>(
                0.1f,
                miniTextures[row][1],
                miniTextures[row][2],
                miniTextures[row][3],
                miniTextures[row][0]
            );
            animations.add(animation);
        }

        return animations;
    }
    public static Drawable getAvatarDrawable(Avatar avatar) {
        TextureRegion region;
        switch (avatar) {
            case Abigail:
                region = new TextureRegion(abigaillTexture);
                break;
            case Alex:
                region = new TextureRegion(alexxTexture);
                break;
            case Haley:
                region = new TextureRegion(haleyyTexture);
                break;
            case Shane:
                region = new TextureRegion(shaneeTexture);
                break;
            default:
                region = new TextureRegion(abigaillTexture);
        }
        return new TextureRegionDrawable(region);
    }



}

package io.github.stardew.mini.view.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Assets {
    public static Skin skin;
    public static Texture menuBackground;

    public static void load() {
        // Load skin from file (default libGDX skin)
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Load other assets
        menuBackground = new Texture("menu_bg.png");

        // Create custom button style if needed
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.newDrawable("button-normal", Color.LIGHT_GRAY);
        buttonStyle.down = skin.newDrawable("button-normal", Color.DARK_GRAY);
        buttonStyle.font = skin.getFont("font");
        buttonStyle.font.getData().setScale(3f);
        skin.add("default", buttonStyle);
    }

    public static void dispose() {
        skin.dispose();
        menuBackground.dispose();
    }

    public static Texture getBackground() {
        return menuBackground;
    }
}

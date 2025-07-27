package io.github.stardew.mini.Model.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopAssets {
    private static ShopAssets instance;
    public static Texture Blacksmith;
    public static Texture Jojamart;
    public static Texture Carpenter;
    public static Texture Fish;
    public static Texture Marnie;
    public static Texture PierresGeneral;
    public static Texture StardropSaloon;
    public static ShopAssets getInstance() {
        if (instance == null) instance = new ShopAssets();
        return instance;
    }

    // Map of crop name to list of textures (1 per stage + final)
    public static final Map<String, List<Texture>> shopTextures = new HashMap<>();

    public static void load() {
        Blacksmith = new Texture(Gdx.files.internal("Shop/Blacksmith.png"));
        Carpenter = new Texture(Gdx.files.internal("Shop/Carpenter.png"));
        Fish = new Texture(Gdx.files.internal("Shop/Fish.png"));
        Marnie = new Texture(Gdx.files.internal("Shop/Marnie.png"));
        Jojamart = new Texture(Gdx.files.internal("Shop/Jojamart.png"));
        PierresGeneral = new Texture(Gdx.files.internal("Shop/PierresGeneral.png"));
        StardropSaloon = new Texture(Gdx.files.internal("Shop/StardropSaloon.png"));

    }


    public static void dispose() {
        if (Blacksmith != null) Blacksmith.dispose();
        if (Carpenter != null) Carpenter.dispose();
        if (Fish != null) Fish.dispose();
        if (Marnie != null) Marnie.dispose();
        if (Jojamart != null) Jojamart.dispose();
        if (PierresGeneral != null) PierresGeneral.dispose();
        if (StardropSaloon != null) StardropSaloon.dispose();
    }
}

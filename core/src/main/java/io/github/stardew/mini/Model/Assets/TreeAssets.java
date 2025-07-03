package io.github.stardew.mini.Model.Assets;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class TreeAssets {
    private static TreeAssets instance;

    public static TreeAssets getInstance() {
        if (instance == null) {
            instance = new TreeAssets();
        }
        return instance;
    }

    public static List<Texture> appleTextures = new ArrayList<>();
    public static List<Texture> apricotTextures = new ArrayList<>();
    public static List<Texture> bananaTextures = new ArrayList<>();
    public static List<Texture> cherryTextures = new ArrayList<>();
    public static List<Texture> mahoganyTextures = new ArrayList<>();
    public static List<Texture> mangoTextures = new ArrayList<>();
    public static List<Texture> mapleTextures = new ArrayList<>();
    public static List<Texture> mushroomTreeTextures = new ArrayList<>();
    public static List<Texture> mysticTreeTextures = new ArrayList<>();
    public static List<Texture> oakTextures = new ArrayList<>();
    public static List<Texture> orangeTextures = new ArrayList<>();
    public static List<Texture> peachTextures = new ArrayList<>();
    public static List<Texture> pineTextures = new ArrayList<>();
    public static List<Texture> pomegranateTextures = new ArrayList<>();


    public static void load() {
        appleTextures.add(new Texture("Trees/Apple_Stage_1.png"));
        appleTextures.add(new Texture("Trees/Apple_Stage_2.png"));
        appleTextures.add(new Texture("Trees/Apple_Stage_3.png"));
        appleTextures.add(new Texture("Trees/Apple_Stage_4.png"));
        appleTextures.add(new Texture("Trees/Apple_Stage_5.png"));


        apricotTextures.add(new Texture("Trees/Apricot_Stage_1.png"));
        apricotTextures.add(new Texture("Trees/Apricot_Stage_2.png"));
        apricotTextures.add(new Texture("Trees/Apricot_Stage_3.png"));
        apricotTextures.add(new Texture("Trees/Apricot_Stage_4.png"));
        apricotTextures.add(new Texture("Trees/Apricot_Stage_5.png"));

        bananaTextures.add(new Texture("Trees/Banana_Stage_1.png"));
        bananaTextures.add(new Texture("Trees/Banana_Stage_2.png"));
        bananaTextures.add(new Texture("Trees/Banana_Stage_3.png"));
        bananaTextures.add(new Texture("Trees/Banana_Stage_4.png"));
        bananaTextures.add(new Texture("Trees/Banana_Stage_5.png"));

        cherryTextures.add(new Texture("Trees/Cherry_Stage_1.png"));
        cherryTextures.add(new Texture("Trees/Cherry_Stage_2.png"));
        cherryTextures.add(new Texture("Trees/Cherry_Stage_3.png"));
        cherryTextures.add(new Texture("Trees/Cherry_Stage_4.png"));
        cherryTextures.add(new Texture("Trees/Cherry_Stage_5.png"));

        mahoganyTextures.add(new Texture("Trees/Mahogany_Stage_1.png"));
        mahoganyTextures.add(new Texture("Trees/Mahogany_Stage_2.png"));
        mahoganyTextures.add(new Texture("Trees/Mahogany_Stage_3.png"));
        mahoganyTextures.add(new Texture("Trees/Mahogany_Stage_4.png"));
        mahoganyTextures.add(new Texture("Trees/Mahogany_Stage_5.png"));

        mangoTextures.add(new Texture("Trees/Mango_Stage_1.png"));
        mangoTextures.add(new Texture("Trees/Mango_Stage_2.png"));
        mangoTextures.add(new Texture("Trees/Mango_Stage_3.png"));
        mangoTextures.add(new Texture("Trees/Mango_Stage_4.png"));
        mangoTextures.add(new Texture("Trees/Mango_Stage_5.png"));

        mapleTextures.add(new Texture("Trees/Maple_Stage_1.png"));
        mapleTextures.add(new Texture("Trees/Maple_Stage_2.png"));
        mapleTextures.add(new Texture("Trees/Maple_Stage_3.png"));
        mapleTextures.add(new Texture("Trees/Maple_Stage_4.png"));
        mapleTextures.add(new Texture("Trees/Maple_Stage_5.png"));

        mushroomTreeTextures.add(new Texture("Trees/MushroomTree_Stage_1.png"));
        mushroomTreeTextures.add(new Texture("Trees/MushroomTree_Stage_2.png"));
        mushroomTreeTextures.add(new Texture("Trees/MushroomTree_Stage_3.png"));
        mushroomTreeTextures.add(new Texture("Trees/MushroomTree_Stage_4.png"));
        mushroomTreeTextures.add(new Texture("Trees/MushroomTree_Stage_5.png"));

        mysticTreeTextures.add(new Texture("Trees/Mystic_Tree_Stage_1.png"));
        mysticTreeTextures.add(new Texture("Trees/Mystic_Tree_Stage_2.png"));
        mysticTreeTextures.add(new Texture("Trees/Mystic_Tree_Stage_3.png"));
        mysticTreeTextures.add(new Texture("Trees/Mystic_Tree_Stage_4.png"));
        mysticTreeTextures.add(new Texture("Trees/Mystic_Tree_Stage_5.png"));

        oakTextures.add(new Texture("Trees/Oak_Stage_1.png"));
        oakTextures.add(new Texture("Trees/Oak_Stage_2.png"));
        oakTextures.add(new Texture("Trees/Oak_Stage_3.png"));
        oakTextures.add(new Texture("Trees/Oak_Stage_4.png"));
        oakTextures.add(new Texture("Trees/Oak_Stage_5.png"));

        orangeTextures.add(new Texture("Trees/Orange_Stage_1.png"));
        orangeTextures.add(new Texture("Trees/Orange_Stage_2.png"));
        orangeTextures.add(new Texture("Trees/Orange_Stage_3.png"));
        orangeTextures.add(new Texture("Trees/Orange_Stage_4.png"));
        orangeTextures.add(new Texture("Trees/Orange_Stage_5.png"));

        peachTextures.add(new Texture("Trees/Peach_Stage_1.png"));
        peachTextures.add(new Texture("Trees/Peach_Stage_2.png"));
        peachTextures.add(new Texture("Trees/Peach_Stage_3.png"));
        peachTextures.add(new Texture("Trees/Peach_Stage_4.png"));
        peachTextures.add(new Texture("Trees/Peach_Stage_5.png"));

        pineTextures.add(new Texture("Trees/Pine_Stage_1.png"));
        pineTextures.add(new Texture("Trees/Pine_Stage_2.png"));
        pineTextures.add(new Texture("Trees/Pine_Stage_3.png"));
        pineTextures.add(new Texture("Trees/Pine_Stage_4.png"));
        pineTextures.add(new Texture("Trees/Pine_Stage_5.png"));

        pomegranateTextures.add(new Texture("Trees/Pomegranate_Stage_1.png"));
        pomegranateTextures.add(new Texture("Trees/Pomegranate_Stage_2.png"));
        pomegranateTextures.add(new Texture("Trees/Pomegranate_Stage_3.png"));
        pomegranateTextures.add(new Texture("Trees/Pomegranate_Stage_4.png"));
        pomegranateTextures.add(new Texture("Trees/Pomegranate_Stage_5.png"));
    }

    public static void dispose() {
        for (Texture texture : appleTextures) texture.dispose();
        for (Texture texture : apricotTextures) texture.dispose();
        for (Texture texture : bananaTextures) texture.dispose();
        for (Texture texture : cherryTextures) texture.dispose();
        for (Texture texture : mahoganyTextures) texture.dispose();
        for (Texture texture : mangoTextures) texture.dispose();
        for (Texture texture : mapleTextures) texture.dispose();
        for (Texture texture : mushroomTreeTextures) texture.dispose();
        for (Texture texture : mysticTreeTextures) texture.dispose();
        for (Texture t : oakTextures) t.dispose();
        for (Texture t : orangeTextures) t.dispose();
        for (Texture t : peachTextures) t.dispose();
        for (Texture t : pineTextures) t.dispose();
        for (Texture t : pomegranateTextures) t.dispose();
    }
}

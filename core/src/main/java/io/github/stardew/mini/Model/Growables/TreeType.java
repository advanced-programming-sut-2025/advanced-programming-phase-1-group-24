package io.github.stardew.mini.Model.Growables;

import java.util.*;

import com.badlogic.gdx.graphics.Texture;
import io.github.stardew.mini.Model.Assets.TreeAssets;
import io.github.stardew.mini.Model.TimeManagement.Season;

public enum TreeType {
    ApricotTree("Apricot Tree", SourceType.ApricotSapling, Arrays.asList(7, 7, 7), 28, FruitType.Apricot, false, new ArrayList<>(Arrays.asList(Season.SPRING)), null),
    CherryTree("Cherry Tree", SourceType.CherrySapling, Arrays.asList(7, 7, 7), 28, FruitType.Cherry, false, new ArrayList<>(Arrays.asList(Season.SPRING)), null),
    BananaTree("Banana Tree", SourceType.BananaSapling, Arrays.asList(7, 7, 7), 28, FruitType.Banana, false, new ArrayList<>(Arrays.asList(Season.SUMMER)), null),
    MangoTree("Mango Tree", SourceType.MangoSapling, Arrays.asList(7, 7, 7), 28, FruitType.Mango, false, new ArrayList<>(Arrays.asList(Season.SUMMER)), null),
    OrangeTree("Orange Tree", SourceType.OrangeSapling, Arrays.asList(7, 7, 7), 28, FruitType.Orange, false, new ArrayList<>(Arrays.asList(Season.SUMMER)), null),
    PeachTree("Peach Tree", SourceType.PeachSapling, Arrays.asList(7, 7, 7), 28, FruitType.Peach, false, new ArrayList<>(Arrays.asList(Season.SUMMER)), null),
    AppleTree("Apple Tree", SourceType.AppleSapling, Arrays.asList(7, 7, 7), 28, FruitType.Apple, false, new ArrayList<>(Arrays.asList(Season.AUTUMN)), null),
    PomegranateTree("Pomegranate Tree", SourceType.PomegranateSapling, Arrays.asList(7, 7, 7), 28, FruitType.Pomegranate, false, new ArrayList<>(Arrays.asList(Season.AUTUMN)), null),
    OakTree("Oak Tree", SourceType.Acorns, Arrays.asList(7, 7, 7), 28, FruitType.OakResin, true, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    MapleTree("Maple Tree", SourceType.MapleSeeds, Arrays.asList(7, 7, 7), 28, FruitType.MapleSyrup, true, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    PineTree("Pine Tree", SourceType.PineCones, Arrays.asList(7, 7, 7), 28, FruitType.PineTar, true, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    MahoganyTree("Mahogany Tree", SourceType.MahoganySeeds, Arrays.asList(7, 7, 7), 28, FruitType.Sap, true, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    MushroomTree("Mushroom Tree", SourceType.MushroomTreeSeeds, Arrays.asList(7, 7, 7), 28, FruitType.CommonMushroom, true, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    MysticTree("Mystic Tree", SourceType.MysticTreeSeeds, Arrays.asList(7, 7, 7), 28, FruitType.MysticSyrup, false, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), null);



    private final String name;
    private final SourceType source;
    private final List<Integer> satges;
    private final int totalHarvestTime;
    private final FruitType fruitType;
    private final boolean isForagingTree;
    private final ArrayList<Season> foragingSeasons;
    private final ArrayList<Season> normalSeasons;
    private List<Texture> textures;
    //As soon as the saplings are planted the tree will be in stage one (List[0])
    private Texture fruitedTexture;
    private Texture burnTexture;

    TreeType(String name, SourceType source, List<Integer> stages, int totalHarvestTime,
             FruitType fruitType, boolean isForagingTree, ArrayList<Season> normalSeasons, ArrayList<Season> foragingSeasons){
        this.name = name;
        this.source = source;
        this.satges = stages;
        this.totalHarvestTime = totalHarvestTime;
        this.fruitType = fruitType;
        this.isForagingTree = isForagingTree;
        this.normalSeasons = normalSeasons;
        this.foragingSeasons = foragingSeasons;

    }

    public String getName() {
        return name;
    }
    public SourceType getSource() {
        return source;
    }
    public List<Integer> getSatges() {
        return satges;
    }
    public int getTotalHarvestTime() {
        return totalHarvestTime;
    }
    public FruitType getFruitType() {
        return fruitType;
    }
    public boolean getIsForagingTree() {
        return isForagingTree;
    }
    public ArrayList<Season> getForagingSeasons() {
        return foragingSeasons;
    }
    public ArrayList<Season> getNormalSeasons() {
        return normalSeasons;
    }

    public void initTextures() {
        switch (this) {
            case AppleTree -> {
                textures = TreeAssets.appleTextures;
                fruitedTexture = new Texture("Trees/Apple_Stage_5_Fruit.png");
                burnTexture = new Texture("Trees/AppleTreeLightning.png");
            }
            case OrangeTree -> {
                textures = TreeAssets.orangeTextures;
                fruitedTexture = new Texture("Trees/Orange_Stage_5_Fruit.png");
                burnTexture = new Texture("Trees/OrangeTreeLightning.png");
            }
            case OakTree -> {
                textures = TreeAssets.oakTextures;
            }
            case PeachTree -> {
                textures = TreeAssets.peachTextures;
                fruitedTexture = new Texture("Trees/Peach_Stage_5_Fruit.png");
                burnTexture = new Texture("Trees/PeachTreeLightning.png");
            }
            case MangoTree -> {
                textures = TreeAssets.mangoTextures;
                fruitedTexture = new Texture("Trees/Mango_Stage_5_Fruit.png");
                burnTexture = new Texture("Trees/MangoTreeLightning.png");
            }
            case ApricotTree -> {
                textures = TreeAssets.apricotTextures;
                fruitedTexture = new Texture("Trees/Apricot_Stage_5_Fruit.png");
                burnTexture = new Texture("Trees/ApricotTreeLightning.png");
            }
            case BananaTree -> {
                textures = TreeAssets.bananaTextures;
                fruitedTexture = new Texture("Trees/Banana_Stage_5_Fruit.png");
                burnTexture = new Texture("Trees/BananaTreeLightning.png");
            }
            case CherryTree -> {
                textures = TreeAssets.cherryTextures;
                fruitedTexture = new Texture("Trees/Cherry_Stage_5_Fruit.png");
                burnTexture = new Texture("Trees/CherryTreeLightning.png");
            }
            case MahoganyTree -> {
                textures = TreeAssets.mahoganyTextures;
            }
            case MapleTree -> {
                textures = TreeAssets.mapleTextures;
            }
            case MushroomTree -> {
                textures = TreeAssets.mushroomTreeTextures;
            }
            case MysticTree -> {
                textures = TreeAssets.mysticTreeTextures;
            }
            case PineTree -> {
                textures = TreeAssets.pineTextures;
            }
            case PomegranateTree -> {
                textures = TreeAssets.pomegranateTextures;
                fruitedTexture = new Texture("Trees/Pomegranate_Stage_5.png");
                burnTexture = new Texture("Trees/PomegranateTreeLightning.png");
            }
        }
    }

    public List<Texture> getTextures() {
        return textures;
    }

    public Texture getFruitedTexture() {
        return fruitedTexture;
    }

    public Texture getBurnTexture() {
        return burnTexture;
    }
}

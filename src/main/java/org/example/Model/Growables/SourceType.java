package org.example.Model.Growables;
import org.example.Model.TimeManagement.Season;

import java.util.*;

public enum SourceType {
    JazzSeeds("Jazz Seeds", true, new ArrayList<>(Arrays.asList(Season.SPRING)), new ArrayList<>(Arrays.asList(Season.SPRING))),
    CarrotSeeds("Carrot Seeds", true, new ArrayList<>(Arrays.asList(Season.SPRING)), new ArrayList<>(Arrays.asList(Season.SPRING))),
    CauliflowerSeeds("Cauliflower Seeds", true, new ArrayList<>(Arrays.asList(Season.SPRING)), new ArrayList<>(Arrays.asList(Season.SPRING))),
    CoffeeBean("Coffee Bean", true, new ArrayList<>(Arrays.asList(Season.SPRING)), new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER))),
    GarlicSeeds("Garlic Seeds", true, new ArrayList<>(Arrays.asList(Season.SPRING)), new ArrayList<>(Arrays.asList(Season.SPRING))),
    BeanStarter("Bean Starter", true, new ArrayList<>(Arrays.asList(Season.SPRING)), new ArrayList<>(Arrays.asList(Season.SPRING))),
    KaleSeeds("Kale Seeds", true, new ArrayList<>(Arrays.asList(Season.SPRING)), new ArrayList<>(Arrays.asList(Season.SPRING))),
    ParsnipSeeds("Parsnip Seeds", true, new ArrayList<>(Arrays.asList(Season.SPRING)), new ArrayList<>(Arrays.asList(Season.SPRING))),
    PotatoSeeds("Potato Seeds", true, new ArrayList<>(Arrays.asList(Season.SPRING)), new ArrayList<>(Arrays.asList(Season.SPRING))),
    RhubarbSeeds("Rhubarb Seeds", true, new ArrayList<>(Arrays.asList(Season.SPRING)), new ArrayList<>(Arrays.asList(Season.SPRING))),
    StrawberrySeeds("Strawberry Seeds", true, new ArrayList<>(Arrays.asList(Season.SPRING)), new ArrayList<>(Arrays.asList(Season.SPRING))),
    TulipBulb("Tulip Bulb", true, new ArrayList<>(Arrays.asList(Season.SPRING)), new ArrayList<>(Arrays.asList(Season.SPRING))),
    RiceShoot("Rice Shoot", true, new ArrayList<>(Arrays.asList(Season.SPRING)), new ArrayList<>(Arrays.asList(Season.SPRING))),
    BlueberrySeeds("Blueberry Seeds", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER))),
    CornSeeds("Corn Seeds", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER, Season.AUTUMN))),
    HopsStarter("Hops Starter", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER))),
    PepperSeeds("Pepper Seeds", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER))),
    MelonSeeds("Melon Seeds", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER))),
    PoppySeeds("Poppy Seeds", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER))),
    RadishSeeds("Radish Seeds", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER))),
    RedCabbageSeeds("Red Cabbage Seeds", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER))),
    StarfruitSeeds("Starfruit Seeds", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER))),
    SpangleSeeds("Spangle Seeds", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER))),
    SummerSquashSeeds("Summer Squash Seeds", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER))),
    SunflowerSeeds("Sunflower Seeds", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER, Season.AUTUMN))),
    TomatoSeeds("Tomato Seeds", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER))),
    WheatSeeds("Wheat Seeds", true, new ArrayList<>(Arrays.asList(Season.SUMMER)), new ArrayList<>(Arrays.asList(Season.SUMMER, Season.AUTUMN))),
    AmaranthSeeds("Amaranth Seeds", true, new ArrayList<>(Arrays.asList(Season.AUTUMN)), new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    ArtichokeSeeds("Artichoke Seeds", true, new ArrayList<>(Arrays.asList(Season.AUTUMN)), new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    BeetSeeds("Beet Seeds", true, new ArrayList<>(Arrays.asList(Season.AUTUMN)), new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    BokChoySeeds("Bok Choy Seeds", true, new ArrayList<>(Arrays.asList(Season.AUTUMN)), new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    BroccoliSeeds("Broccoli Seeds", true, new ArrayList<>(Arrays.asList(Season.AUTUMN)), new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    CranberrySeeds("Cranberry Seeds", true, new ArrayList<>(Arrays.asList(Season.AUTUMN)), new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    EggplantSeeds("Eggplant Seeds", true, new ArrayList<>(Arrays.asList(Season.AUTUMN)), new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    FairySeeds("Fairy Seeds", true, new ArrayList<>(Arrays.asList(Season.AUTUMN)), new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    GrapeStarter("Grape Starter", true, new ArrayList<>(Arrays.asList(Season.AUTUMN)), new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    PumpkinSeeds("Pumpkin Seeds", true, new ArrayList<>(Arrays.asList(Season.AUTUMN)), new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    YamSeeds("Yam Seeds", true, new ArrayList<>(Arrays.asList(Season.AUTUMN)), new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    RareSeed("Rare Seed", true, new ArrayList<>(Arrays.asList(Season.AUTUMN)), new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    PowdermelonSeeds("Powdermelon Seeds", true, new ArrayList<>(Arrays.asList(Season.WINTER)), new ArrayList<>(Arrays.asList(Season.WINTER))),
    AncientSeeds("Ancient Seeds", true, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN))),
    ApricotSapling("Apricot Sapling", false, null, new ArrayList<>(Arrays.asList(Season.SPRING))),
    CherrySapling("Cherry Sapling", false, null, new ArrayList<>(Arrays.asList(Season.SPRING))),
    BananaSapling("Banana Sapling", false, null,  new ArrayList<>(Arrays.asList(Season.SUMMER))),
    MangoSapling("Mango Sapling", false, null,  new ArrayList<>(Arrays.asList(Season.SUMMER))),
    OrangeSapling("Orange Sapling", false, null,  new ArrayList<>(Arrays.asList(Season.SUMMER))),
    PeachSapling("Peach Sapling", false, null,  new ArrayList<>(Arrays.asList(Season.SUMMER))),
    AppleSapling("Apple Sapling", false, null, new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    PomegranateSapling("Pomegranate Sapling", false, null, new ArrayList<>(Arrays.asList(Season.AUTUMN))),
    Acorns("Acorns", false, null, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    MapleSeeds("Maple Seeds", false, null, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    PineCones("Pine Cones", false, null, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    MahoganySeeds("Mahogany Seeds", false, null, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    MushroomTreeSeeds("Mushroom Tree Seeds", false, null, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    MysticTreeSeeds("Mystic Tree Seeds", false, null, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)));
    

    private final String name;
    private final boolean isForagingseed;
    private final ArrayList<Season> foragingSeedSeason; // this field will be only filled for foraging seeds
    private final ArrayList<Season> normalSeasons;

    SourceType(String name, boolean isForagingseed, ArrayList<Season> foragingSeedSeason, ArrayList<Season> normSeasons){
        this.name = name;
        this.isForagingseed = isForagingseed;
        this.foragingSeedSeason = foragingSeedSeason;
        this.normalSeasons = normSeasons;
    }

    public String getName() {
        return name;
    }
    public boolean getIsForagingSeed(){
        return isForagingseed;
    }
    public ArrayList<Season> getForagingSeedSeason() {
        return foragingSeedSeason;
    }
    public ArrayList<Season> getNormalSeasons() {
        return normalSeasons;
    }
    public static SourceType fromName(String name) {
        for (SourceType type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}

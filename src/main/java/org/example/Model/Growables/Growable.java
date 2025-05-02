package org.example.Model.Growables;

import org.example.Model.TimeManagement.Season;

import java.util.List;
import java.util.Map;

public class Growable implements Cloneable{
    //When the growable is added to a tile we will fill out the containedGrowable field in the tile
    SourceType source;
    GrowableType growableType;
    int age;
    int currentStage; //It is 1 if it has been planted
    boolean isWateredToday;
    int daysLeftToDie = 2;
    //two boolean is null one is full
    TreeType treeType;
    CropType cropType;
    ForagingCropType foragingCropType;
    //when the crop/tree is ready to harvest if we can only harvest it once, we will change the growableType ,
    //else we will create a copy of this growable and put the growableType as product/plant and we make the age and stage of the initial growable 0, we will add the product to the tile
    //if the product of growable(tree) is coal then create an Item that its type is coal
    //note that if growableType is fruit we can find the fruitType from the filled treeType

    //if we ever add an ArrayList or List to this class we need to do a deep copy
    @Override
    public Growable clone() {
        try {
            return (Growable) super.clone(); // Shallow copy (OK if fields are primitives or enums)
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Should never happen
        }
    }

    public SourceType getSource() {
        return source;
    }
    public GrowableType getGrowableType() {
        return growableType;
    }
    public int getAge() {
        return age;
    }
    public int getCurrentStage() {
        return currentStage;
    }
    public TreeType getTreeType() {
        return treeType;
    }
    public CropType getCropType() {
        return cropType;
    }
    public ForagingCropType getForagingCropType() {
        return foragingCropType;
    }
    public int getDaysLeftToDie() {
        return daysLeftToDie;
    }
    public boolean getIsWateredToday() {
        return isWateredToday;
    }

    public void grow(){}
    public void harvest(){}
    private static final Map<Season, List<SourceType>> mixedSeedsMap = Map.of(
            Season.SPRING, List.of(
                    SourceType.CauliflowerSeeds,
                    SourceType.ParsnipSeeds,
                    SourceType.PotatoSeeds,
                    SourceType.JazzSeeds,
                    SourceType.TulipBulb
            ),
            Season.SUMMER, List.of(
                    SourceType.CornSeeds,
                    SourceType.PepperSeeds,
                    SourceType.RadishSeeds,
                    SourceType.WheatSeeds,
                    SourceType.PoppySeeds,
                    SourceType.SunflowerSeeds,
                    SourceType.SummerSquashSeeds
            ),
            Season.AUTUMN, List.of(
                    SourceType.ArtichokeSeeds,
                    SourceType.CornSeeds,
                    SourceType.EggplantSeeds,
                    SourceType.PumpkinSeeds,
                    SourceType.SunflowerSeeds,
                    SourceType.FairySeeds
            ),
            Season.WINTER, List.of(
                    SourceType.PowdermelonSeeds
            )
    );

    public static List<SourceType> getMixedSeeds(Season season) {
        return mixedSeedsMap.getOrDefault(season, List.of());
    }

    public void setGrowableType(GrowableType growableType) {
        this.growableType = growableType;
    }
}

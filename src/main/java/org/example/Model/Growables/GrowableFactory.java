package org.example.Model.Growables;

import java.util.*;
public class GrowableFactory {
    private static final GrowableFactory instance = new GrowableFactory();
    private final Map<SourceType, Growable> prototypes = new EnumMap<>(SourceType.class);
    private final Map<ForagingCropType, Growable> foragingPrototypes = new EnumMap<>(ForagingCropType.class);

    public GrowableFactory() {
        for (CropType crop : CropType.values()) {
            prototypes.put(crop.getSource(), createPrototypeFromCrop(crop));
        }
        for (TreeType tree : TreeType.values()) {
            prototypes.put(tree.getSource(), createPrototypeFromTree(tree));
        }
        for (ForagingCropType forage : ForagingCropType.values()) {
            foragingPrototypes.put(forage, createPrototypeFromForaging(forage));
        }
    }

    public static GrowableFactory getInstance() {
        return instance;
    }

    private Growable createPrototypeFromCrop(CropType crop) {
        Growable g = new Growable();
        g.source = crop.getSource();
        g.age = 0;
        g.currentStage = 0;
        g.hasBeenFertalized = false;
        g.hasBeenAttackedByCrow = false;
        g.isWateredToday = false;
        g.growableType = GrowableType.Source;
        g.treeType = null;
        g.cropType = crop; //after planting this if the crop is a foraging crop we will change the growableType to that
        g.foragingCropType = null;

        g.setName(crop.getSource().getName());   //I will change the name to the product whenever the product is being built
        g.setSellable(true);
        g.setPrice(crop.getBaseSellPrice());
        g.setPlaceable(false);

        return g;
    }

    private Growable createPrototypeFromTree(TreeType tree) {
        Growable g = new Growable();
        g.source = tree.getSource();
        g.age = 0;
        g.currentStage = 0;
        g.hasBeenFertalized = false;
        g.hasBeenAttackedByCrow = false;
        g.isWateredToday = false;
        g.growableType = GrowableType.Source;
        g.treeType = tree; //after planting this if the crop is a foraging crop we will change the growableType to that
        g.cropType = null;
        g.foragingCropType = null;

        g.setName(tree.getSource().getName());   //I will change the name to the product whenever the product is being built
        g.setSellable(true);
        g.setPrice(tree.getFruitType().getFruitBaseSellPrice());
        g.setPlaceable(false);

        return g;
    }

    private Growable createPrototypeFromForaging(ForagingCropType forage) {
        Growable g = new Growable();
        g.source = null;
        g.age = 0;
        g.currentStage = 0;
        g.growableType = GrowableType.ForagingCrop;
        g.hasBeenFertalized = false;
        g.hasBeenAttackedByCrow = false;
        g.isWateredToday = false;
        g.foragingCropType = forage;
        g.treeType = null;
        g.cropType = null;

        g.setName(forage.getName());
        g.setSellable(true);
        g.setPrice(forage.getBaseSellPrice());
        g.setPlaceable(false);

        return g;
    }

    public Growable create(SourceType source) {
        Growable original = prototypes.get(source);
        if (original == null) {
            throw new IllegalArgumentException("No growable registered for source: " + source);
        }
        return original.copy();
    }

    public Growable create(ForagingCropType forage) {
        Growable prototype = foragingPrototypes.get(forage);
        if (prototype == null) {
            throw new IllegalArgumentException("No Growable registered for forage crop: " + forage);
        }
        return prototype.copy();
    }
}

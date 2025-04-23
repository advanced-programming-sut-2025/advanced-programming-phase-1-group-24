package org.example.Model.Growables;

public class Growable {
    Source source;
    GrowableType growableType;
    int age;
    boolean isReadyToHarvest;
    int currentStage;
    //Tree type
    //Crop type
    //two is null one is full

    public Source getSource() {
        return source;
    }
    public GrowableType getGrowableType() {
        return growableType;
    }
    public int getAge() {
        return age;
    }
    public boolean getIsReadyForHarvest(){
        return isReadyToHarvest;
    }
    public int getCurrentStage() {
        return currentStage;
    }

    public void grow(){}
    public void harvest(){}
}

package org.example.Model.Places;

public class GreenHouse extends Place{
    private boolean isGreenHouseFixed = false;

    public GreenHouse(int startX, int startY, int width, int height){
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
    }

    public boolean getIsGreenHouseFixed(){
        return isGreenHouseFixed;
    }

    public void setGreenHouseFixed(boolean isGreenHouseFixed) {
        this.isGreenHouseFixed = isGreenHouseFixed;
    }



    
}

package org.example.Model.Animals;


import org.example.Model.MapManagement.Tile;
import org.example.Model.Places.Habitat;
import org.example.Model.Things.ProductQuality;

public class Animal {
    private final String name;
    private final AnimalType animalType;
    private Tile currentTile;
    private Habitat livingPlace;
    private int friendship = 0;
    private boolean pettedToday = false;
    private boolean fedToday = false;
    private boolean isInHabitat = true;
    private AnimalProduct product = null;
    private int daysLeftToProduce;

    public Animal(String name, AnimalType animalType) {
        this.name = name;
        this.animalType = animalType;
        this.currentTile = null;
        this.livingPlace = null;
        this.daysLeftToProduce = animalType.getDaysToProduce();

    }

    public void pet() {
        if (!pettedToday) {
            friendship = Math.min(friendship + 15, 1000);
            pettedToday = true;
        }
    }

    public void feed() {
        if (!fedToday) {
            fedToday = true;
            friendship = Math.min(friendship + 8, 1000);
        }
    }

    public void endOfDayUpdate() {
        // in controller update the sleptOutSide
        //if (!isInHabitat()) sleptOutside = true;
        if (!fedToday) friendship -= 20;
        if (!isInHabitat) friendship -= 20;
        if (!pettedToday) friendship -= 10;
        //(200 / Math.max(friendship, 1)); ?????????
        if (friendship <= 0) friendship = 0;

        fedToday = false;
        pettedToday = false;
        //isInHabitat = false;
    }

    public void updateProductEndDay() {
        if (daysLeftToProduce > 0 && product == null) daysLeftToProduce--;
        if (!fedToday || daysLeftToProduce > 0) return;

        AnimalProductType type = animalType.getPrimaryProduct();
        if (animalType.hasSecondaryProduct() && friendship >= 100) {
            double chance = (friendship + 150 * (0.5 + Math.random())) / 1500.0;
            if (Math.random() < chance)
                type = animalType.getSecondaryProduct();
        }

        double quality = (friendship / 1000.0) * (0.5 + 0.5 * Math.random());
        ProductQuality productQuality = ProductQuality.getQualityByValue(quality);
        this.product = new AnimalProduct(productQuality, type);
        if (daysLeftToProduce == 0) daysLeftToProduce = animalType.getDaysToProduce();
    }

    public AnimalProduct getProduct() {
        return product;
    }

    public AnimalProduct collectProduct() {
        AnimalProduct product = this.product;
        this.product = null;
        return product;
    }

    // Getters, Setters, etc...

    public String getName() {
        return name;
    }


    public AnimalType getAnimalType() {
        return animalType;
    }

    public boolean isFedToday() {
        return fedToday;
    }

    public int getFriendship() {
        return friendship;
    }

    public AnimalProduct getProducts() {
        return product;
    }

    public void setDaysLeftToProduce(int days) {
        this.daysLeftToProduce = days;
    }

    public boolean updateIsInHabitat() {
        if (currentTile == null || livingPlace == null) return false;

        int tileX = currentTile.getX();
        int tileY = currentTile.getY();

        int habitatX = livingPlace.getX();
        int habitatY = livingPlace.getY();
        int habitatWidth = livingPlace.getWidth();
        int habitatHeight = livingPlace.getHeight();
        isInHabitat = (tileX >= habitatX && tileX < habitatX + habitatWidth &&
                tileY >= habitatY && tileY < habitatY + habitatHeight);
        return isInHabitat;
    }


    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    public Habitat getLivingPlace() {
        return livingPlace;
    }

    public void setLivingPlace(Habitat livingPlace) {
        this.livingPlace = livingPlace;
    }

    public void setFriendship(int friendship) {
        this.friendship = friendship;
    }

    public boolean isPettedToday() {
        return pettedToday;
    }

    public void setPettedToday(boolean pettedToday) {
        this.pettedToday = pettedToday;
    }

    public void setFedToday(boolean fedToday) {
        this.fedToday = fedToday;
    }

    public boolean isInHabitat() {
        return isInHabitat;
    }

    public void setInHabitat(boolean inHabitat) {
        isInHabitat = inHabitat;
    }

    public int getDaysLeftToProduce() {
        return daysLeftToProduce;
    }

    public void setProduct(AnimalProduct product) {
        this.product = product;
    }

    public Boolean hasProduct() {
        return product != null;
    }
}

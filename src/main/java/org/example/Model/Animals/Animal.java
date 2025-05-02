package org.example.Model.Animals;


import org.example.Model.MapManagement.Tile;
import org.example.Model.Places.Habitat;

import java.util.ArrayList;

public class Animal {
    private final String name;
    private final AnimalType animalType;
    private Tile currentTile;
    private Habitat livingPlace;
    private int friendship = 0;
    private boolean pettedToday = false;
    private boolean fedToday = false;
    private boolean sleptOutside = false;
    private ArrayList<AnimalProduct> products = new ArrayList<>();
    private int daysLeftToProuce;

    public Animal(String name, AnimalType animalType) {
        this.name = name;
        this.animalType = animalType;
        this.currentTile = null;
        this.livingPlace = null;

    }

    public void pet() {
        if (!pettedToday) {
            friendship = Math.min(friendship + 15, 1000);
            pettedToday = true;
        }
    }

    public void feed() {
        fedToday = true;
        friendship = Math.min(friendship + 8, 1000);
    }

    public void endOfDayUpdate() {
        // in controller update the sleptOutSide
        //if (!isInHabitat()) sleptOutside = true;
        if (!fedToday) friendship -= 20;
        if (sleptOutside) friendship -= 20;
        if (!pettedToday) friendship -= 10;
        //(200 / Math.max(friendship, 1)); ?????????
        if (friendship <= 0) friendship = 0;

        fedToday = false;
        pettedToday = false;
        sleptOutside = false;
    }

//    public AnimalProduct produceProduct() {
//        if (!fedToday) return null;
//
//        AnimalProductType type = animalType.getPrimaryProduct();
//        if (animalType.hasSecondaryProduct() && friendship >= 100) {
//            double chance = (friendship + 150 * (0.5 + Math.random())) / 1500.0;
//            if (Math.random() < chance)
//                type = animalType.getSecondaryProduct();
//          }
//
//        double quality = (friendship / 1000.0) * (0.5 + 0.5 * Math.random());
//        AnimalProduct product = new AnimalProduct(type, quality);
//        products.add(product);
//        return product;
//    }

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

    public ArrayList<AnimalProduct> getProducts() {
        return products;
    }

    public void setDaysLeftToProduce(int days) {
        this.daysLeftToProuce = days;
    }

//    private boolean isInHabitat() {
//        if (currentTile == null || livingPlace == null) return false;
//
//        int tileX = getX(currentTile);
//        int tileY = currentTile.getY();
//
//        int habitatX = livingPlace.getX();
//        int habitatY = livingPlace.getY();
//        int habitatWidth = livingPlace.getWidth();
//        int habitatHeight = livingPlace.getHeight();
//
//        return tileX >= habitatX && tileX < habitatX + habitatWidth &&
//                tileY >= habitatY && tileY < habitatY + habitatHeight;
//    }


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

    public boolean isSleptOutside() {
        return sleptOutside;
    }

    public void setSleptOutside(boolean sleptOutside) {
        this.sleptOutside = sleptOutside;
    }

    public void setProducts(ArrayList<AnimalProduct> products) {
        this.products = products;
    }

    public int getDaysLeftToProuce() {
        return daysLeftToProuce;
    }

    public void setDaysLeftToProuce(int daysLeftToProuce) {
        this.daysLeftToProuce = daysLeftToProuce;
    }
}

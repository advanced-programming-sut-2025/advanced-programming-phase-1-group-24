package io.github.stardew.mini.Model.Animals;


import com.badlogic.gdx.math.MathUtils;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.Places.Habitat;
import io.github.stardew.mini.Model.Things.ProductQuality;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
@JsonIdentityInfo(
    generator = ObjectIdGenerators.IntSequenceGenerator.class,
    property = "@id"
)
public class Animal {
    private String name;
    private AnimalType animalType;

    @JsonIgnore
    private Tile currentTile;

    private Habitat livingPlace;
    private int friendship = 0;
    private boolean pettedToday = false;
    private boolean fedToday = false;
    private boolean isInHabitat = true;
    private AnimalProduct product = null;
    private int daysLeftToProduce;

    //new fields added
    private float movementCooldown = 0f;

    @JsonIgnore
    private Queue<Tile> pathToTarget = new LinkedList<>();

    @JsonIgnore
    private Tile movingFrom = null;
    @JsonIgnore
    private Tile movingTo = null;

    private float moveProgress = 0f; // 0.0 -> 1.0
    private float moveSpeed = 2f; // tiles per second

    public Animal(String name, AnimalType animalType) {
        this.name = name;
        this.animalType = animalType;
        this.currentTile = null;
        this.livingPlace = null;
        this.daysLeftToProduce = animalType.getDaysToProduce();
    }

    public Animal() {
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

//    public void endOfDayUpdate() {
//        // in controller update the sleptOutSide
//        //if (!isInHabitat()) sleptOutside = true;
//        if (!fedToday) friendship -= 20;
//        if (!isInHabitat) friendship -= 20;
//        if (!pettedToday) friendship -= 10;
//        //(200 / Math.max(friendship, 1)); ?????????
//        if (friendship <= 0) friendship = 0;
//
//        fedToday = false;
//        pettedToday = false;
//        //isInHabitat = false;
//    }
    public void endOfDayUpdate() {
        if (!fedToday) friendship -= 20;
        if (!isInHabitat) friendship -= 20;
        if (!pettedToday) friendship -= 10;
        if (friendship < 0) friendship = 0;

        fedToday = false;
        pettedToday = false;

        // Reset movement state if animals sleep at night
        pathToTarget.clear();
        movingFrom = null;
        movingTo = null;
        moveProgress = 0f;
        movementCooldown = 0f; // Optional
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

    public boolean hasProduct() {
        return product != null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Animal copy() {
        Animal copy = new Animal(this.name, this.animalType);
        copy.setFriendship(this.friendship);
        copy.setPettedToday(this.pettedToday);
        copy.setFedToday(this.fedToday);
        copy.setInHabitat(this.isInHabitat);
        copy.setDaysLeftToProduce(this.daysLeftToProduce);

        return copy;
    }

    public boolean itMoving() {
        return movingTo != null;
    }

    public void startMove(Tile from, Tile to) {
        this.movingFrom = from;
        this.movingTo = to;
        this.moveProgress = 0f;
    }

    public void updateMovement(float delta) {
        if (movingTo != null) {
            moveProgress += moveSpeed * delta;
            if (moveProgress >= 1f) {
                moveProgress = 0f;
                currentTile.setContainedAnimal(null);
                currentTile = movingTo;
                currentTile.setContainedAnimal(this);
                movingFrom = null;
                movingTo = null;

                if (!pathToTarget.isEmpty()) {
                    startMove(currentTile, pathToTarget.poll());
                }
            }
        }
    }

    public void setPathToTarget(List<Tile> path) {
        this.pathToTarget.clear();
        this.pathToTarget.addAll(path);
        if (!this.pathToTarget.isEmpty()) {
            startMove(currentTile, this.pathToTarget.poll());
        }
    }

    public Tile getMovingFrom() {
        return movingFrom;
    }

    public Tile getMovingTo() {
        return movingTo;
    }

    public float getMoveProgress() {
        return moveProgress;
    }


    public float getMovementCooldown() {
        return movementCooldown;
    }

    public void reduceCooldown(float delta) {
        movementCooldown -= delta;
    }

    public void resetCooldown() {
        movementCooldown = (3f + MathUtils.random(2f));
    }
    public void reloadAfterLoad(Tile tile) {
        this.currentTile = tile;
        this.pathToTarget = new LinkedList<>();
        this.movingFrom = null;
        this.movingTo = null;
        this.movementCooldown = 0f;
        this.moveProgress = 0f;

        if (livingPlace != null && !livingPlace.getLivingAnimals().contains(this)) {
            livingPlace.getLivingAnimals().add(this);
        }
    }

}

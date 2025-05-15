package org.example.Model.Reccepies;

import org.example.Model.App;
import org.example.Model.Places.House;
import org.example.Model.Result;
import org.example.Model.Things.Item;
import org.example.Model.Things.ProductQuality;
import org.example.Model.User;

import java.util.Map;
import java.util.Objects;

public class Machine extends Item {
    private MachineType type;
    private int hoursLeft;
    private Boolean isActivated;
    private Boolean isReady;
    private Item preparedProduct;

    public Machine(MachineType type) {
        super(type.getName(), true, type.getSellPrice(), true, ProductQuality.Normal, false);
        this.type = type;
        if (type.getSellPrice() == 0) {
            isSellable = false;
        }
        this.preparedProduct = null;
        this.hoursLeft = 0;
        this.isActivated = false;
        this.isReady = false;
    }

    public MachineType getType() {
        return type;
    }

    public void setType(MachineType type) {
        this.type = type;
    }

    public int getHoursLeft() {
        return hoursLeft;
    }

    public Boolean getActivated() {
        return isActivated;
    }

    public Item getPreparedProduct() {
        return preparedProduct;
    }

    public void setHoursLeft(int hoursLeft) {
        this.hoursLeft = hoursLeft;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public void setPreparedProduct(Item preparedProduct) {
        this.preparedProduct = preparedProduct;
    }

    public Boolean getReady() {
        return isReady;
    }

    public void setReady(Boolean ready) {
        isReady = ready;
    }

    public Result useMachine(String productName, User currentPlayer) {
        for (randomStuffType item : this.type.getProducts()) {
            if (item.getName().equals(productName)) {
                if(item.getIngredients() == null)
                    return new Result(false, "There are no ingredients for " + productName);
                for (String name : item.getIngredients().keySet()) {
                    if (!currentPlayer.getBackpack().hasItem(name, item.getIngredients().get(name))) {
                        return new Result(false,"You have not enough ingredients to use this machine");
                    }
                }
                for (String name : item.getIngredients().keySet()) {
                    currentPlayer.getBackpack().grabItem(name, item.getIngredients().get(name));
                }
                setActivated(true);
                setHoursLeft(item.getProcessingTime());
                preparedProduct = Item.getRandomItem(item.getName());
                return new Result(true,"Process started successfully.");
            }
        }
        return new Result(false, "You cant make this item with this machine.");
    }

    public static void updateMachines() {  //use this method every hour
        for (User user : App.getInstance().getCurrentGame().getPlayers()) {
            House house = App.getInstance().getCurrentGame().getMap().getFarmByOwner(user).getHouse();
            for (Machine machine : house.getMachines()) {
                if (machine.getActivated()) {
                    machine.setHoursLeft(machine.getHoursLeft() - 1);
                    if (machine.getHoursLeft() == 0) {
                        machine.setActivated(false);
                        machine.setReady(true);
                    }
                }
            }
        }
    }

    public Result grabPreparedProduct(User currentPlayer) {
        if (isReady) {
            Result result = currentPlayer.getBackpack().addItem(preparedProduct,1);
            if (!result.isSuccessful()) return result;
            isReady = false;
            preparedProduct = null;
            return new Result(true, "Grabbed the product successfully.");
        }
        else if (isActivated) {
            return new Result(false,"Product isnt ready yet!");
        }
        else return new Result(false,"No product to grab");
    }

//    @Override
//    public Machine copy() {
//        // Creating a new instance of Machine and copying relevant fields
//        Machine newMachine = new Machine(type);
//        newMachine.setSellable(isSellable());
//        newMachine.setPlaceable(isPlaceable());
//        newMachine.setQuality(getProductQuality());
//        if (type.getSellPrice() == 0) {
//            newMachine.setSellable(false);
//        }
//        return newMachine;
//    }

    @Override
    public Machine copy() {
        Machine newMachine = new Machine(type);
        newMachine.setSellable(isSellable());
        newMachine.setPlaceable(isPlaceable());
        newMachine.setQuality(getProductQuality());

        newMachine.setHoursLeft(this.hoursLeft);
        newMachine.setActivated(this.isActivated);
        newMachine.setReady(this.isReady);

        if (this.preparedProduct != null) {
            newMachine.setPreparedProduct(this.preparedProduct.copy());
        }

        if (type.getSellPrice() == 0) {
            newMachine.setSellable(false);
        }

        return newMachine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Machine that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(type, that.type) &&
                Objects.equals(hoursLeft, that.hoursLeft) &&
                Objects.equals(isActivated, that.isActivated) &&
                Objects.equals(isReady, that.isReady) &&
                Objects.equals(preparedProduct, that.preparedProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, hoursLeft, isActivated, isReady, preparedProduct);
    }

}
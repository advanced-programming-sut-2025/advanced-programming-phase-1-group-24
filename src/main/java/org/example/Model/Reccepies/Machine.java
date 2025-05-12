package org.example.Model.Reccepies;

import org.example.Model.Things.Item;
import org.example.Model.Things.ProductQuality;
import java.util.Objects;

public class Machine extends Item {
    private MachineType type;

    public Machine(MachineType type) {
        super(type.getName(), true, type.getSellPrice(), true, ProductQuality.Normal, false);
        this.type = type;
        if (type.getSellPrice() == 0) {
            isSellable = false;
        }
    }

    public MachineType getType() {
        return type;
    }

    public void setType(MachineType type) {
        this.type = type;
    }

    @Override
    public Machine copy() {
        // Creating a new instance of Machine and copying relevant fields
        Machine newMachine = new Machine(type);
        newMachine.setSellable(isSellable());
        newMachine.setPlaceable(isPlaceable());
        newMachine.setQuality(getProductQuality());
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
        return type == that.type;  // Check if MachineType is the same
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);  // Combine hash codes of parent class and type
    }
}

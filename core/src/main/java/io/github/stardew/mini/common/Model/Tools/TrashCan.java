package io.github.stardew.mini.common.Model.Tools;

import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.common.Model.Things.Item;
import io.github.stardew.mini.common.Model.Things.ToolMaterial;

public class TrashCan extends Tool {


    public TrashCan(ToolType type) {
        super(type);
    }
    public TrashCan(){}
    public void upgrade(ToolMaterial material){
        this.material = material;
    }

    public void useTrashCan(Item item, int amount){
        if (material == ToolMaterial.Initial){
            return;
        }
        else if (material == ToolMaterial.Copper){
            int initialMoney = MainApp.getInstance().getCurrentGame().getCurrentPlayer().getMoney();
            int addedMoney = amount * item.getPrice() * 15 / 100;
            MainApp.getInstance().getCurrentGame().getCurrentPlayer().addMoney(addedMoney);
        }
        else if (material == ToolMaterial.Iron){
            int initialMoney = MainApp.getInstance().getCurrentGame().getCurrentPlayer().getMoney();
            int addedMoney = amount * item.getPrice() * 30 / 100;
            MainApp.getInstance().getCurrentGame().getCurrentPlayer().addMoney(addedMoney);
        }
        else if (material == ToolMaterial.Gold){
            int initialMoney = MainApp.getInstance().getCurrentGame().getCurrentPlayer().getMoney();
            int addedMoney = amount * item.getPrice() * 45 / 100;
            MainApp.getInstance().getCurrentGame().getCurrentPlayer().addMoney(addedMoney);
        }
        else if (material == ToolMaterial.Iridium){
            int initialMoney = MainApp.getInstance().getCurrentGame().getCurrentPlayer().getMoney();
            int addedMoney = amount * item.getPrice() * 60 / 100;
            MainApp.getInstance().getCurrentGame().getCurrentPlayer().addMoney(addedMoney);
        }
    }

    public void setMaterial(ToolMaterial material) {
        this.material = material;
    }

    public ToolMaterial getMaterial() {
        return material;
    }

    @Override
    public TrashCan copy() {
        TrashCan copy = new TrashCan(this.type);
        copy.upgrade(this.material);
        return copy;
    }


}

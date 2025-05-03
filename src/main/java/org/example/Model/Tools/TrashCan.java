package org.example.Model.Tools;

import org.example.Model.App;
import org.example.Model.Things.Item;
import org.example.Model.Things.ToolMaterial;

public class TrashCan{
    ToolMaterial material;

    public TrashCan() {
        this.material = ToolMaterial.Initial;
    }

    public void upgrade(ToolMaterial material){
        this.material = material;
    }

    public void useTrashCan(Item item, int amount){
        if (material == ToolMaterial.Initial){
            return;
        }
        else if (material == ToolMaterial.Copper){
            int initialMoney = App.getInstance().getCurrentGame().getCurrentPlayer().getMoney();
            int addedMoney = amount * item.getPrice() * 15 / 100;
            App.getInstance().getCurrentGame().getCurrentPlayer().setMoney(amount + addedMoney);
        }
        else if (material == ToolMaterial.Iron){
            int initialMoney = App.getInstance().getCurrentGame().getCurrentPlayer().getMoney();
            int addedMoney = amount * item.getPrice() * 30 / 100;
            App.getInstance().getCurrentGame().getCurrentPlayer().setMoney(amount + addedMoney);
        }
        else if (material == ToolMaterial.Gold){
            int initialMoney = App.getInstance().getCurrentGame().getCurrentPlayer().getMoney();
            int addedMoney = amount * item.getPrice() * 45 / 100;
            App.getInstance().getCurrentGame().getCurrentPlayer().setMoney(amount + addedMoney);
        }
        else if (material == ToolMaterial.Iridium){
            int initialMoney = App.getInstance().getCurrentGame().getCurrentPlayer().getMoney();
            int addedMoney = amount * item.getPrice() * 60 / 100;
            App.getInstance().getCurrentGame().getCurrentPlayer().setMoney(amount + addedMoney);
        }
    }
}

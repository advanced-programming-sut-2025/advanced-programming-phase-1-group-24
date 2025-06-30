package io.github.stardew.mini.Model.Places;

import java.util.ArrayList;

import io.github.stardew.mini.Model.Animals.Animal;
import io.github.stardew.mini.Model.App;
import io.github.stardew.mini.Model.Growables.Growable;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.Reccepies.randomStuff;
import io.github.stardew.mini.Model.Things.Backpack;
import io.github.stardew.mini.Model.Things.Fish;
import io.github.stardew.mini.Model.Things.Food;
import io.github.stardew.mini.Model.Things.ForagingMineral;
import io.github.stardew.mini.Model.Tools.TrashCan;
import io.github.stardew.mini.Model.User;

public class Shop extends Place {
    private ShopType shopType;
    private String shopName;
    private String shopOwner;
    private int startHour;
    private int endHour;
    private ArrayList<ShopItem> products;

    public Shop(ShopType shopType, String shopName, String shopOwner, int startHour, int endHour,
                ArrayList<ShopItem> products, int startX, int startY, int width, int height) {
        this.shopType = shopType;
        this.shopName = shopName;
        this.shopOwner = shopOwner;
        this.startHour = startHour;
        this.endHour = endHour;
        this.products = products;

        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;

    }


    public ShopType getShopType() {
        return shopType;
    }

    public void setShopType(ShopType shopType) {
        this.shopType = shopType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopOwner() {
        return shopOwner;
    }

    public void setShopOwner(String shopOwner) {
        this.shopOwner = shopOwner;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public ArrayList<ShopItem> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ShopItem> products) {
        this.products = products;
    }
    public ShopItem getItemByName(String name) {
        for (ShopItem item : products) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

}

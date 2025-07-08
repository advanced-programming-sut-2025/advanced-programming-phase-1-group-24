package io.github.stardew.mini.Model.Places;

import com.badlogic.gdx.graphics.Texture;
import io.github.stardew.mini.Model.Assets.ShopAssets;

public enum ShopType {
    BLACKSMITH(ShopAssets.Blacksmith),
    JOJA_MART(ShopAssets.Jojamart),
    PIERRE_GENERAL_SHOP(ShopAssets.PierresGeneral),
    CARPENTER_SHOP(ShopAssets.Carpenter),
    FISH_SHOP(ShopAssets.Fish),
    MARNIE_RANCH(ShopAssets.Marnie),
    STAR_DROP_SALOON(ShopAssets.StardropSaloon);
    private Texture texture;
    ShopType(Texture texture) {
        this.texture = texture;
    }
    public Texture getTexture() {
        return texture;
    }
}


package io.github.stardew.mini.client.View;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.client.Assets.InventoryAssets;
import io.github.stardew.mini.client.MainApp;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class TradeDialog extends Dialog {

    private final User localPlayer;
    private final User remotePlayer;
    private final GameView gameView;

    private final Table localOfferGrid;
    private final Table remoteOfferGrid;
    private final Table inventoryTable;

    private final TextButton confirmButton;
    private final TextButton cancelButton;
    private final TextButton tradeButton;
    private final Label localPlayerStatus;
    private final Label remotePlayerStatus;

    private boolean localPlayerConfirmed = false;
    private boolean remotePlayerConfirmed = false;

    private final List<Map<String, Object>> localPlayerOfferItems = new ArrayList<>();

    public TradeDialog(User localPlayer, User remotePlayer, GameView gameView, Skin skin) {
        super("Trade with " + remotePlayer.getUsername(), skin, "custom-window");
        this.localPlayer = localPlayer;
        this.remotePlayer = remotePlayer;
        this.gameView = gameView;

        setModal(true);
        setMovable(false);
        padTop(40f);

        Table content = getContentTable();
        content.pad(10f);

        // --- Main Panels ---
        Table mainPanels = new Table();
        localOfferGrid = createPlayerPanel(localPlayer.getUsername());
        remoteOfferGrid = createPlayerPanel(remotePlayer.getUsername());
        mainPanels.add(localOfferGrid).expand().fill().pad(5);
        mainPanels.add(remoteOfferGrid).expand().fill().pad(5);
        content.add(mainPanels).row();

        // --- Status Labels ---
        Table statusTable = new Table();
        localPlayerStatus = new Label("Offering...", skin, "custom-label");
        remotePlayerStatus = new Label("Offering...", skin, "custom-label");
        localPlayerStatus.setColor(Color.WHITE);
        remotePlayerStatus.setColor(Color.WHITE);
        statusTable.add(localPlayerStatus).expandX();
        statusTable.add(remotePlayerStatus).expandX();
        content.add(statusTable).fillX().padTop(5).row();

        // --- Inventory Display ---
        Label inventoryLabel = new Label("Your Inventory (Click to add/remove offer)", skin, "custom-label");
        content.add(inventoryLabel).colspan(2).padTop(15).row();
        inventoryTable = new Table();
        ScrollPane inventoryScrollPane = new ScrollPane(inventoryTable, skin);
        inventoryScrollPane.setFadeScrollBars(false);
        content.add(inventoryScrollPane).colspan(2).height(160).expandX().fillX().row();

        // --- Action Buttons ---
        confirmButton = new TextButton("Confirm Offer", skin, "custom-button");
        tradeButton = new TextButton("Trade", skin, "custom-button");
        cancelButton = new TextButton("Cancel", skin, "custom-button");
        tradeButton.setDisabled(true);

        getButtonTable().add(confirmButton).pad(10);
        getButtonTable().add(tradeButton).pad(10);
        getButtonTable().add(cancelButton).pad(10);

        setupListeners();
        displayInventory();
    }

    private Table createPlayerPanel(String playerName) {
        Table panel = new Table();
        panel.top();
        panel.add(new Label(playerName, GameAssetManager.skin, "custom-label")).padBottom(10).row();
        Table grid = new Table();
        for (int i = 0; i < 4; i++) {
            grid.add(createEmptySlot()).size(64).pad(2);
        }
        panel.add(grid);
        return panel;
    }

    private Stack createEmptySlot() {
        Stack slot = new Stack();
        slot.add(new Image(InventoryAssets.slot));
        return slot;
    }

    private void setupListeners() {
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                localPlayerConfirmed = !localPlayerConfirmed;
                updateLocalConfirmStatus();
                sendConfirmStatusUpdate();
            }
        });

        tradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendFinalizeTradeRequest();
                hide();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendCancelTradeRequest();
                hide();
            }
        });
    }

    private void displayInventory() {
        inventoryTable.clear();
        Map<Item, Integer> items = localPlayer.getBackpack().getInventoryItems();
        int i = 0;
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            int count = entry.getValue();

            Stack itemSlot = createItemSlot(item, count);

            itemSlot.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (localPlayerConfirmed) return;
                    toggleItemInOffer(item);
                }
            });

            inventoryTable.add(itemSlot).size(64).pad(2);
            if (++i % 8 == 0) {
                inventoryTable.row();
            }
        }
    }

    private Stack createItemSlot(Item item, int count) {
        Stack itemSlot = new Stack();
        itemSlot.add(new Image(InventoryAssets.slot));
        if (item != null) {
            Image itemImage = new Image(gameView.getItemTexture(item));
            itemImage.setScaling(Scaling.fit);
            itemSlot.add(itemImage);

            if (count > 1) {
                Label countLabel = new Label(String.valueOf(count), GameAssetManager.skin, "custom-label");
                countLabel.setAlignment(Align.bottomRight);
                countLabel.setFontScale(0.8f);
                itemSlot.add(countLabel);
            }
        }
        return itemSlot;
    }

    private void toggleItemInOffer(Item item) {
        Optional<Map<String, Object>> existingItem = localPlayerOfferItems.stream()
            .filter(map -> map.get("name").equals(item.getName()))
            .findFirst();

        if (existingItem.isPresent()) {
            localPlayerOfferItems.remove(existingItem.get());
        } else {
            if (localPlayerOfferItems.size() < 4) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("name", item.getName());
                itemData.put("count", Integer.valueOf(1));
                localPlayerOfferItems.add(itemData);
            }
        }
        updateOfferDisplay(localOfferGrid, localPlayerOfferItems);
        sendOfferUpdate();
    }

    private void updateOfferDisplay(Table offerPanel, List<Map<String, Object>> items) {
        Table grid = (Table) offerPanel.getCells().get(1).getActor();
        grid.clearChildren();

        for (int i = 0; i < 4; i++) {
            if (i < items.size()) {
                Map<String, Object> itemData = items.get(i);
                Item item = Item.getRandomItem((String) itemData.get("name"));
                int count = ((Number) itemData.get("count")).intValue();
                grid.add(createItemSlot(item, count)).size(64).pad(2);
            } else {
                grid.add(createEmptySlot()).size(64).pad(2);
            }
        }
    }

    private void updateLocalConfirmStatus() {
        if (localPlayerConfirmed) {
            confirmButton.setText("Unconfirm");
            localPlayerStatus.setText("Confirmed!");
            localPlayerStatus.setColor(Color.GREEN);
        } else {
            confirmButton.setText("Confirm Offer");
            localPlayerStatus.setText("Offering...");
            localPlayerStatus.setColor(Color.WHITE);
        }
        updateTradeButtonState();
    }

    public void setRemotePlayerConfirmed(boolean confirmed) {
        this.remotePlayerConfirmed = confirmed;
        if (confirmed) {
            remotePlayerStatus.setText("Confirmed!");
            remotePlayerStatus.setColor(Color.GREEN);
        } else {
            remotePlayerStatus.setText("Offering...");
            remotePlayerStatus.setColor(Color.WHITE);
        }
        updateTradeButtonState();
    }

    public void updateRemoteOffer(List<Map<String, Object>> itemsData) {
        updateOfferDisplay(remoteOfferGrid, itemsData);
    }

    private void updateTradeButtonState() {
        tradeButton.setDisabled(!(localPlayerConfirmed && remotePlayerConfirmed));
    }

    // --- Network Communication ---
    private void sendOfferUpdate() {
        Map<String, Object> params = new HashMap<>();
        params.put("targetUsername", remotePlayer.getUsername());
        params.put("offeredItems", localPlayerOfferItems);
        MainApp.getInstance().getNetworkClient().sendPost(
            MainApp.getInstance().getCurrentGame().getNetworkId(), "GameController", "updateTradeOffer", params, localPlayer.getUsername());
    }

    private void sendConfirmStatusUpdate() {
        Map<String, Object> params = new HashMap<>();
        params.put("targetUsername", remotePlayer.getUsername());
        params.put("isConfirmed", localPlayerConfirmed);
        MainApp.getInstance().getNetworkClient().sendPost(
            MainApp.getInstance().getCurrentGame().getNetworkId(), "GameController", "confirmTradeOffer", params, localPlayer.getUsername());
    }

    private void sendFinalizeTradeRequest() {
        Map<String, Object> params = new HashMap<>();
        params.put("targetUsername", remotePlayer.getUsername());
        MainApp.getInstance().getNetworkClient().sendPost(
            MainApp.getInstance().getCurrentGame().getNetworkId(), "GameController", "finalizeTrade", params, localPlayer.getUsername());
    }

    private void sendCancelTradeRequest() {
        Map<String, Object> params = new HashMap<>();
        params.put("targetUsername", remotePlayer.getUsername());
        MainApp.getInstance().getNetworkClient().sendPost(
            MainApp.getInstance().getCurrentGame().getNetworkId(), "GameController", "cancelTrade", params, localPlayer.getUsername());
    }
}

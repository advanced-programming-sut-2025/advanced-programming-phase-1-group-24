package io.github.stardew.mini.client.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.client.Assets.InventoryAssets;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.server.Controller.GameController;

import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatDialog extends Dialog {

    private Table publicChatTable;
    private Table privateChatTable;
    private TextField messageInput;
    private TextButton sendButton;
    private TextButton publicTabButton;
    private TextButton privateTabButton;
    private ScrollPane publicScrollPane;
    private ScrollPane privateScrollPane;
    private SelectBox<String> privateRecipientSelectBox;
    private GameView gameView;

    private boolean isPublicChatActive = true;

    private final List<String> publicMessages = new ArrayList<>();
    private final Map<String, List<String>> privateMessages = new ConcurrentHashMap<>();
    private final Map<String, Boolean> unreadPrivateMessages = new ConcurrentHashMap<>();

    private static final Pattern MENTION_PATTERN = Pattern.compile("@(\\w+)");

    private GameController controller;

    public ChatDialog(Skin skin, GameController controller, GameView gameView) {
        super("Game Chat", skin, "custom-window");
        this.controller = controller;
        this.gameView = gameView;
        padTop(40);
        getTitleLabel().setAlignment(Align.center);
        setBackground(new TextureRegionDrawable(InventoryAssets.inventoryMenuBackground));

        createUI(skin);
        setupListeners();
        updateChatView();
        updatePrivateTabButtonNotification();
    }

    private void createUI(Skin skin) {
        Table rootTable = getContentTable();
        rootTable.defaults().pad(5);

        rootTable.setBackground(new TextureRegionDrawable(InventoryAssets.inventoryMenuBackground));
        rootTable.setDebug(true);

        // Tab buttons
        Table tabTable = new Table();
        publicTabButton = new TextButton("Public", skin, "custom-button");
        privateTabButton = new TextButton("Private", skin, "custom-button");
        tabTable.add(publicTabButton).expandX().fillX();
        tabTable.add(privateTabButton).expandX().fillX();
        rootTable.add(tabTable).expandX().fillX().row();

        // Chat display area
        publicChatTable = new Table(skin);
        publicChatTable.top().left().pad(5);
        publicChatTable.defaults().expandX().fillX().left().padBottom(2);

        privateChatTable = new Table(skin);
        privateChatTable.top().left().pad(5);
        privateChatTable.defaults().expandX().fillX().left().padBottom(2);

        publicScrollPane = new ScrollPane(publicChatTable, skin);
        publicScrollPane.setFadeScrollBars(false);
        publicScrollPane.setScrollingDisabled(true, false);

        privateScrollPane = new ScrollPane(privateChatTable, skin);
        privateScrollPane.setFadeScrollBars(false);
        privateScrollPane.setScrollingDisabled(true, false);

        Stack chatStack = new Stack();
        chatStack.add(publicScrollPane);
        chatStack.add(privateScrollPane);
        rootTable.add(chatStack).expand().fill().row();

        // Private recipient select box
        privateRecipientSelectBox = new SelectBox<>(skin);
        updatePrivateRecipients();
        rootTable.add(privateRecipientSelectBox).expandX().fillX().row();
        privateRecipientSelectBox.setColor(Color.ORANGE);
        privateRecipientSelectBox.setVisible(false);

        // Message input and send button
        Table inputTable = new Table();
        messageInput = new TextField("", skin);
        messageInput.setMessageText("Type your message...");
        messageInput.setColor(Color.ORANGE);
        sendButton = new TextButton("Send", skin, "custom-button");
        sendButton.setColor(Color.GREEN);

        inputTable.add(messageInput).expandX().fillX();
        inputTable.add(sendButton).width(sendButton.getPrefWidth()).height(sendButton.getPrefHeight());
        rootTable.add(inputTable).expandX().fillX().row();

        // Set initial tab state
        publicTabButton.setChecked(true);
        publicTabButton.getLabel().setColor(Color.GREEN);
        privateTabButton.getLabel().setColor(Color.WHITE);
        privateScrollPane.setVisible(false);

        TextButton closeButton = new TextButton("Close", skin, "custom-button");
        closeButton.setColor(Color.RED);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                Gdx.input.setInputProcessor(gameView);
            }
        });
        getButtonTable().add(closeButton).pad(10);
    }

    private void setupListeners() {
        publicTabButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPublicChatActive = true;
                updateChatView();
                updatePrivateTabButtonNotification();
            }
        });

        privateTabButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPublicChatActive = false;
                updatePrivateRecipients();
                updateChatView();
                updatePrivateTabButtonNotification();
            }
        });

        privateRecipientSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selected = privateRecipientSelectBox.getSelected();
                if (selected != null && !selected.equals("Select Player")) {
                    String cleanSelected = selected.replace(" (New)", "");
                    if (unreadPrivateMessages.containsKey(cleanSelected) && unreadPrivateMessages.get(cleanSelected)) {
                        unreadPrivateMessages.put(cleanSelected, false); // Mark as read
                        updatePrivateTabButtonNotification();
                        updatePrivateRecipients();
                    }
                    updateChatDisplay(privateChatTable, privateMessages.getOrDefault(cleanSelected, new ArrayList<>()));
                } else {
                    privateChatTable.clearChildren();
                }
            }
        });

        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendMessage();
            }
        });

        messageInput.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == '\n' || c == '\r') {
                    sendMessage();
                }
            }
        });

        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isVisible()) {
                    Gdx.input.setInputProcessor(gameView);
                }
            }
        });
    }

    private void sendMessage() {
        String messageContent = messageInput.getText().trim();
        if (messageContent.isEmpty()) return;

        User currentUser = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
        String senderUsername = currentUser.getUsername();
        String gameId = MainApp.getInstance().getCurrentGame().getNetworkId();

        if (isPublicChatActive) {

            List<String> mentionedUsernames = new ArrayList<>();
            Matcher matcher = MENTION_PATTERN.matcher(messageContent);
            while (matcher.find()) {
                String mentionedUsername = matcher.group(1);
                mentionedUsernames.add(mentionedUsername);
            }
            System.out.println("[DEBUG-CHATDIALOG] Detected mentions: " + mentionedUsernames); // NEW DEBUG

            Map<String, Object> params = new HashMap<>();
            params.put("messageContent", messageContent);
            params.put("chatType", Message.CHAT_PUBLIC);
            if (!mentionedUsernames.isEmpty()) {
                params.put("mentionedUsers", mentionedUsernames);
            }


            MainApp.getInstance().getNetworkClient().sendPost(
                gameId,
                "GameController",
                "handleChatMessage",
                params,
                senderUsername
            ).thenAccept(response -> {
                if (response.getStatus() == 200) {
                } else {
                    Gdx.app.postRunnable(() -> {
                        if (MainApp.getInstance().getCurrentGameView() != null) {
                            MainApp.getInstance().getCurrentGameView().showErrorDialog(getStage(), "Failed to send message: " + response.getMessage());
                        }
                    });
                }
            }).exceptionally(ex -> {
                Gdx.app.postRunnable(() -> {
                    if (MainApp.getInstance().getCurrentGameView() != null) {
                        MainApp.getInstance().getCurrentGameView().showErrorDialog(getStage(), "Error sending message: " + ex.getMessage());
                    }
                });
                return null;
            });
            messageInput.setText("");
        } else {
            String recipientUsername = privateRecipientSelectBox.getSelected();
            if (recipientUsername != null && !recipientUsername.equals("Select Player")) {
                recipientUsername = recipientUsername.replace(" (New)", "");
            }

            if (recipientUsername == null || recipientUsername.isEmpty() || recipientUsername.equals("Select Player")) {
                if (MainApp.getInstance().getCurrentGameView() != null) {
                    MainApp.getInstance().getCurrentGameView().showErrorDialog(getStage(), "Please select a recipient for private message.");
                }
                return;
            }

            Map<String, Object> params = new HashMap<>();
            params.put("messageContent", messageContent);
            params.put("chatType", Message.CHAT_PRIVATE);
            params.put("recipientUsername", recipientUsername);

            MainApp.getInstance().getNetworkClient().sendPost(
                gameId,
                "GameController",
                "handleChatMessage",
                params,
                senderUsername
            ).thenAccept(response -> {
                if (response.getStatus() == 200) {
                    messageInput.setText("");
                } else {
                    Gdx.app.postRunnable(() -> {
                        if (MainApp.getInstance().getCurrentGameView() != null) {
                            MainApp.getInstance().getCurrentGameView().showErrorDialog(getStage(), "Failed to send private message: " + response.getMessage());
                        }
                    });
                }
            }).exceptionally(ex -> {
                Gdx.app.postRunnable(() -> {
                    if (MainApp.getInstance().getCurrentGameView() != null) {
                        MainApp.getInstance().getCurrentGameView().showErrorDialog(getStage(), "Error sending private message: " + ex.getMessage());
                    }
                });
                return null;
            });
        }
    }
    public void addPublicMessage(String sender, String messageContent) {
        String fullMessage = String.format("[%s] : %s", sender, messageContent);
        publicMessages.add(fullMessage);
        updateChatDisplay(publicChatTable, publicMessages);
    }

    public void addPrivateMessage(String sender, String recipient, String messageContent) {
        String fullMessage;
        String currentUsername = MainApp.getInstance().getCurrentGame().getCurrentPlayer().getUsername();

        String chatPartnerKey = sender.equals(currentUsername) ? recipient : sender;

        if (sender.equals(currentUsername)) {
            fullMessage = String.format("[%s] : %s", "You", messageContent);
        } else {
            fullMessage = String.format("[%s] : %s", sender, messageContent);
            String currentlySelectedRecipientInDropdown = privateRecipientSelectBox.getSelected();
            if (currentlySelectedRecipientInDropdown != null) {
                currentlySelectedRecipientInDropdown = currentlySelectedRecipientInDropdown.replace(" (New)", "");
            }
            if (isPublicChatActive || !chatPartnerKey.equals(currentlySelectedRecipientInDropdown)) {
                unreadPrivateMessages.put(chatPartnerKey, true);
                updatePrivateTabButtonNotification();
            }
        }
        privateMessages.computeIfAbsent(chatPartnerKey, k -> {
            return new ArrayList<>();
        }).add(fullMessage);

        if (!isPublicChatActive && privateRecipientSelectBox.getSelected() != null &&
            privateRecipientSelectBox.getSelected().replace(" (New)", "").equals(chatPartnerKey)) {
            System.out.println("[DEBUG-CHATDIALOG] Updating private chat display for " + chatPartnerKey + ". Currently selected: " + privateRecipientSelectBox.getSelected());
            updateChatDisplay(privateChatTable, privateMessages.get(chatPartnerKey));
            unreadPrivateMessages.put(chatPartnerKey, false);
            updatePrivateTabButtonNotification();
        } else {
            System.out.println("[DEBUG-CHATDIALOG] Not updating private chat display immediately. Active public: " + isPublicChatActive + ", Selected recipient: " + privateRecipientSelectBox.getSelected());
        }
    }


    private void updateChatView() {
        publicTabButton.getLabel().setColor(isPublicChatActive ? Color.GREEN : Color.WHITE);
        privateTabButton.getLabel().setColor(isPublicChatActive ? Color.WHITE : Color.GREEN);

        publicScrollPane.setVisible(isPublicChatActive);
        privateScrollPane.setVisible(!isPublicChatActive);
        privateRecipientSelectBox.setVisible(!isPublicChatActive);

        if (isPublicChatActive) {
            updateChatDisplay(publicChatTable, publicMessages);
        } else {
            String selectedRecipient = privateRecipientSelectBox.getSelected();
            if (selectedRecipient != null) {
                selectedRecipient = selectedRecipient.replace(" (New)", "");
            }
            if (selectedRecipient != null && !selectedRecipient.isEmpty() && !selectedRecipient.equals("Select Player")) {
                if (unreadPrivateMessages.getOrDefault(selectedRecipient, false)) {
                    unreadPrivateMessages.put(selectedRecipient, false);
                    updatePrivateTabButtonNotification();
                }
                updateChatDisplay(privateChatTable, privateMessages.getOrDefault(selectedRecipient, new ArrayList<>()));
            } else {
                privateChatTable.clearChildren();
            }
        }
        updatePrivateRecipients();
    }

    private void updateChatDisplay(Table chatTable, List<String> messages) {
        chatTable.clearChildren();
        for (String msg : messages) {
            Label messageLabel = new Label(msg, getSkin(), "custom-label");
            messageLabel.setWrap(true);
            chatTable.add(messageLabel).expandX().fillX().left().row();
        }
        chatTable.layout();
        if (chatTable == publicChatTable) {
            publicScrollPane.scrollTo(0, 0, 0, 0);
        } else {
            privateScrollPane.scrollTo(0, 0, 0, 0);
        }
    }

    private void updatePrivateRecipients() {
        Array<String> playerNames = new Array<>();
        playerNames.add("Select Player");
        User currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
        List<String> allPlayerUsernames = new ArrayList<>();

        for (User player : MainApp.getInstance().getCurrentGame().getPlayers()) {
            if (!player.getUsername().equals(currentPlayer.getUsername())) {
                allPlayerUsernames.add(player.getUsername());
            }
        }
        Collections.sort(allPlayerUsernames);

        String currentSelectedRecipient = privateRecipientSelectBox.getSelected();

        for (String playerName : allPlayerUsernames) {
            String displayName = playerName;
            if (unreadPrivateMessages.getOrDefault(playerName, false)) {
                displayName += " (New)";
            }
            playerNames.add(displayName);
        }

        privateRecipientSelectBox.setItems(playerNames);

        if (currentSelectedRecipient != null) {
            String cleanCurrentSelection = currentSelectedRecipient.replace(" (New)", "");
            for (String item : playerNames) {
                if (item.replace(" (New)", "").equals(cleanCurrentSelection)) {
                    privateRecipientSelectBox.setSelected(item);
                    break;
                }
            }
        } else {
            privateRecipientSelectBox.setSelectedIndex(0);
        }
    }

    private void updatePrivateTabButtonNotification() {
        boolean hasAnyUnread = unreadPrivateMessages.values().stream().anyMatch(Boolean::booleanValue);
        System.out.println("[DEBUG-CHATDIALOG] updatePrivateTabButtonNotification called. Has unread: " + hasAnyUnread + ", Active public: " + isPublicChatActive);
        if (hasAnyUnread && isPublicChatActive) {
            privateTabButton.setText("Private (New)");
            privateTabButton.getLabel().setColor(Color.YELLOW);
        } else if (hasAnyUnread && !isPublicChatActive) {
            privateTabButton.setText("Private");
            privateTabButton.getLabel().setColor(Color.WHITE);
        }
        else {
            privateTabButton.setText("Private");
            privateTabButton.getLabel().setColor(Color.WHITE);
        }
    }

    @Override
    public Dialog show(Stage stage) {
        invalidate();
        layout();
        float currentScreenWidth = Gdx.graphics.getWidth();
        float currentScreenHeight = Gdx.graphics.getHeight();
        float desiredDialogWidth = currentScreenWidth * 0.5f;
        float desiredDialogHeight = currentScreenHeight * 0.7f;

        updatePrivateRecipients();
        updateChatView();
        updatePrivateTabButtonNotification();

        Dialog dialog = super.show(stage);
        Gdx.input.setInputProcessor(stage);
        setSize(desiredDialogWidth, desiredDialogHeight);
        setPosition(
            (Gdx.graphics.getWidth() - getWidth()) / 2,
            (Gdx.graphics.getHeight() - getHeight()) / 2
        );
        return dialog;
    }

    @Override
    public void hide() {
        super.hide();
        this.setVisible(false);
        Gdx.input.setInputProcessor(gameView);
    }
}

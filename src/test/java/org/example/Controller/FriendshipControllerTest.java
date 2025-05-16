package org.example.Controller;

import org.example.Model.Friendships.Friendship;
import org.example.Model.Friendships.Gift;
import org.example.Model.Friendships.Message;
import org.example.Model.MapManagement.Tile;
import org.example.Model.Menus.Menu;
import org.example.Model.Reccepies.randomStuff;
import org.example.Model.Reccepies.randomStuffType;
import org.example.Model.Things.Backpack;
import org.example.Model.Things.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.example.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FriendshipControllerTest {

    private App mockApp;
    private FriendshipController controller;
    private Game mockGame;
    private User currentPlayer;
    private User receiver;
    private Friendship mockFriendship;
    private Tile senderTile;
    private Tile receiverTile;
    private Backpack backpack;
    private randomStuff randomRing;



    @BeforeEach
    void setUp() {
        controller = new FriendshipController();

        // Setup mocks
        mockGame = mock(Game.class);
        mockApp = mock(App.class);
        currentPlayer = mock(User.class);
        receiver = mock(User.class);
        mockFriendship = mock(Friendship.class);
        backpack = mock(Backpack.class);
        randomRing = mock(randomStuff.class);


        // Setup App and Game
        when(mockApp.getCurrentGame()).thenReturn(mockGame);
        App.setInstance(mockApp);

        when(mockGame.getCurrentPlayer()).thenReturn(currentPlayer);
    }

    // === Tests for talk ===
    @Test
    void testTalk_userNotFound_returnsError() {
        when(mockGame.getPlayerByUsername("receiver")).thenReturn(null);

        Result result = controller.talk("receiver", "Hello");
        assertFalse(result.isSuccessful());
        assertEquals("One or both users of the relation not found.", result.message());
    }

    @Test
    void testTalk_notAdjacent_returnsError() {
        // Arrange
        String username = "receiverUsername";
        String message = "Hello!";

        // Mock player data
        when(currentPlayer.getUsername()).thenReturn("senderUsername");
        when(receiver.getUsername()).thenReturn(username);
        when(mockGame.getPlayerByUsername(username)).thenReturn(receiver);

        // Mock friendship
        when(mockGame.getFriendship("senderUsername", username)).thenReturn(mockFriendship);

        // Mock the current tiles of sender and receiver
        Tile senderTile = mock(Tile.class);
        Tile receiverTile = mock(Tile.class);
        when(currentPlayer.getCurrentTile()).thenReturn(senderTile);
        when(receiver.getCurrentTile()).thenReturn(receiverTile);

        // Mock getX and getY for the tiles to return specific values
        when(senderTile.getX()).thenReturn(1); // Example value
        when(senderTile.getY()).thenReturn(1); // Example value
        when(receiverTile.getX()).thenReturn(3); // Example value
        when(receiverTile.getY()).thenReturn(3); // Example value

        // Mock that the players are not adjacent
        //  when(controller.isAdjacent(senderTile, receiverTile)).thenReturn(false);

        // Act
        Result result = controller.talk(username, message);

        // Assert
        assertFalse(result.isSuccessful());
        assertEquals("Players are not adjacent.", result.message());
    }


    @Test
    void testTalk_withPartner_bonusXP() {
        // Mocks
        Tile mockTile = mock(Tile.class);
        User mockPartner = mock(User.class);
        ArrayList<Message> mockTalkHistory = mock(ArrayList.class);

        // Mock game structure
        when(mockGame.getPlayerByUsername("receiver")).thenReturn(receiver);
        when(mockGame.getFriendship(any(), any())).thenReturn(mockFriendship);

        // Return mocked tiles
        when(currentPlayer.getCurrentTile()).thenReturn(mockTile);
        when(receiver.getCurrentTile()).thenReturn(mockTile); // Same tile = adjacent

        // Return same partner instance for both users
        when(currentPlayer.getPartner()).thenReturn(mockPartner);
        when(receiver.getPartner()).thenReturn(mockPartner);

        // Usernames
        when(currentPlayer.getUsername()).thenReturn("sender");
        when(receiver.getUsername()).thenReturn("receiver");

        // Talk history
        when(mockFriendship.getTalkHistory()).thenReturn(mockTalkHistory);

        // Run method
        Result result = controller.talk("receiver", "Hello");

        // Verify effects
        verify(mockFriendship).addXp(50);  // Bonus XP
        verify(receiver).addToNotifications(any(Message.class));
        verify(mockTalkHistory).add(any(Message.class));

        assertTrue(result.isSuccessful());
        assertTrue(result.message().contains("message sent successfully"));
    }


    @Test
    void testTalk_noPartner_regularXP() {
        // Create necessary mocks
        Tile mockTile = mock(Tile.class);  // ✅ correct for Tile object
        ArrayList<Message> mockTalkHistory = mock(ArrayList.class);  // Needed for .add()

        // Game structure setup
        when(mockGame.getPlayerByUsername("receiver")).thenReturn(receiver);
        when(mockGame.getFriendship(any(), any())).thenReturn(mockFriendship);

        // Set up adjacency by giving both users the same tile
        when(currentPlayer.getCurrentTile()).thenReturn(mockTile);
        when(receiver.getCurrentTile()).thenReturn(mockTile);

        // Simulate no partner
        when(currentPlayer.getPartner()).thenReturn(null);
        when(receiver.getPartner()).thenReturn(null);

        // Set up usernames
        when(currentPlayer.getUsername()).thenReturn("sender");
        when(receiver.getUsername()).thenReturn("receiver");

        // Talk history for verification
        when(mockFriendship.getTalkHistory()).thenReturn(mockTalkHistory);

        // Run method
        Result result = controller.talk("receiver", "Hello");

        // Verify regular XP and message behavior
        verify(mockFriendship).addXp(20);
        verify(receiver).addToNotifications(any(Message.class));
        verify(mockTalkHistory).add(any(Message.class));

        assertTrue(result.isSuccessful());
    }


    // === Tests for showTalkHistory ===
    @Test
    void testShowTalkHistory_empty_returnsError() {
        when(mockGame.getFriendship(any(), any())).thenReturn(mockFriendship);
        when(mockFriendship.getTalkHistory()).thenReturn(new ArrayList<>());

        Result result = controller.showTalkHistory("receiver");
        assertFalse(result.isSuccessful());
        assertEquals("No messages found between you and receiver", result.message());
    }

    @Test
    void testShowTalkHistory_showsFormattedMessages() {
        ArrayList<Message> history = new ArrayList<>();
        history.add(new Message("sender", "receiver", "Hello"));

        when(mockGame.getFriendship(any(), any())).thenReturn(mockFriendship);
        when(mockFriendship.getTalkHistory()).thenReturn(history);
        when(currentPlayer.getUsername()).thenReturn("sender");

        Result result = controller.showTalkHistory("receiver");
        assertTrue(result.isSuccessful());
        assertTrue(result.message().contains("Talk history with receiver"));
    }

    // === Tests for hug ===
    @Test
    void testHug_userNotFound_returnsError() {
        when(mockGame.getPlayerByUsername("receiver")).thenReturn(null);

        Result result = controller.hug("receiver");
        assertFalse(result.isSuccessful());
        assertEquals("One or both users of the relation not found.", result.message());
    }

    // --- HUG TESTS ---

    @Test
    void testHug_usersNotFound_returnsError() {
        when(mockGame.getPlayerByUsername("target")).thenReturn(null);

        Result result = controller.hug("target");

        assertFalse(result.isSuccessful());
        assertEquals("One or both users of the relation not found.", result.message());
    }

    @Test
    void testHug_notEnoughLevel_returnsError() {
        setupBasicFriendship("target");
        when(mockFriendship.getLevel()).thenReturn(1);

        Result result = controller.hug("target");

        assertFalse(result.isSuccessful());
        assertEquals("You have not enough level!", result.message());
    }

    @Test
    void testHug_notAdjacent_returnsError() {
        setupBasicFriendship("target");
        when(mockFriendship.getLevel()).thenReturn(2);
        setupTiles(0, 0, 5, 5); // not adjacent

        Result result = controller.hug("target");

        assertFalse(result.isSuccessful());
        assertEquals("Players are not adjacent.", result.message());
    }

    @Test
    void testHug_samePartner_adds50XP() {
        setupBasicFriendship("target");
        when(mockFriendship.getLevel()).thenReturn(2);
        setupTiles(0, 0, 0, 1); // adjacent

        // Mock the same partner for both
        User partner = mock(User.class);
        when(currentPlayer.getPartner()).thenReturn(partner);
        when(receiver.getPartner()).thenReturn(partner);

        Result result = controller.hug("target");

        verify(mockFriendship).addXp(50);
        assertTrue(result.isSuccessful());
        assertTrue(result.message().contains("You succesfully hugged target"));
    }


    @Test
    void testHug_differentPartner_adds60XP() {
        setupBasicFriendship("target");
        when(mockFriendship.getLevel()).thenReturn(2);
        setupTiles(0, 0, 1, 1); // adjacent

        // Use different mock User objects for partners
        User partnerA = mock(User.class);
        User partnerB = mock(User.class);

        when(currentPlayer.getPartner()).thenReturn(partnerA);
        when(receiver.getPartner()).thenReturn(partnerB);

        Result result = controller.hug("target");

        verify(mockFriendship).addXp(60);
        assertTrue(result.isSuccessful());
    }

    // --- ASK MARRIAGE TESTS ---

    @Test
    void testAskMarriage_usersNotFound() {
        when(mockGame.getPlayerByUsername("target")).thenReturn(null);

        Result result = controller.askMarriage("target", "ring");

        assertFalse(result.isSuccessful());
        assertEquals("One or both users ot the relation not found.", result.message());
    }

    @Test
    void testAskMarriage_senderIsFemale_returnsError() {
        setupBasicFriendship("target");
        when(currentPlayer.isGender()).thenReturn(true); // true = female

        Result result = controller.askMarriage("target", "ring");

        assertFalse(result.isSuccessful());
        assertEquals("Only the male players can ask marriage.", result.message());
    }

    @Test
    void testAskMarriage_notEnoughLevel() {
        setupBasicFriendship("target");
        when(currentPlayer.isGender()).thenReturn(false);
        when(mockFriendship.getLevel()).thenReturn(2);

        Result result = controller.askMarriage("target", "ring");

        assertFalse(result.isSuccessful());
        assertEquals("You have not enough level!", result.message());
    }

    @Test
    void testAskMarriage_notAdjacent() {
        setupBasicFriendship("target");
        when(currentPlayer.isGender()).thenReturn(false);
        when(mockFriendship.getLevel()).thenReturn(3);
        setupTiles(0, 0, 10, 10);

        Result result = controller.askMarriage("target", "ring");

        assertFalse(result.isSuccessful());
        assertEquals("Players are not adjacent.", result.message());
    }

    @Test
    void testAskMarriage_noRing_returnsError() {
        setupBasicFriendship("target");
        when(currentPlayer.isGender()).thenReturn(false);
        when(mockFriendship.getLevel()).thenReturn(3);
        setupTiles(0, 0, 0, 1);
        when(currentPlayer.getBackpack()).thenReturn(backpack);
        when(backpack.grabItemAndReturn("Wedding Ring", 1)).thenReturn(null);

        Result result = controller.askMarriage("target", "ring");

        assertFalse(result.isSuccessful());
        assertEquals("You don't have any ring!", result.message());
    }

    @Test
    void testAskMarriage_success() {
        setupBasicFriendship("target");
        when(currentPlayer.isGender()).thenReturn(false);
        when(mockFriendship.getLevel()).thenReturn(3);
        setupTiles(0, 0, 1, 1);
        when(currentPlayer.getBackpack()).thenReturn(backpack);
        when(backpack.grabItemAndReturn("Wedding Ring", 1)).thenReturn(randomRing);

        Result result = controller.askMarriage("target", "ring");

        verify(receiver).addToNotifications(any(Message.class));
        assertTrue(result.isSuccessful());
    }

    // --- RESPOND TO MARRIAGE TESTS ---

    @Test
    void testRespondToMarriage_accept() {
        setupBasicFriendship("target");
        when(receiver.getBackpack()).thenReturn(backpack);
        when(backpack.grabItemAndReturn("Wedding Ring", 1)).thenReturn(randomRing);
        when(currentPlayer.getBackpack()).thenReturn(backpack);
        when(currentPlayer.getMoney()).thenReturn(100);
        when(receiver.getMoney()).thenReturn(50);
        when(mockGame.getPlayerByUsername("target")).thenReturn(receiver);

        Result result = controller.respondToMarriage("accept", "target");

        verify(mockFriendship).setLevel(4);
        verify(currentPlayer).setPartner(receiver);
        verify(receiver).setPartner(currentPlayer);
        verify(currentPlayer).setMoney(150);
        verify(receiver).setMoney(150);
        assertTrue(result.isSuccessful());
    }

    @Test
    void testRespondToMarriage_reject() {
        setupBasicFriendship("target");
        when(receiver.getEnergy()).thenReturn(80);
        when(mockGame.getPlayerByUsername("target")).thenReturn(receiver);

        Result result = controller.respondToMarriage("reject", "target");

        verify(mockFriendship).setLevel(0);
        verify(receiver).setEnergy(40);
        verify(receiver).setDaysSinceRejection(7);
        assertFalse(result.isSuccessful());
    }

    // --- HELPERS ---

    private void setupBasicFriendship(String targetUsername) {
        when(mockGame.getPlayerByUsername(targetUsername)).thenReturn(receiver);
        when(mockGame.getFriendship(any(), eq(targetUsername))).thenReturn(mockFriendship);
        when(currentPlayer.getUsername()).thenReturn("sender");
    }

    private void setupTiles(int sx, int sy, int rx, int ry) {
        senderTile = mock(Tile.class);
        receiverTile = mock(Tile.class);

        when(currentPlayer.getCurrentTile()).thenReturn(senderTile);
        when(receiver.getCurrentTile()).thenReturn(receiverTile);
        when(senderTile.getX()).thenReturn(sx);
        when(senderTile.getY()).thenReturn(sy);
        when(receiverTile.getX()).thenReturn(rx);
        when(receiverTile.getY()).thenReturn(ry);
    }


    @Test
    void sendFlower_alreadyHighLevel_noLevelUp() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(3); // Already high enough

        Backpack senderBackpack = mock(Backpack.class);
        Backpack receiverBackpack = mock(Backpack.class);
        when(currentPlayer.getBackpack()).thenReturn(senderBackpack);
        when(receiver.getBackpack()).thenReturn(receiverBackpack);
        when(senderBackpack.getItemCount("Bouquet")).thenReturn(1);
        randomStuff flower = new randomStuff(20, randomStuffType.Bouquet);
        when(senderBackpack.grabItemAndReturn("Bouquet", 1)).thenReturn(flower);
        when(receiverBackpack.addItem(any(), eq(1)))
                .thenReturn(new Result(true, "Success"));

        setupTiles(0, 0, 0, 1);
        Result result = controller.sendFlower("Bob");

        verify(mockFriendship, never()).setLevel(anyInt()); // Ensure no level up
        assertTrue(result.isSuccessful());
    }

    @Test
    void sendFlower_negativeFlowerCount_treatedAsNoFlower() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(2);

        Backpack senderBackpack = mock(Backpack.class);
        when(currentPlayer.getBackpack()).thenReturn(senderBackpack);
        when(senderBackpack.getItemCount("Bouquet")).thenReturn(-3);

        setupTiles(0, 0, 0, 1);

        Result result = controller.sendFlower("Bob");
        assertFalse(result.isSuccessful());
        assertEquals("You don't have a flower to send.", result.message());
    }

    @Test
    void sendFlower_success_level2_receiverFemale_levelUp() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(2);

        Backpack senderBackpack = mock(Backpack.class);
        Backpack receiverBackpack = mock(Backpack.class);
        when(currentPlayer.getBackpack()).thenReturn(senderBackpack);
        when(receiver.getBackpack()).thenReturn(receiverBackpack);
        when(senderBackpack.getItemCount("Bouquet")).thenReturn(1);

        randomStuff flower = new randomStuff(20, randomStuffType.Bouquet);
        when(senderBackpack.grabItemAndReturn("Bouquet", 1)).thenReturn(flower);
        when(receiverBackpack.addItem(any(), eq(1))).thenReturn(new Result(true, "Added"));

        setupTiles(0, 0, 0, 1);
        when(receiver.isGender()).thenReturn(true); // Female

        Result result = controller.sendFlower("Bob");

        assertTrue(result.isSuccessful());
        verify(mockFriendship).setLevel(3); // Level up
    }

    @Test
    void sendFlower_success_level3_receiverMale_noLevelUp() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(3);

        Backpack senderBackpack = mock(Backpack.class);
        Backpack receiverBackpack = mock(Backpack.class);
        when(currentPlayer.getBackpack()).thenReturn(senderBackpack);
        when(receiver.getBackpack()).thenReturn(receiverBackpack);
        when(senderBackpack.getItemCount("Bouquet")).thenReturn(1);

        randomStuff flower = new randomStuff(20, randomStuffType.Bouquet);
        when(senderBackpack.grabItemAndReturn("Bouquet", 1)).thenReturn(flower);
        when(receiverBackpack.addItem(any(), eq(1))).thenReturn(new Result(true, "Added"));

        setupTiles(0, 0, 1, 1);
        when(receiver.isGender()).thenReturn(false); // Male

        Result result = controller.sendFlower("Bob");

        assertTrue(result.isSuccessful());
        verify(mockFriendship, never()).setLevel(anyInt()); // No level change
    }


    @Test
    void sendFlower_receiverNull() {
        when(mockGame.getCurrentPlayer()).thenReturn(currentPlayer);
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(null);

        Result result = controller.sendFlower("Bob");

        assertFalse(result.isSuccessful());
        assertEquals("Receiver not found.", result.message());
    }

    @Test
    void sendFlower_friendshipNull() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(null);

        Result result = controller.sendFlower("Bob");

        assertFalse(result.isSuccessful());
        assertEquals("friendship not found.", result.message());
    }

    @Test
    void sendFlower_friendshipLevelTooLow() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(1);

        Result result = controller.sendFlower("Bob");

        assertFalse(result.isSuccessful());
        assertEquals("You don't have enough level!", result.message());
    }

    @Test
    void sendFlower_notAdjacent() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(2);

        setupTiles(0, 0, 10, 10); // Far away

        Result result = controller.sendFlower("Bob");

        assertFalse(result.isSuccessful());
        assertEquals("Players are not adjacent.", result.message());
    }

    @Test
    void sendFlower_grabReturnsNull() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(2);

        Backpack senderBackpack = mock(Backpack.class);
        when(currentPlayer.getBackpack()).thenReturn(senderBackpack);
        when(senderBackpack.getItemCount("Bouquet")).thenReturn(1);
        when(senderBackpack.grabItemAndReturn("Bouquet", 1)).thenReturn(null);

        setupTiles(0, 0, 0, 1);

        Result result = controller.sendFlower("Bob");

        assertFalse(result.isSuccessful());
        assertEquals("Failed to grab flower from inventory.", result.message());
    }

    @Test
    void sendFlower_receiverInventoryFull() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(2);

        Backpack senderBackpack = mock(Backpack.class);
        Backpack receiverBackpack = mock(Backpack.class);
        when(currentPlayer.getBackpack()).thenReturn(senderBackpack);
        when(receiver.getBackpack()).thenReturn(receiverBackpack);
        when(senderBackpack.getItemCount("Bouquet")).thenReturn(1);

        randomStuff flower = new randomStuff(20, randomStuffType.Bouquet);
        when(senderBackpack.grabItemAndReturn("Bouquet", 1)).thenReturn(flower);
        when(receiverBackpack.addItem(any(), eq(1)))
                .thenReturn(new Result(false, "Inventory full"));

        setupTiles(0, 0, 0, 1);

        Result result = controller.sendFlower("Bob");

        assertFalse(result.isSuccessful());
        assertEquals("Receiver's inventory is full.", result.message());
    }

    @Test
    void sendGift_success() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(2);
        setupTiles(0, 0, 0, 1);

        Backpack senderBackpack = mock(Backpack.class);
        Backpack receiverBackpack = mock(Backpack.class);
        Item item = mock(Item.class);
        when(item.copy()).thenReturn(item);
        when(item.getName()).thenReturn("Carrot");

        when(currentPlayer.getBackpack()).thenReturn(senderBackpack);
        when(senderBackpack.getItemCount("Carrot")).thenReturn(3);
        when(senderBackpack.grabItemAndReturn("Carrot", 2)).thenReturn(item);
        when(receiver.getBackpack()).thenReturn(receiverBackpack);
        when(receiverBackpack.addItem(item, 2)).thenReturn(new Result(true, "Added"));

        Result result = controller.sendGift("Bob", "Carrot", "2");

        verify(senderBackpack).grabItem("Carrot", 2);
        verify(mockFriendship).addToGifts(any());
        verify(receiver).addToNotifications(any());
        verify(receiver).addRecievedGift(any());
        assertTrue(result.isSuccessful());
        assertTrue(result.message().contains("Gift sent successfully"));
    }

    @Test
    void sendGift_nullEntities() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(null); // receiver null

        Result result = controller.sendGift("Bob", "Carrot", "1");

        assertFalse(result.isSuccessful());
        assertEquals("Sender or receiver or friendship not found.", result.message());
    }
    @Test
    void sendGift_lowFriendshipLevel() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(0);

        Result result = controller.sendGift("Bob", "Carrot", "1");

        assertFalse(result.isSuccessful());
        assertEquals("You don't have enough level!", result.message());
    }
    @Test
    void sendGift_notAdjacent() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(2);
        setupTiles(0, 0, 5, 5); // not adjacent

        Result result = controller.sendGift("Bob", "Carrot", "1");

        assertFalse(result.isSuccessful());
        assertEquals("Players are not adjacent.", result.message());
    }

    @Test
    void sendGift_notEnoughItems() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(2);
        setupTiles(0, 0, 0, 1);

        Backpack senderBackpack = mock(Backpack.class);
        when(currentPlayer.getBackpack()).thenReturn(senderBackpack);
        when(senderBackpack.getItemCount("Carrot")).thenReturn(0);

        Result result = controller.sendGift("Bob", "Carrot", "1");

        assertFalse(result.isSuccessful());
        assertEquals("Sender does not have enough of the specified item.", result.message());
    }
    @Test
    void sendGift_grabReturnsNull() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(2);
        setupTiles(0, 0, 0, 1);

        Backpack senderBackpack = mock(Backpack.class);
        when(currentPlayer.getBackpack()).thenReturn(senderBackpack);
        when(senderBackpack.getItemCount("Carrot")).thenReturn(2);
        when(senderBackpack.grabItemAndReturn("Carrot", 2)).thenReturn(null);

        Result result = controller.sendGift("Bob", "Carrot", "2");

        assertFalse(result.isSuccessful());
        assertEquals("Sender does not have enough of the specified item.", result.message());
    }
    @Test
    void sendGift_receiverInventoryFull() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(2);
        setupTiles(0, 0, 0, 1);

        Backpack senderBackpack = mock(Backpack.class);
        Backpack receiverBackpack = mock(Backpack.class);
        Item item = mock(Item.class);
        when(item.copy()).thenReturn(item);
        when(item.getName()).thenReturn("Carrot");

        when(currentPlayer.getBackpack()).thenReturn(senderBackpack);
        when(senderBackpack.getItemCount("Carrot")).thenReturn(2);
        when(senderBackpack.grabItemAndReturn("Carrot", 2)).thenReturn(item);
        when(receiver.getBackpack()).thenReturn(receiverBackpack);
        when(receiverBackpack.addItem(any(), eq(2))).thenReturn(new Result(false, "Inventory full"));

        Result result = controller.sendGift("Bob", "Carrot", "2");

        assertFalse(result.isSuccessful());
        assertEquals("Receiver inventory is full!", result.message());
    }


    @Test
    void sendGift_invalidAmount_throwsException() {
        assertThrows(NumberFormatException.class, () -> controller.sendGift("Bob", "Carrot", "abc"));
    }

    @Test
    void sendGift_senderReceiverFriendshipNull() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(null);

        Result result = controller.sendGift("Bob", "Carrot", "1");

        assertFalse(result.isSuccessful());
        assertEquals("Sender or receiver or friendship not found.", result.message());
    }

    @Test
    void sendGift_friendshipLevelTooLow() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(0);

        Result result = controller.sendGift("Bob", "Carrot", "1");

        assertFalse(result.isSuccessful());
        assertEquals("You don't have enough level!", result.message());
    }

    @Test
    void sendGift_notEnoughItemCount() {
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(1);
        setupTiles(0, 0, 0, 1);

        Backpack senderBackpack = mock(Backpack.class);
        when(currentPlayer.getBackpack()).thenReturn(senderBackpack);
        when(senderBackpack.getItemCount("Carrot")).thenReturn(0);

        Result result = controller.sendGift("Bob", "Carrot", "1");

        assertFalse(result.isSuccessful());
        assertEquals("Sender does not have enough of the specified item.", result.message());
    }

    @Test
    void rateGifts_noGiftsReceived() {
        when(mockGame.getCurrentPlayer()).thenReturn(currentPlayer);
        when(mockGame.getCurrentPlayer()).thenReturn(currentPlayer);
        ArrayList<Gift> gifts = new ArrayList<>();

        when(currentPlayer.getRecievedGift()).thenReturn(gifts);

        Result result = controller.rateGifts("1", "4");

        assertFalse(result.isSuccessful());
        assertEquals("No gifts received yet.", result.message());
    }

    @Test
    void rateGifts_invalidRatingTooLow() {
        when(mockGame.getCurrentPlayer()).thenReturn(currentPlayer);
        when(mockGame.getCurrentPlayer()).thenReturn(currentPlayer);
        Gift mockGift = mock(Gift.class);
        ArrayList<Gift> gifts = new ArrayList<>();
        gifts.add(mockGift);

        when(currentPlayer.getRecievedGift()).thenReturn(gifts);

        Result result = controller.rateGifts("1", "0");

        assertFalse(result.isSuccessful());
        assertEquals("Rating must be between 1 and 5.", result.message());
    }


    @Test
    void rateGifts_invalidRatingTooHigh() {
        // Arrange
        Gift mockGift = mock(Gift.class);
        ArrayList<Gift> gifts = new ArrayList<>();
        gifts.add(mockGift); // Add a mock gift to the received gifts list
        when(mockGame.getCurrentPlayer()).thenReturn(currentPlayer);
        when(currentPlayer.getRecievedGift()).thenReturn(gifts);

        // Act
        Result result = controller.rateGifts("1", "6"); // Invalid: rating too high

        // Assert
        assertFalse(result.isSuccessful());
        assertEquals("Rating must be between 1 and 5.", result.message());
    }


    @Test
    void rateGifts_invalidGiftNumber() {
        when(mockGame.getCurrentPlayer()).thenReturn(currentPlayer);
        Gift mockGift = mock(Gift.class);
        ArrayList<Gift> gifts = new ArrayList<>();
        gifts.add(mockGift);
        when(currentPlayer.getRecievedGift()).thenReturn(gifts);

        Result result = controller.rateGifts("2", "3");

        assertFalse(result.isSuccessful());
        assertEquals("No gift found with the specified gift number.", result.message());
    }

    @Test
    void rateGifts_noFriendshipFound() {
        // Arrange
        Gift mockGift = mock(Gift.class);
        when(mockGift.getSender()).thenReturn("Bob"); // Set sender for the gift
        when(mockGame.getCurrentPlayer()).thenReturn(currentPlayer);
        when(currentPlayer.getUsername()).thenReturn("Alice");

        // Setup the received gifts
        ArrayList<Gift> gifts = new ArrayList<>();
        gifts.add(mockGift); // Add the mock gift to the list of received gifts
        when(currentPlayer.getRecievedGift()).thenReturn(gifts);

        // Mock the receiver and friendship
        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(null); // No friendship

        // Act
        Result result = controller.rateGifts("1", "3"); // Invalid: no friendship

        // Assert
        assertFalse(result.isSuccessful());
        assertEquals("No friendship exists between you and the sender.", result.message());
    }




    @Test
    void rateGifts_withoutPartner_appliesCalculatedXp() {
        // Arrange
        Gift mockGift = mock(Gift.class);
        when(mockGift.getSender()).thenReturn("Bob"); // Missing in original
        when(mockGame.getCurrentPlayer()).thenReturn(currentPlayer);
        when(currentPlayer.getUsername()).thenReturn("Alice");

        ArrayList<Gift> gifts = new ArrayList<>();
        gifts.add(mockGift);
        when(currentPlayer.getRecievedGift()).thenReturn(gifts);

        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(receiver.getUsername()).thenReturn("Bob");
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(1);
        when(currentPlayer.getPartner()).thenReturn(null);  // No partner

        // Act
        Result result = controller.rateGifts("1", "4");

        // Assert
        verify(mockFriendship).addXp(45);  // XP calculation: 15 + 30 * (4 - 3)
        verify(mockGift).setRate(4);       // Set rating to 4
        assertTrue(result.isSuccessful());
    }
    @Test
    void rateGifts_successfulWithPartner_appliesFixedXp() {
        // Arrange
        Gift mockGift = mock(Gift.class);
        when(mockGift.getSender()).thenReturn("Bob");

        ArrayList<Gift> giftList = new ArrayList<>();
        giftList.add(mockGift);

        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(currentPlayer.getRecievedGift()).thenReturn(giftList);

        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(receiver.getUsername()).thenReturn("Bob");
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(2);

        // Simulate partner relationship
        when(currentPlayer.getPartner()).thenReturn(receiver);

        // Act
        Result result = controller.rateGifts("1", "5");

        // Assert
        verify(mockFriendship).addXp(50); // Fixed XP for partner
        verify(mockGift).setRate(5);
        assertTrue(result.isSuccessful());
        assertEquals("Gift rated successfully. Friendship level adjusted.", result.message());
    }


    @Test
    void rateGifts_friendshipTooLow() {
        // Arrange
        Gift mockGift = mock(Gift.class);
        when(mockGift.getSender()).thenReturn("Bob"); // This line is crucial

        when(mockGame.getCurrentPlayer()).thenReturn(currentPlayer);
        when(currentPlayer.getUsername()).thenReturn("Alice");

        ArrayList<Gift> gifts = new ArrayList<>();
        gifts.add(mockGift);
        when(currentPlayer.getRecievedGift()).thenReturn(gifts);

        when(mockGame.getPlayerByUsername("Bob")).thenReturn(receiver);
        when(receiver.getUsername()).thenReturn("Bob");

        // Friendship level too low
        when(mockGame.getFriendship("Alice", "Bob")).thenReturn(mockFriendship);
        when(mockFriendship.getLevel()).thenReturn(0);

        // Act
        Result result = controller.rateGifts("1", "3");

        // Assert
        assertFalse(result.isSuccessful());
        assertEquals("You don't have enough level!", result.message());
    }


    Tile createTile(int x, int y) {
        Tile tile = mock(Tile.class);
        when(tile.getX()).thenReturn(x);
        when(tile.getY()).thenReturn(y);
        return tile;
    }

    @Test
    void isAdjacent_sameTile_returnsTrue() {
        Tile t = createTile(2, 3);
        assertTrue(controller.isAdjacent(t, t)); // Accepts self as adjacent
    }

    @Test
    void isAdjacent_directlyHorizontal_returnsTrue() {
        Tile t1 = createTile(2, 3);
        Tile t2 = createTile(3, 3);
        assertTrue(controller.isAdjacent(t1, t2));
    }

    @Test
    void isAdjacent_directlyVertical_returnsTrue() {
        Tile t1 = createTile(2, 3);
        Tile t2 = createTile(2, 4);
        assertTrue(controller.isAdjacent(t1, t2));
    }

    @Test
    void isAdjacent_diagonal_returnsTrue() {
        Tile t1 = createTile(2, 3);
        Tile t2 = createTile(3, 4);
        assertTrue(controller.isAdjacent(t1, t2));
    }

    @Test
    void isAdjacent_farApart_returnsFalse() {
        Tile t1 = createTile(1, 1);
        Tile t2 = createTile(4, 4);
        assertFalse(controller.isAdjacent(t1, t2));
    }

    @Test
    void isAdjacent_farApartXOnly_returnsFalse() {
        Tile t1 = createTile(1, 1);
        Tile t2 = createTile(3, 1);
        assertFalse(controller.isAdjacent(t1, t2));
    }

    @Test
    void isAdjacent_farApartYOnly_returnsFalse() {
        Tile t1 = createTile(1, 1);
        Tile t2 = createTile(1, 3);
        assertFalse(controller.isAdjacent(t1, t2));
    }
}

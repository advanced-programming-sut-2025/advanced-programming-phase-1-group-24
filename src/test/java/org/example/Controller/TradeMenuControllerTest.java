package org.example.Controller;

import org.example.Model.*;
import org.example.Model.Friendships.Friendship;
import org.example.Model.Friendships.Message;
import org.example.Model.Friendships.Trade;
import org.example.Model.Menus.Menu;
import org.example.Model.Things.Backpack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TradeMenuControllerTest {

    private TradeMenuController controller;
    private App appMock;
    private Game gameMock;
    private User currentPlayer;
    private User targetUser;
    private Backpack playerBackpack;
    private ArrayList<Trade> tradingHistory;
    private ArrayList<Message> tradeNotifications;

    @BeforeEach
    void setUp() {
        controller = new TradeMenuController();
        appMock = mock(App.class);
        gameMock = mock(Game.class);
        currentPlayer = mock(User.class);
        targetUser = mock(User.class);
        playerBackpack = mock(Backpack.class);
        tradingHistory = new ArrayList<>();
        tradeNotifications = new ArrayList<>();

        App.setInstance(appMock);
        when(appMock.getCurrentGame()).thenReturn(gameMock);
        when(gameMock.getCurrentPlayer()).thenReturn(currentPlayer);
        when(currentPlayer.getBackpack()).thenReturn(playerBackpack);
        when(targetUser.getTradingHistory()).thenReturn(tradingHistory);
        when(targetUser.getTradeNotifications()).thenReturn(tradeNotifications);
    }

    @Test
    void offerTrade_success() {
        when(appMock.getUserByUsername("target")).thenReturn(targetUser);
        when(playerBackpack.hasItem("apple", 3)).thenReturn(true);

        Result result = controller.offerTrade("target", "offer", "apple", 3, 50);
        assertTrue(result.isSuccessful());
        assertEquals("Trade has been successfully placed!", result.message());
        assertEquals(1, tradingHistory.size());
        assertEquals(1, tradeNotifications.size());
    }

    @Test
    void offerTrade_invalidUser() {
        when(appMock.getUserByUsername("target")).thenReturn(null);
        Result result = controller.offerTrade("target", "offer", "apple", 3, 50);
        assertFalse(result.isSuccessful());
    }

    @Test
    void offerTrade_invalidType() {
        when(appMock.getUserByUsername("target")).thenReturn(targetUser);
        when(playerBackpack.hasItem("apple", 3)).thenReturn(true);
        Result result = controller.offerTrade("target", "invalid", "apple", 3, 50);
        assertFalse(result.isSuccessful());
    }

    @Test
    void requestTrade_success() {
        when(appMock.getUserByUsername("target")).thenReturn(targetUser);
        when(playerBackpack.hasItem("banana", 5)).thenReturn(true);

        Result result = controller.requestTrade("target", "request", "banana", 5, "gold", 2);
        assertTrue(result.isSuccessful());
        assertEquals("Trade has been successfully placed!", result.message());
    }

    @Test
    void showTradeList_noTrades() {
        when(currentPlayer.getTradingHistory()).thenReturn(new ArrayList<>());
        Result result = controller.showTradeList();
        assertTrue(result.isSuccessful());
        assertTrue(result.message().contains("no trades"));
    }

    @Test
    void tradeResponse_accept_offer_success() {
        Trade trade = new Trade(currentPlayer, targetUser, "offer", 2, "apple", null, 0, 10, false);
        trade.setId(101);
        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(trade);

        when(currentPlayer.getTradingHistory()).thenReturn(trades);
        when(gameMock.getFriendship(currentPlayer.getUsername(), targetUser.getUsername()))
                .thenReturn(mock(Friendship.class));
        when(targetUser.getMoney()).thenReturn(20);

        Result result = controller.tradeResponse("accept", 101);
        assertTrue(result.isSuccessful());
    }

    @Test
    void tradeResponse_reject_success() {
        Trade trade = new Trade(currentPlayer, targetUser, "offer", 1, "apple", null, 0, 10, false);
        trade.setId(200);
        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(trade);
        when(currentPlayer.getTradingHistory()).thenReturn(trades);
        Friendship friendship = mock(Friendship.class);
        when(gameMock.getFriendship(currentPlayer.getUsername(), targetUser.getUsername()))
                .thenReturn(friendship);

        Result result = controller.tradeResponse("reject", 200);
        assertFalse(result.isSuccessful());
        assertEquals("Trade has been successfully rejected.", result.message());
    }

    @Test
    void tradeResponse_invalidId() {
        when(currentPlayer.getTradingHistory()).thenReturn(new ArrayList<>());
        Result result = controller.tradeResponse("accept", 999);
        assertFalse(result.isSuccessful());
        assertEquals("There is no trade with the given ID.", result.message());
    }

    @Test
    void tradeResponse_invalidResponse() {
        Trade trade = new Trade(currentPlayer, targetUser, "offer", 1, "apple", null, 0, 10, false);
        trade.setId(300);
        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(trade);
        when(currentPlayer.getTradingHistory()).thenReturn(trades);
        Result result = controller.tradeResponse("maybe", 300);
        assertFalse(result.isSuccessful());
    }

    @Test
    void tradeResponse_accept_request_success() {
        Trade trade = new Trade(currentPlayer, targetUser, "request", 2, "apple", "banana", 1, 0, false);
        trade.setId(400);
        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(trade);

        // Mocking the current player's trade history
        when(currentPlayer.getTradingHistory()).thenReturn(trades);

        // Friendship mock
        Friendship friendship = mock(Friendship.class);
        when(gameMock.getFriendship(currentPlayer.getUsername(), targetUser.getUsername()))
                .thenReturn(friendship);

        // Receiver's backpack has the item
        Backpack receiverBackpack = mock(Backpack.class);
        when(targetUser.getBackpack()).thenReturn(receiverBackpack);
        when(receiverBackpack.hasItem("banana", 1)).thenReturn(true);

        // Sender backpack mock
        Backpack senderBackpack = mock(Backpack.class);
        when(currentPlayer.getBackpack()).thenReturn(senderBackpack);

        // Perform the trade
        Result result = controller.tradeResponse("accept", 400);
        assertTrue(result.isSuccessful());
        assertEquals("Trade has been successfully placed!", result.message());
    }
    @Test
    void tradeResponse_accept_request_insufficientItems() {
        Trade trade = new Trade(currentPlayer, targetUser, "request", 2, "apple", "banana", 1, 0, false);
        trade.setId(401);
        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(trade);

        when(currentPlayer.getTradingHistory()).thenReturn(trades);
        Friendship friendship = mock(Friendship.class);
        when(gameMock.getFriendship(currentPlayer.getUsername(), targetUser.getUsername()))
                .thenReturn(friendship);

        Backpack receiverBackpack = mock(Backpack.class);
        when(targetUser.getBackpack()).thenReturn(receiverBackpack);
        when(receiverBackpack.hasItem("banana", 1)).thenReturn(false);

        Result result = controller.tradeResponse("accept", 401);
        assertFalse(result.isSuccessful());
        assertEquals("You don't have enough items.", result.message());
    }

    @Test
    void tradeResponse_accept_offer_insufficientMoney() {
        // Setup a trade of type "offer"
        Trade trade = new Trade(currentPlayer, targetUser, "offer", 1, "apple", null, 0, 50, false);
        trade.setId(402);
        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(trade);

        // Mock current player's trade history
        when(currentPlayer.getTradingHistory()).thenReturn(trades);

        // Friendship mock
        Friendship friendship = mock(Friendship.class);
        when(gameMock.getFriendship(currentPlayer.getUsername(), targetUser.getUsername()))
                .thenReturn(friendship);

        // Receiver doesn't have enough money
        when(targetUser.getMoney()).thenReturn(20);  // Less than requestedPrice (50)

        // Perform the trade
        Result result = controller.tradeResponse("accept", 402);

        // Assert failure due to insufficient money
        assertFalse(result.isSuccessful());
        assertEquals("You don't have enough money.", result.message());
    }



    @Test
    void showTradeHistory_showsCorrectOutput() {
        Trade trade = new Trade(currentPlayer, targetUser, "offer", 2, "apple", null, 0, 10, true);
        trade.setId(1);
        trade.setAccept(true);
        trade.setHasBeenAnswered(true);
        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(trade);
        when(currentPlayer.getTradingHistory()).thenReturn(trades);

        Result result = controller.showTradeHistory();
        assertTrue(result.isSuccessful());
        assertTrue(result.message().contains("Trade ID: 1"));
    }

    @Test
    void exitTrade_setsMenu() {
        controller.exitTrade();
        verify(appMock).setCurrentMenu(Menu.GameMenu);
    }

    @Test
    void findTradeByID_findsCorrectTrade() {
        Trade t = new Trade(currentPlayer, targetUser, "offer", 1, "apple", null, 0, 10, false);
        t.setId(555);
        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(t);
        when(currentPlayer.getTradingHistory()).thenReturn(trades);

        Trade found = controller.findTradeByID(555);
        assertNotNull(found);
        assertEquals(555, found.getId());
    }

    @Test
    void showTradeList_withTrades_displaysCorrectInfo() {
        // Create a trade and set necessary properties
        Trade trade = new Trade(currentPlayer, targetUser, "offer", 2, "apple", null, 0, 15, false);
        trade.setId(101); // assuming setId is public or done through constructor
        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(trade);

        // Mock the trading history
        when(currentPlayer.getTradingHistory()).thenReturn(trades);

        // Act
        Result result = controller.showTradeList();

        // Assert
        assertTrue(result.isSuccessful());
        String message = result.message();
        assertTrue(message.contains("Your trade history:"));
        assertTrue(message.contains("Trade ID: 101"));
        assertTrue(message.contains("Sender: " + currentPlayer.getUsername()));
        assertTrue(message.contains("Recipient: " + targetUser.getUsername()));
        assertTrue(message.contains("Trade Type: offer"));
        assertTrue(message.contains("Offered: 2 x apple"));
        assertTrue(message.contains("Requested Price: 15 coins"));
    }
    @Test
    void offerTrade_notEnoughItems() {
        // Arrange: Setup the mock for the current player
        when(appMock.getUserByUsername("target")).thenReturn(targetUser);
        when(playerBackpack.hasItem("apple", 3)).thenReturn(false);  // The player doesn't have enough apples

        // Act: Try to offer a trade with insufficient items
        Result result = controller.offerTrade("target", "offer", "apple", 3, 50);

        // Assert: Check if the trade fails with the appropriate message
        assertFalse(result.isSuccessful());
        assertEquals("You don't have enough items.", result.message());
    }
    @Test
    void showTradeList_offerTradeFormatDisplayedCorrectly() {
        Trade offerTrade = mock(Trade.class);
        when(offerTrade.isHasBeenAnswered()).thenReturn(false);
        when(offerTrade.getId()).thenReturn(1);
        when(offerTrade.getSender()).thenReturn(currentPlayer);
        when(offerTrade.getRecipient()).thenReturn(targetUser);
        when(offerTrade.getTypeOfTrade()).thenReturn("offer");
        when(offerTrade.getAmountOfOfferedItems()).thenReturn(3);
        when(offerTrade.getOfferedItem()).thenReturn("Apple");
        when(offerTrade.getRequestedPrice()).thenReturn(50);

        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(offerTrade);
        when(currentPlayer.getTradingHistory()).thenReturn(trades);
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(targetUser.getUsername()).thenReturn("Bob");

        Result result = controller.showTradeList();

        assertTrue(result.isSuccessful());
        String msg = result.message();
        assertTrue(msg.contains("Trade Type: offer"));
        assertTrue(msg.contains("Offered: 3 x Apple"));
        assertTrue(msg.contains("Requested Price: 50 coins"));
    }
    @Test
    void showTradeList_requestTradeFormatDisplayedCorrectly() {
        Trade requestTrade = mock(Trade.class);
        when(requestTrade.isHasBeenAnswered()).thenReturn(false);
        when(requestTrade.getId()).thenReturn(2);
        when(requestTrade.getSender()).thenReturn(currentPlayer);
        when(requestTrade.getRecipient()).thenReturn(targetUser);
        when(requestTrade.getTypeOfTrade()).thenReturn("request");
        when(requestTrade.getAmountOfOfferedItems()).thenReturn(2);
        when(requestTrade.getOfferedItem()).thenReturn("Wool");
        when(requestTrade.getAmountOfRequestedItem()).thenReturn(1);
        when(requestTrade.getRequestedItem()).thenReturn("Egg");

        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(requestTrade);
        when(currentPlayer.getTradingHistory()).thenReturn(trades);
        when(currentPlayer.getUsername()).thenReturn("Alice");
        when(targetUser.getUsername()).thenReturn("Bob");

        Result result = controller.showTradeList();

        assertTrue(result.isSuccessful());
        String msg = result.message();
        assertTrue(msg.contains("Trade Type: request"));
        assertTrue(msg.contains("Offered: 2 x Wool"));
        assertTrue(msg.contains("Requested: 1 x Egg"));
    }
    @Test
    void showTradeList_noTrades_returnsMessage() {
        when(currentPlayer.getTradingHistory()).thenReturn(new ArrayList<>());

        Result result = controller.showTradeList();

        assertTrue(result.isSuccessful());
        assertEquals("You have no trades in your history.", result.message());
    }
    @Test
    void showTradeList_answeredTradeSkipped() {
        Trade answeredTrade = mock(Trade.class);
        when(answeredTrade.isHasBeenAnswered()).thenReturn(true);

        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(answeredTrade);
        when(currentPlayer.getTradingHistory()).thenReturn(trades);

        Result result = controller.showTradeList();

        assertTrue(result.isSuccessful());
        assertEquals("Your trade history:\n", result.message());
    }
    @Test
    void requestTrade_invalidUser_returnsError() {
        // Arrange
        when(appMock.getUserByUsername("unknownUser")).thenReturn(null);

        // Act
        Result result = controller.requestTrade("unknownUser", "request", "banana", 5, "gold", 2);

        // Assert
        assertFalse(result.isSuccessful());
        assertEquals("There is no user with the given username.", result.message());
    }
    @Test
    void requestTrade_notEnoughItems_returnsError() {
        // Arrange
        when(appMock.getUserByUsername("target")).thenReturn(targetUser);
        when(playerBackpack.hasItem("banana", 5)).thenReturn(false); // Player lacks items

        // Act
        Result result = controller.requestTrade("target", "request", "banana", 5, "gold", 2);

        // Assert
        assertFalse(result.isSuccessful());
        assertEquals("You don't have enough items.", result.message());
    }
    @Test
    void requestTrade_invalidTradeType_returnsError() {
        // Arrange
        when(appMock.getUserByUsername("target")).thenReturn(targetUser);
        when(playerBackpack.hasItem("banana", 5)).thenReturn(true);

        // Act
        Result result = controller.requestTrade("target", "offer", "banana", 5, "gold", 2); // Invalid type

        // Assert
        assertFalse(result.isSuccessful());
        assertEquals("This is not the right trade type.", result.message());
    }

    @Test
    void showTradeHistory_whenEmpty_returnsNoTradesMessage() {
        when(currentPlayer.getTradingHistory()).thenReturn(new ArrayList<>());

        Result result = controller.showTradeHistory();

        assertTrue(result.isSuccessful());
        assertEquals("You have no trades in your history.", result.message());
    }
    @Test
    void showTradeHistory_withRequestTrade_includesRequestedItemLine() {
        // Arrange
        Trade trade = new Trade(currentPlayer, targetUser, "request", 3, "wheat", "carrot", 2, 0, false);
        trade.setId(7);
        trade.setAccept(false);
        trade.setHasBeenAnswered(false);

        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(trade);

        when(currentPlayer.getTradingHistory()).thenReturn(trades);

        // Act
        Result result = controller.showTradeHistory();

        // Assert
        assertTrue(result.isSuccessful());
        assertTrue(result.message().contains("Trade ID: 7"));
        assertTrue(result.message().contains("Requested: 2 x carrot"));
    }

}

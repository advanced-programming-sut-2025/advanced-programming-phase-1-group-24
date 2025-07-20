package io.github.stardew.mini.client;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.stardew.mini.Model.LobbyInfo;
import io.github.stardew.mini.client.View.LobbyMenuView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyMenuController {
    private LobbyMenuView view;
    private NetworkClient networkService; // handles HTTP/WebSocket communication

    public LobbyMenuController() {
        this.networkService = MainApp.getInstance().getNetworkClient();
    }

    public void setView(LobbyMenuView view) {
        this.view = view;
    }

    public void createLobby(String name, String password, boolean isPrivate, boolean invisible) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("isPrivate", isPrivate);
        params.put("password", password);
        params.put("isInvisible", invisible);
        String gameId = null;
        String controllerName = "LobbyController";
        String methodName = "createLobby";
        String username = MainApp.getInstance().getLoggedInUser().getUsername();

        MainApp.getInstance().getNetworkClient()
            .sendPost(gameId, controllerName, methodName, params, username)
            .thenAccept(response -> {
                if (response.getStatus() == 200) {
                    System.out.println("✅ Lobby created successfully!");
                    refreshLobbies();
                } else {
                    System.err.println("❌ Failed to create lobby: " + response.getMessage());
                    Gdx.app.postRunnable(() -> view.showErrorDialog(view.getStage(), "Failed to create lobby: " + response.getMessage()));
                }
            })
            .exceptionally(ex -> {
                System.err.println("❌ Error sending createLobby request: " + ex.getMessage());
                return null;
            });

    }

    public void refreshLobbies() {
        Map<String, Object> params = new HashMap<>();
        String gameId = null;
        String controllerName = "LobbyController";
        String methodName = "getAllLobbies";
        String username = MainApp.getInstance().getLoggedInUser().getUsername();

        MainApp.getInstance().getNetworkClient()
            .sendGet(gameId, controllerName, methodName, params, username)
            .thenAccept(response -> {
                if (response.getStatus() == 200) {
                   Object body = response.getBody();
                   if(body instanceof Map<?,?>) {
                       Map<String, Object> bodyMap = (Map<String, Object>) body;
                       Object lobbies = bodyMap.get("lobbies");
                       if(lobbies != null) {
                           Gson gson = new Gson();
                           String json = gson.toJson(lobbies);
                           Type listType = new TypeToken<List<LobbyInfo>>() {}.getType();
                           List<LobbyInfo> lobbiesList = gson.fromJson(json, listType);
                           System.out.println("lobbies found");
                           Gdx.app.postRunnable(() -> view.updateLobbyList(lobbiesList));
                       }
                       else{
                           System.err.println("❌ Failed to fetch lobbies: " + response.getMessage());
                           Gdx.app.postRunnable(() -> view.showErrorDialog(view.getStage(),"No lobby yet!"));
                       }
                   }
                } else {
                    System.err.println("❌ Failed to fetch lobbies: " + response.getMessage());
                    Gdx.app.postRunnable(() -> view.showErrorDialog(view.getStage(),"Failed to get lobby list: " + response.getMessage()));
                }
            })
            .exceptionally(ex -> {
                System.err.println("❌ Error fetching lobbies: " + ex.getMessage());
                return null;
            });

    }

    public void joinLobby(String lobbyId, boolean isPrivate) {
        String password = "";

        if (isPrivate) {
            password = LobbyMenuView.PasswordPrompt.ask();
            if (password == null || password.isEmpty()) {
                view.showErrorDialog(view.getStage(), "Password is required to join this lobby.");
                return;
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put("lobbyId", lobbyId);
        if (isPrivate) {
            params.put("password", password);
        }

        String gameId = null;
        String controllerName = "LobbyController";
        String methodName = "joinLobby";
        String username = MainApp.getInstance().getLoggedInUser().getUsername();

        MainApp.getInstance().getNetworkClient()
            .sendPost(gameId, controllerName, methodName, params, username)
            .thenAccept(response -> {
                if (response.getStatus() == 200) {
                    System.out.println("✅ Joined lobby successfully!");
                    Gdx.app.postRunnable(() -> {
                        view.showErrorDialog(view.getStage(), "Joined lobby successfully!");
                    });
                } else {
                    System.err.println("❌ Failed to join lobby: " + response.getMessage());
                    Gdx.app.postRunnable(() -> view.showErrorDialog(view.getStage(), "Failed to join lobby: " + response.getMessage()));
                }
            })
            .exceptionally(ex -> {
                System.err.println("❌ Error joining lobby: " + ex.getMessage());
                Gdx.app.postRunnable(() -> view.showErrorDialog(view.getStage(), "Error joining lobby: " + ex.getMessage()));
                return null;
            });
    }

    public void leaveLobby(String lobbyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("lobbyId", lobbyId);
        String gameId = null;
        String controllerName = "LobbyController";
        String methodName = "leaveLobby";
        String username = MainApp.getInstance().getLoggedInUser().getUsername();
        MainApp.getInstance().getNetworkClient().sendPost(gameId, controllerName, methodName, params, username)
            .thenAccept(response -> {
                if (response.getStatus() == 200) {
                    System.out.println("Left lobby successfully!");
                }
                else {
                    Gdx.app.postRunnable(() -> view.showErrorDialog(view.getStage(), "Failed to join lobby: " + response.getMessage()));
                }
            }).exceptionally(ex -> {
                System.err.println("❌ Error joining lobby: " + ex.getMessage());
                Gdx.app.postRunnable(() -> view.showErrorDialog(view.getStage(), "Error leaving the lobby: " + ex.getMessage()));
                return null;
            });
    }

    public void searchLobbyById(String id) {
        String gameId = null;
        String controllerName = "LobbyController";
        String methodName = "searchLobbyById";
        String username = MainApp.getInstance().getLoggedInUser().getUsername();
        Map<String, Object> params = new HashMap<>();
        params.put("lobbyID", id);
        MainApp.getInstance().getNetworkClient()
            .sendGet(gameId, controllerName, methodName, params, username)
            .thenAccept(response -> {
                if (response.getStatus() == 200) {
                    Object body = response.getBody();
                    if (body instanceof Map<?, ?> bodyMap) {
                        Object lobbyData = bodyMap.get("lobby");
                        if (lobbyData instanceof Map) {
                            Gson gson = new Gson();
                            LobbyInfo lobby = gson.fromJson(gson.toJson(lobbyData), LobbyInfo.class);
                            if (lobby != null && lobby.isInvisible()) {
                                System.out.println("lobby found");
                                Gdx.app.postRunnable(() -> view.updateLobbyList(List.of(lobby)));
                            } else {
                                System.err.println("❌ Failed to fetch lobby: lobby is null or visible");
                                Gdx.app.postRunnable(() -> view.showErrorDialog(view.getStage(), "No lobby found with the given ID!"));
                            }
                        }
                    }
                } else {
                    System.err.println("❌ Failed to fetch lobby: " + response.getMessage());
                    Gdx.app.postRunnable(() -> view.showErrorDialog(view.getStage(),"Failed to get lobby: " + response.getMessage()));
                }
            })
            .exceptionally(ex -> {
                System.err.println("❌ Error fetching lobby: " + ex.getMessage());
                return null;
            });
    }

}

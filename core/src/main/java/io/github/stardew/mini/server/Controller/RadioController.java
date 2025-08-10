package io.github.stardew.mini.server.Controller;

import io.github.stardew.mini.Model.AudioTrack;
import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.server.GameServer;
import io.github.stardew.mini.server.RadioService;
import io.github.stardew.mini.server.ServerApp;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RadioController {
    private final RadioService radio = ServerApp.getInstance().getRadioService();

    public Message<AudioTrack> upload(Map<String, Object> body) {
        String user = (String) body.get("ownerUsername");
        String name = (String) body.get("name");
        byte[] data = Base64.getDecoder().decode((String) body.get("data"));
        AudioTrack t = new AudioTrack(user, name, data);
        radio.uploadTrack(t);
        return Message.ok(t);
    }

    @SuppressWarnings("unchecked")
    public Message<List<AudioTrack>> listUserTracks(Map<String, Object> body) {
        String user = (String) body.get("ownerUsername");
        List<AudioTrack> list = radio.listTracks(user);
        return Message.ok(list);
    }

    public Message<Void> switchTrack(Map<String, Object> body, GameServer gs) {
        String trackId = (String) body.get("trackId");
        radio.setCurrentTrack(gs.getGame().getNetworkId(), trackId);
        // broadcast new track data
        AudioTrack t = radio.getCurrentTrack(gs.getGame().getNetworkId());
        if (t != null) {
            String b64 = Base64.getEncoder().encodeToString(t.getData());
            Map<String, Object> payload = new HashMap<>();
            payload.put("trackId", t.getId());
            payload.put("name", t.getName());
            payload.put("data", b64);
            Message<Map<String,Object>> msg = Message.ok(payload);
            msg.setType("radio-update");
            String json = ServerApp.getInstance().getGson().toJson(msg);
            for (var pc : gs.getPlayers()) {
                pc.getWsContext().send(json);
            }
        }
        return Message.ok(null);
    }

    public static Message<?> route(String method, Map<String,Object> body, GameServer gs) {
        switch(method) {
            case "upload":   return new RadioController().upload(body);
            case "list":     return new RadioController().listUserTracks(body);
            case "switch":   return new RadioController().switchTrack(body, gs);
            default:         return Message.NOT_FOUND.setMessage("Unknown radio method");
        }
    }

}

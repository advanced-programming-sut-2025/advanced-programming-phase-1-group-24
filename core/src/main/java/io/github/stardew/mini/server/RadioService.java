package io.github.stardew.mini.server;

import io.github.stardew.mini.common.Model.AudioTrack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RadioService {
    private final ConcurrentHashMap<String, AudioTrack> tracks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> currentTrack = new ConcurrentHashMap<>();

    private final Map<String,List<AudioTrack>> userPlaylists = new ConcurrentHashMap<>();
    private final Map<String,String> gameCurrentTrack = new ConcurrentHashMap<>();


    public AudioTrack uploadTrack(AudioTrack t) {
        tracks.put(t.getId(), t);
        return t;
    }

    public List<AudioTrack> listTracks(String owner) {
        List<AudioTrack> out = new ArrayList<>();
        for (AudioTrack t : tracks.values()) {
            if (t.getOwnerUsername().equals(owner)) out.add(t);
        }
        return out;
    }

    public void setCurrentTrack(String gameId, String trackId) {
        if (tracks.containsKey(trackId)) {
            currentTrack.put(gameId, trackId);
        }
    }

    public AudioTrack getCurrentTrack(String gameId) {
        String tid = currentTrack.get(gameId);
        return tid == null ? null : tracks.get(tid);
    }
}


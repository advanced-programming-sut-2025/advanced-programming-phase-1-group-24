package io.github.stardew.mini.server;

import io.github.stardew.mini.common.Model.AudioTrack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class RadioService {
    private final ConcurrentHashMap<String, AudioTrack> tracks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> currentTrack = new ConcurrentHashMap<>();

    // use CopyOnWriteArrayList for thread-safety and addIfAbsent
    private final Map<String, CopyOnWriteArrayList<AudioTrack>> userPlaylists = new ConcurrentHashMap<>();

    public AudioTrack uploadTrack(AudioTrack t) {
        tracks.put(t.getId(), t);
        // add to uploader's playlist (thread-safe, no duplicates)
        addTrackToUser(t.getOwnerUsername(), t);
        return t;
    }

    public List<AudioTrack> listTracks(String owner) {
        if (owner == null) return new ArrayList<>();
        CopyOnWriteArrayList<AudioTrack> list = userPlaylists.get(owner);
        if (list != null) return new ArrayList<>(list);
        // fallback: scan global tracks with ownerUsername equals owner
        List<AudioTrack> fallback = new ArrayList<>();
        for (AudioTrack tr : tracks.values()) {
            if (owner.equals(tr.getOwnerUsername())) fallback.add(tr);
        }
        return fallback;
    }

    public void setCurrentTrack(String gameId, String trackId) {
        if (trackId != null && tracks.containsKey(trackId)) {
            currentTrack.put(gameId, trackId);
        }
    }

    public void addTrackToUser(String username, AudioTrack t) {
        if (username == null || t == null) return;
        userPlaylists
            .computeIfAbsent(username, k -> new CopyOnWriteArrayList<>())
            .addIfAbsent(t); // CopyOnWriteArrayList has addIfAbsent
    }

    public void addTrackToUsers(Collection<String> usernames, AudioTrack t) {
        if (usernames == null || t == null) return;
        for (String u : usernames) addTrackToUser(u, t);
    }

    public AudioTrack getCurrentTrack(String gameId) {
        String tid = currentTrack.get(gameId);
        return tid == null ? null : tracks.get(tid);
    }
}

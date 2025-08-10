package io.github.stardew.mini.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;


public class GameAudioManager {
    private static GameAudioManager instance;

    private Music currentMusic;
    private final HashMap<String, Sound> sounds = new HashMap<>();

    private GameAudioManager() {
    }

    public static GameAudioManager getInstance() {
        if (instance == null) {
            instance = new GameAudioManager();
        }
        return instance;
    }

    //    public void playMusic(String path, boolean loop, float volume) {
//        if (currentMusic != null) currentMusic.stop();
//        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
//        currentMusic.setLooping(loop);
//        currentMusic.setVolume(volume);
//        currentMusic.play();
//    }
// new overload
    public void playMusic(FileHandle fh, boolean loop, float volume) {
        try {
            if (currentMusic != null) {
                currentMusic.stop();
                currentMusic.dispose();
            }
            currentMusic = Gdx.audio.newMusic(fh);

            currentMusic.setLooping(loop);
            currentMusic.setVolume(volume);


            currentMusic.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music m) {
                    m.dispose();
                    System.out.println("Playback finished and disposed: " + fh.name());
                }
            });

            currentMusic.play();
        } catch (GdxRuntimeException e) {
            System.err.println("GameAudioManager.playMusic(FileHandle) failed: " + e.getMessage());
            throw e;
        }
    }


    // keep string overload for existing assets-based calls
    public void playMusic(String path, boolean loop, float volume) {
        playMusic(Gdx.files.internal(path), loop, volume);
    }


    public void stopMusic() {
        if (currentMusic != null) {
            try {
                currentMusic.stop();
                currentMusic.dispose();
            } catch (Exception ignored) {}
            currentMusic = null;
        }
    }

    public void pauseMusic() {
        if (currentMusic != null) currentMusic.pause();
    }

    public void resumeMusic() {
        if (currentMusic != null) currentMusic.play();
    }

    public void playSound(String path) {
        Sound sfx = sounds.get(path);
        if (sfx == null) {
            sfx = Gdx.audio.newSound(Gdx.files.internal(path));
            sounds.put(path, sfx);
        }
        sfx.play();
    }

    public void dispose() {
        if (currentMusic != null) currentMusic.dispose();
        for (Sound s : sounds.values()) {
            s.dispose();
        }
    }
}

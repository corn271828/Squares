package squares.api;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class AudioManager {
    private Map<String, ExamplePlayer> examplePlayers = new HashMap<>();
    ExamplePlayer current;
    String currString;
    public boolean running;

    public AudioManager addClip(String name, String path) {
        ExamplePlayer ep = new ExamplePlayer(path);
        examplePlayers.put(name, ep);
        return this;
    }
    public ExamplePlayer getClip(String name) {
        return name == null ? current : examplePlayers.getOrDefault(name, null);
    }
    public AudioManager setPlaying(String name) {
        return setPlaying(name, 0);
    }
    public AudioManager setPlaying(String name, long mspt) {
        if (examplePlayers.get(name).equals(current))
            return this;
        return restartPlaying(name, mspt);
    }
    public AudioManager restartPlaying(String name, long mspt) {
        running = false;
        if(current != null) {
            current.setStop();
            try {
                current.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(AudioManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            examplePlayers.put(currString, new ExamplePlayer(current.path));
        }
        currString = name;
        current = examplePlayers.getOrDefault(name, null);
        if(current != null) {
            //current.setMicrosecondPosition(mspt);
            current.start();
            running = true;
        }
        return this;
    }
}

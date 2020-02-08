package squares;

import java.util.Map;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import squares.api.ResourceLocator;

public class AudioManager {
    private Map<String, Clip> clips = new HashMap<>();
    Clip current;

    public AudioManager addClip(String name, ResourceLocator loc) {
        try (AudioInputStream ais = loc.asAudioStream()) {
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clips.put(name, clip);
            clip.stop();
        }
        catch(javax.sound.sampled.UnsupportedAudioFileException
                | LineUnavailableException
                | java.io.IOException ex) {
            java.util.logging.Logger.getLogger(MainRunningThing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return this;
    }
    public Clip getClip(String name) {
        return name == null ? current : clips.getOrDefault(name, null);
    }
    public AudioManager setPlaying(String name) {
        return setPlaying(name, 0);
    }
    public AudioManager setPlaying(String name, long mspt) {
        if (clips.get(name).equals(current))
            return this;
        return restartPlaying(name, mspt);
    }
    public AudioManager restartPlaying(String name, long mspt) {
        if(current != null) {
            current.stop();
        }
        current = clips.getOrDefault(name, null);
        if(current != null) {
            current.setMicrosecondPosition(mspt);
            current.start();
        }
        return this;
    }
}

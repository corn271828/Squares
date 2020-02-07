package squares.api;

import java.io.BufferedReader;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;

public class ResourceLocator {
    private static final String format = "assets/%s/%s";

    private final String path;
    private InputStream stream;
    private final static String[] prefices = {"", "/"};

    public ResourceLocator(String namespace, String path) {
        this.path = String.format(format, namespace, path);
        boolean okay = false;
        for(String prefix: prefices) {
            stream = getClass().getResourceAsStream(prefix + this.path);
            if(stream != null) {
                stream = new java.io.BufferedInputStream(stream);
                okay = true;
                break;
            }
        }
        if(!okay)
            throw new IllegalArgumentException("Could not locate file: " + this.path);
    }
    public BufferedReader asBufferedReader() {
        return new BufferedReader(new java.io.InputStreamReader(stream));
    }
    public AudioInputStream asAudioStream() throws javax.sound.sampled.UnsupportedAudioFileException, java.io.IOException {
        return javax.sound.sampled.AudioSystem.getAudioInputStream(stream);
    }
}

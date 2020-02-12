package squares.api;

import javax.swing.ImageIcon;
import java.io.BufferedReader;
import java.io.InputStream;

public class ResourceLoader {
    private static final String format = "assets/%s/%s";

    private final String path;
    private final static String[] prefices = {"", "/"};

    public ResourceLoader(String namespace, String name) {
        this.path = String.format(format, namespace, name);
    }

    // Output methods
    public InputStream asInputStream(String suffix) {
        String path = suffixedPath(suffix);
        for(String prefix: prefices) {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(prefix + path);
            if(stream != null)
                return new java.io.BufferedInputStream(stream);
        }
        throw new IllegalArgumentException("Not Okay - Could not locate file: " + path);
    }
    public BufferedReader asBufferedReader() {
        return new BufferedReader(new java.io.InputStreamReader(asInputStream("txt")));
    }
    public javax.sound.sampled.AudioInputStream asAudioStream() throws javax.sound.sampled.UnsupportedAudioFileException, java.io.IOException {
        return javax.sound.sampled.AudioSystem.getAudioInputStream(asInputStream("wav"));
    }
    public ImageIcon asImageIcon() {
        return new ImageIcon(suffixedPath("png"));
    }

    private String suffixedPath(String suffix) {
        return suffix.lastIndexOf('.') == -1 ? path + "." + suffix : path;
    }
}

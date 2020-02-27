package squares.api;

import javax.swing.ImageIcon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

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
        path = path.substring("assets/".length());
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
        ImageIcon ret = new ImageIcon(suffixedPath("png"));
        if (ret.getIconHeight() <= 1)
            try {
                ret = new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource(suffixedPath("png").substring("assets/".length()))));
            } catch (IOException ex) {
                Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        return ret;
    }

    private String suffixedPath(String suffix) {
        return path.lastIndexOf('.') == -1 ? path + "." + suffix : path;
    }
}

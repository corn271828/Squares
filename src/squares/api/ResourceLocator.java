package squares.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.sound.sampled.AudioInputStream;

public class ResourceLocator {
	private final String path;
	private InputStream stream;
	private final static String[] prefices = {"", "/"};
	
	public ResourceLocator(String path) {
		this.path = path;
		boolean okay = false;
		for(String prefix: prefices) {
			stream = getClass().getResourceAsStream(prefix + path);
			if(stream != null) {
				okay = true;
				break;
			}
		}
		if(!okay)
			throw new IllegalArgumentException("Could not locate file: " + path);
	}
	public BufferedReader asBufferedReader() {
		return new BufferedReader(new InputStreamReader(stream));
	}
	public AudioInputStream asAudioStream() throws javax.sound.sampled.UnsupportedAudioFileException, java.io.IOException {
		return javax.sound.sampled.AudioSystem.getAudioInputStream(stream);
	}
}

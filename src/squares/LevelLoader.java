package squares;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.util.function.BiFunction;

import squares.api.ResourceLocator;

public class LevelLoader {
    private int index = 0;
    private Level[] levels = new Level[0];
    public static final Map<String, BiFunction<String[][], String[], Level>> levelProvs = new java.util.HashMap<>();
    static {
        LevelLoader.levelProvs.put("sjbossfight", SJBossFight::new);
        LevelLoader.levelProvs.put("level", Level::new);
    }

    public LevelLoader(ResourceLocator index) {
        List<Level> levels = new ArrayList<>();
        try(BufferedReader br = index.asBufferedReader()) {
            while(br.ready()) {
                String provider = br.readLine();
                if(!levelProvs.containsKey(provider))
                    throw new IllegalArgumentException("Could not find a loader for level type: " + provider);
                String[] arguments = br.readLine().split("\\s+");
                List<String[]> map = new ArrayList<>();
                String buf;
                System.out.println(java.util.Arrays.toString(arguments));
                while((buf = line(br)) != null)
                    map.add(buf.split("\\s+"));
                levels.add(levelProvs.get(provider).apply(map.toArray(new String[0][0]), arguments));
            }
            this.levels = levels.toArray(this.levels);
        }
        catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }
    static private String line(BufferedReader br) throws java.io.IOException {
        String ret = br.readLine();
        if("".equals(ret)) return line(br);
        return "%%".equals(ret) ? null : ret;
    }
    public Level[] getLevels() { return levels; }
    public Level getNext() {
        return levels[++index];
    }
    public Level getPrev() {
        return levels[--index];
    }
    public Level getCurrent() {
        return levels[index];
    }
    public int getLevelIndex() {
        return index;
    }
    public int getNumLevels() {
        return levels.length;
    }
    public Level getLevel(int index) {
        return levels[index];
    }
    public void setLevelIndex(int ind) {
        index = ind;
    }
}

package squares.api.block;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;


//import squares.level.Level;

public class BlockFactory {
    @FunctionalInterface
    public interface BlockProv extends Function<String, Block> {}
    @FunctionalInterface
    public interface BlockListener {
        void onBlockCreated(Block b, int x, int y);
    }

    private static Map<Character, BlockProv> blockProvs = new HashMap<>();
    private Map<Character, List<BlockListener>> listeners = new HashMap<>();

    public static void addBlockType(Character c, BlockProv prov) {
        blockProvs.put(c, prov);
    }

    public void addBlockListener(Character c, BlockListener bl) {
        listeners.computeIfAbsent(c, $ -> new LinkedList<BlockListener>()).add(bl);
    }

    public void removeBlockListener(Character c, BlockListener bl) {
        if(!listeners.containsKey(c)) return;
        listeners.get(c).remove(bl);
    }

    public Block makeBlock(String input) {
        return makeBlock(input, -1, -1);
    }

    public Block makeBlock(String input, int x, int y) {
        char type = input.charAt(0);
        if(!blockProvs.containsKey(type))
            throw new IllegalArgumentException("Unknown ID-char: " + type);
        Block res = blockProvs.get(type).apply(input.substring(1));
        if(listeners.containsKey(type))
            for(BlockListener bl: listeners.get(type))
                bl.onBlockCreated(res, x, y);
        return res;
    }
}

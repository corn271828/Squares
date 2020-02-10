package squares;

import squares.api.block.Block;
import squares.api.block.BlockFactory;
import squares.api.Direction;
import squares.block.BlasterBlock;
import squares.block.CannonBlock;
import squares.block.EndingBlock;
import squares.block.LauncherBlock;
import squares.block.NormalBlock;
import squares.level.Level;
import squares.level.LevelLoader;
import squares.level.SJBossFight;

public class RegistrationHandler {
    private RegistrationHandler() {}
    private static boolean done = false;

    public static final Block nb = new NormalBlock();
    public static final Block eb = new EndingBlock();
    public static final Block[] lbs = new LauncherBlock[Direction.values().length];

    public static boolean init() {
        if(done) return false;
        done = true;

        for(int i=0; i<lbs.length; i++)
            lbs[i] = new LauncherBlock(Direction.values()[i]);

        BlockFactory.addBlockType('N', $   -> nb);
        BlockFactory.addBlockType('X', $   -> nb);
        BlockFactory.addBlockType('O', $   -> eb);
        BlockFactory.addBlockType('0', $   -> null);
        BlockFactory.addBlockType('L', str -> lbs[getDirByChar(str.charAt(0)).ordinal()]);
        BlockFactory.addBlockType('B', str -> new BlasterBlock(getDirByChar(str.charAt(0)), str.substring(1)));
        BlockFactory.addBlockType('C', str -> new CannonBlock(str));

        LevelLoader.addLevelType("sjbossfight", SJBossFight::new);
        LevelLoader.addLevelType("level", Level::new);
        return true;
    }
    
    public static Direction getDirByChar(char c) {
        switch(c) {
        case '^':
            return Direction.UP;
        case '>':
            return Direction.RIGHT;
        case 'v':
            return Direction.LEFT;
        case '<':
            return Direction.DOWN;
        }
        return null;
    }
}

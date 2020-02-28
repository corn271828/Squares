package squares;

import squares.api.block.Block;
import squares.api.block.BlockFactory;
import squares.api.Direction;
import squares.block.BlasterBlock;
import squares.block.ButtonBlock;
import squares.block.ButtonBlock.ButtonLinkedBlock;
import squares.block.CannonBlock;
import squares.block.EndingBlock;
import squares.block.LauncherBlock;
import squares.block.NormalBlock;
import squares.block.ShankerBlock;
import squares.level.BaseLevel;
import squares.level.LevelLoader;
import squares.level.SJBossFight;
import squares.level.TesterLevel;

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
        BlockFactory.addBlockType('C', CannonBlock::new);
        BlockFactory.addBlockType('U', str -> new ButtonBlock(Integer.parseInt(str)));
        BlockFactory.addBlockType('V', str -> new ButtonLinkedBlock(Integer.parseInt(str.substring(0, 1)), str.length() > 1));
        BlockFactory.addBlockType('S', str -> new ShankerBlock(str));

        LevelLoader.addLevelType("sjbossfight", SJBossFight::new);
        LevelLoader.addLevelType("level",       BaseLevel::new);
        LevelLoader.addLevelType("testlevel",   TesterLevel::new);
        return true;
    }
    
    public static Direction getDirByChar(char c) {
        switch(c) {
        case '^':
            return Direction.UP;
        case '>':
            return Direction.RIGHT;
        case 'v':
            return Direction.DOWN;
        case '<':
            return Direction.LEFT;
        }
        return null;
    }
}

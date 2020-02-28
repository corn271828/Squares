package squares.block;

import javax.swing.Icon;
import squares.api.Clock;
import squares.api.block.Block;

public abstract class BaseBlock implements Block {
    protected Icon icon;
    public final String label;

    public BaseBlock(Icon i, String l) {
        icon = i;
        label = l;
    }

    @Override
    public boolean refreshIcon() {
        return false;
    }

    @Override
    public void reset() {}

    @Override
    public String toString() {
        return label;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }
    
    @Override
    public boolean isOuch() {
        return false;
    }
}

package squares.api.block;

import squares.Player;
import squares.block.BlasterBlock; // Stopgap

public interface FiringBlock {
    int getPeriod();
    int getPhase();
    //Projectile createAtCoords(int x, int y);
    BlasterBlock.Blast createAtCoords(int x, int y);
}

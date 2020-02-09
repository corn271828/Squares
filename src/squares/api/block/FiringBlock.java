package squares.api.block;

import squares.Player;

public interface FiringBlock {
    int getPeriod();
    int getPhase();
    Projectile createAtCoords(int x, int y);
}

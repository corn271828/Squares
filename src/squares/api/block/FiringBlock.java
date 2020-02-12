package squares.api.block;

import squares.Player;
import squares.api.entity.Projectile;

public interface FiringBlock {
    int getPeriod();
    int getPhase();
    Projectile createAtCoords(int x, int y);
}

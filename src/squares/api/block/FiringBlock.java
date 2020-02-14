package squares.api.block;

import squares.api.entity.Projectile;

public interface FiringBlock extends Block {
    int getPeriod();
    int getPhase();
    Projectile createAtCoords(int x, int y);
}

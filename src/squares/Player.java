package squares;

import squares.api.CharacterState;

public class Player {
    public static final int itime = 10;
    public static final int PRACTICE_MODE_LIVES = 100;

    public CharacterState charState;
    public int xPosition; //Target position of the character in grid coordinates
    public int yPosition;
    public int xTarg; // Target position of the character in panel coordinates
    public int yTarg;
    public int xCoordinates; //Position of the upper left hand corner of the character pic in panel coordinates
    public int yCoordinates;

    public int iftime;
    public int hp;
    public boolean isPracticeMode;

    public boolean isInvincible(int cframe) {
        return iftime + itime >= cframe;
    }

    public Level level;
}

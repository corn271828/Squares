package squares;

import java.awt.Rectangle;
import java.awt.geom.Area;
import squares.api.CharacterState;
import static squares.api.RenderingConstants.CHARACTER_FASTSPEED;
import static squares.api.RenderingConstants.CHARACTER_SPEED;
import static squares.api.RenderingConstants.CHARACTER_WIDTH;

public class Player {
    public static final int itime = 10;
    public static final int PRACTICE_MODE_LIVES = 100;

    public CharacterState charState = CharacterState.NORMAL;
    public int xPosition; //Target position of the character in grid coordinates
    public int yPosition;
    public int xTarg; // Target position of the character in panel coordinates
    public int yTarg;
    public int xCoordinates; //Position of the upper left hand corner of the character pic in panel coordinates
    public int yCoordinates;

    public int iftime = -11;
    public int hp = 1;
    public boolean isPracticeMode = false;
    
    public static final int RIGHT_KEY_PRESS = 39;
    public static final int LEFT_KEY_PRESS = 37;
    public static final int DOWN_KEY_PRESS = 40;
    public static final int UP_KEY_PRESS = 38;

    public boolean isInvincible(int cframe) {
        return iftime + itime >= cframe;
    }

    public boolean canMoveRight() {
        return !(xPosition == level.blocks[0].length - 1) &&
                !(level.blocks[yPosition][xPosition + 1] == null) &&
                level.blocks[yPosition][xPosition + 1].stepable;
    }
    
    public boolean canMoveLeft() {
        return !(xPosition == 0) &&
                !(level.blocks[yPosition][xPosition - 1] == null) &&
                level.blocks[yPosition][xPosition - 1].stepable;
    }
    
    public boolean canMoveUp() {
        return !(yPosition == 0) &&
                !(level.blocks[yPosition - 1][xPosition] == null) &&
                level.blocks[yPosition - 1][xPosition].stepable;
    }
    
    public boolean canMoveDown() {
        return !(yPosition == level.blocks.length - 1) &&
                !(level.blocks[yPosition + 1][xPosition] == null) &&
                level.blocks[yPosition + 1][xPosition].stepable;
    }
    
    public void callMove(int keyCode) {
        switch (keyCode) { // move
            case RIGHT_KEY_PRESS:
            case 'D':
                if (canMoveRight())
                    xPosition++;
                break;
            case LEFT_KEY_PRESS:
            case 'A':
                if (canMoveLeft())
                    xPosition--;
                break;
            case DOWN_KEY_PRESS:
            case 'S':
                if (canMoveDown())
                    yPosition++;
                break;
            case UP_KEY_PRESS:
            case 'W':
                if (canMoveUp())
                    yPosition--;
                break;
        }
    }
    
    public boolean moveAnim(Area clipholder) {
        // Checks to see if the character is still moving
        int currSpeed = charState == CharacterState.MOVING ? CHARACTER_SPEED : charState == CharacterState.FASTMOVING ? CHARACTER_FASTSPEED : 0;
        if (currSpeed == 0)
            return false;
        
        if (xCoordinates != xTarg) {
            if (Math.abs(xCoordinates - xTarg) <= currSpeed) {
                xCoordinates = xTarg;
            } else 
                xCoordinates += xCoordinates > xTarg ? -currSpeed : currSpeed;
        }
        if (yCoordinates != yTarg) {
            if (Math.abs(yCoordinates - yTarg) <= currSpeed) {
                yCoordinates = yTarg;
            } else 
                yCoordinates += yCoordinates > yTarg ? -currSpeed : currSpeed;
        }
        clipholder.add(new Area(new Rectangle(xCoordinates - currSpeed, yCoordinates - currSpeed, 
                CHARACTER_WIDTH + 2 * currSpeed,  CHARACTER_WIDTH + 2 * currSpeed)));
        
        return xCoordinates == xTarg && yCoordinates == yTarg; // for landchecker
    }
    
    public Level level;
}

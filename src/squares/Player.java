package squares;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.function.Consumer;

import squares.api.CharacterState;
import squares.api.Coordinate;
import squares.api.Clock;
import static squares.api.RenderingConstants.BORDER_WIDTH;
import squares.api.level.Level;

import static squares.api.RenderingConstants.CHARACTER_FASTSPEED;
import static squares.api.RenderingConstants.CHARACTER_SPEED;
import static squares.api.RenderingConstants.CHARACTER_WIDTH;
import static squares.api.RenderingConstants.SPACING_BETWEEN_BLOCKS;
import squares.api.level.BossLevel;

public class Player {
    public static final int itime = 10;
    public static final int PRACTICE_MODE_LIVES = 100;

    public CharacterState charState = CharacterState.NORMAL;
    public Coordinate position = new Coordinate(0, 0); // Target pos, grid coords
    public Coordinate target   = new Coordinate(0, 0); // Target position of the character in panel coordinates
    public Coordinate render   = new Coordinate(0, 0); // Position of the upper left hand corner of the character pic in panel coordinates
    public Coordinate drawingStart;
    public Consumer<Player> deathCb;

    public int iftime = -11;
    public int deaths = 0;
    public int hp = 1;
    public boolean isPracticeMode = false;
    
    public static final int RIGHT_KEY_PRESS = 39;
    public static final int LEFT_KEY_PRESS = 37;
    public static final int DOWN_KEY_PRESS = 40;
    public static final int UP_KEY_PRESS = 38;

    public Clock clock;
    public Level level;
    
    public int queueKey;
    public int queueKeyTime;

    public Player(Clock clock, Coordinate ds) {
        this.clock = clock;
        drawingStart = ds;
    }
    
    public boolean isInvincible() {
        return iftime + itime >= clock.getTimestamp();
    }

    public void setMaxHP(int hp) {
        this.hp = isPracticeMode ? PRACTICE_MODE_LIVES : hp;
    }

    public boolean canMoveRight() {
        return !(position.x == level.xSize() - 1) &&
                !(level.blockAt(position.x + 1, position.y) == null) &&
                level.blockAt(position.x + 1, position.y).stepable;
    }
    
    public boolean canMoveLeft() {
        return !(position.x == 0) &&
                !(level.blockAt(position.x - 1, position.y) == null) &&
                level.blockAt(position.x - 1, position.y).stepable;
    }
    
    public boolean canMoveUp() {
        return !(position.y == 0) &&
                !(level.blockAt(position.x, position.y - 1) == null) &&
                level.blockAt(position.x, position.y - 1).stepable;
    }
    
    public boolean canMoveDown() {
        return !(position.y == level.ySize() - 1) &&
                !(level.blockAt(position.x, position.y + 1) == null) &&
                level.blockAt(position.x, position.y + 1).stepable;
    }
    
    public void setQueueKey(int keyCode) {
        queueKey = keyCode;
        queueKeyTime = clock.getTimestamp();
    }
    
    public void checkFlushQueueKey() {
        if (Math.abs(clock.getTimestamp() - queueKeyTime) > 2)
            queueKey = -12345;
    }
    
    public void callMove() {
        if (queueKey == -12345)
            return;
        switch (queueKey) { // move
            case RIGHT_KEY_PRESS:
            case 'D':
                if (canMoveRight())
                    position.x++;
                break;
            case LEFT_KEY_PRESS:
            case 'A':
                if (canMoveLeft())
                    position.x--;
                break;
            case DOWN_KEY_PRESS:
            case 'S':
                if (canMoveDown())
                    position.y++;
                break;
            case UP_KEY_PRESS:
            case 'W':
                if (canMoveUp())
                    position.y--;
                break;
        }
        target.x = drawingStart.x +position.x * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        target.y = drawingStart.y + position.y * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        if (charState == CharacterState.NORMAL) {
            if (level instanceof BossLevel) 
                charState = CharacterState.FASTMOVING;
            else
                charState = CharacterState.MOVING;
        }
        queueKey = -12345;
    }
    
    public boolean moveAnim(Area clipholder) {
        // Checks to see if the character is still moving
        int currSpeed = charState == CharacterState.MOVING ? CHARACTER_SPEED : charState == CharacterState.FASTMOVING ? CHARACTER_FASTSPEED : 0;
        if (currSpeed == 0)
            return false;
        
        if (render.x != target.x) {
            if (Math.abs(render.x - target.x) <= currSpeed) {
                render.x = target.x;
            } else 
                render.x += render.x > target.x ? -currSpeed : currSpeed;
        }
        if (render.y != target.y) {
            if (Math.abs(render.y - target.y) <= currSpeed) {
                render.y = target.y;
            } else 
                render.y += render.y > target.y ? -currSpeed : currSpeed;
        }
        clipholder.add(new Area(new Rectangle(render.x - currSpeed, render.y - currSpeed, 
                CHARACTER_WIDTH + 2 * currSpeed,  CHARACTER_WIDTH + 2 * currSpeed)));
        
        return render.x == target.x && render.y == target.y; // for landchecker
    }

    public void hurt() {
        if (!isInvincible() && charState.vulnerable) {
            if (--hp <= 0) {
                die();
            } else {
                iftime = clock.getTimestamp();
            }
        }
    }

    public void die() {
        if (charState.vulnerable) {
            charState = CharacterState.DEAD;
            level.addDeath();
            deaths++;
            deathCb.accept(this);
        }
    }
}

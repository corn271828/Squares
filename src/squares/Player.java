package squares;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.function.Consumer;

import squares.api.CharacterState;
import squares.api.Coordinate;
import squares.api.Clock;
import squares.api.level.BossLevel;
import squares.api.level.Level;

import static squares.api.RenderingConstants.CHARACTER_FASTSPEED;
import static squares.api.RenderingConstants.CHARACTER_SPEED;
import static squares.api.RenderingConstants.CHARACTER_WIDTH;
import static squares.api.RenderingConstants.SPACING_BETWEEN_BLOCKS;
import static squares.api.RenderingConstants.BORDER_WIDTH;
import squares.block.ShankerBlock;

public class Player {
    private static final int itime = 10;
    private static final int PRACTICE_MODE_LIVES = 100;

    public CharacterState charState = CharacterState.NORMAL;
    public Coordinate position = new Coordinate(0, 0); // Target pos, grid coords
    public Coordinate target   = new Coordinate(0, 0); // Target position of the character in panel coordinates
    public Coordinate render   = new Coordinate(0, 0); // Position of the upper left hand corner of the character pic in panel coordinates
    public Coordinate drawingStart;
    public Consumer<Player> deathCb;

    private int iftime = -11;
    private int deaths = 0;
    private int hp = 1;
    public boolean isPracticeMode = false;
    
    private static final int RIGHT_KEY_PRESS = 39;
    private static final int LEFT_KEY_PRESS = 37;
    private static final int DOWN_KEY_PRESS = 40;
    private static final int UP_KEY_PRESS = 38;

    public Clock clock;
    public Level level;
    
    private int queueKey;
    private int queueKeyTime;
    private HUDLine[] hud = {
        new HUDLine("Death Count (total)", 1, 0.0),
        new HUDLine("Death Count (level)", 1, 1.0),
        new HUDLine("HP",                  1, 2.0),
        new HUDLine("Level Code",          1, 3.0),
        new HUDLine("Current Tick",       -1, 1.0),
        new HUDLine("Practice Mode",      -1, 2.0),
        new HUDLine("",               0, 1.0),
    };

    public Player(Clock clock, Coordinate ds) {
        this.clock = clock;
        drawingStart = ds;
        ShankerBlock.setTime(clock);
    }
    
    public boolean isInvincible() {
        return iftime + itime >= clock.time();
    }
    
    public boolean shouldRefresh() {
        return iftime + itime > clock.time();
    }

    public void setMaxHP(int hp) {
        this.hp = isPracticeMode ? PRACTICE_MODE_LIVES : hp;
        this.iftime = ~itime;
    }

    public boolean canMoveRight() {
        return !(position.x == level.xSize() - 1) &&
                !(level.blockAt(position.x + 1, position.y) == null) &&
                level.blockAt(position.x + 1, position.y).canStep();
    }
    
    public boolean canMoveLeft() {
        return !(position.x == 0) &&
                !(level.blockAt(position.x - 1, position.y) == null) &&
                level.blockAt(position.x - 1, position.y).canStep();
    }
    
    public boolean canMoveUp() {
        return !(position.y == 0) &&
                !(level.blockAt(position.x, position.y - 1) == null) &&
                level.blockAt(position.x, position.y - 1).canStep();
    }
    
    public boolean canMoveDown() {
        return !(position.y == level.ySize() - 1) &&
                !(level.blockAt(position.x, position.y + 1) == null) &&
                level.blockAt(position.x, position.y + 1).canStep();
    }
    
    public void setQueueKey(int keyCode) {
        queueKey = keyCode;
        queueKeyTime = clock.time();
    }
    
    public void checkFlushQueueKey() {
        if (Math.abs(clock.time() - queueKeyTime) > 1)
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
                iftime = clock.time();
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

    public HUDLine[] getHUDLines() {
        hud[0].value = String.valueOf(deaths);
        hud[1].value = String.valueOf(level.getDeaths());
        hud[2].value = String.valueOf(hp);
        hud[3].value = level.code;
        hud[4].value = String.valueOf(clock.time());
        hud[5].value = isPracticeMode ? "ON" : "OFF";
        hud[6].value = level.label;
        return hud;
    }
    static class HUDLine {
        public final String key;
        private String value;
        public final double align, voffset;
        public HUDLine(String key, double align, double voffset) {
            this.key = key;
            this.align = align;
            this.voffset = voffset;
        }
        public String getValue()    { return value; }
    }
}

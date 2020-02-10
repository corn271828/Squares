package squares;

import java.io.BufferedReader;
import java.util.TreeMap;
import java.util.Map;
import java.util.logging.Logger;

import squares.api.CharacterState;
import squares.api.Coordinate;
import squares.api.ResourceLoader;
import squares.api.block.Block;
import squares.api.level.Level;
import squares.api.level.BossLevel;

import static squares.api.RenderingConstants.*;

public class TasGenerator {
    Map<Integer, Character> script;

    public TasGenerator(ResourceLoader in) {
        script = new TreeMap<>();
        try (BufferedReader br = in.asBufferedReader()) {
            while (br.ready()) {
                String[] split = br.readLine().split(" ");
                script.put(Integer.parseInt(split[0]), split[1].charAt(0));
            }
        } catch (java.io.IOException ex) {
            Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public boolean doTasStuff(Coordinate start, int timestamp, Player player) {
        if (script.containsKey(timestamp)) {
            player.callMove(script.get(timestamp));
            player.target.x = start.x + player.position.x * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
            player.target.y = start.y + player.position.y * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
            if (player.charState == CharacterState.NORMAL) {
                if (player.level instanceof BossLevel) 
                    player.charState = CharacterState.FASTMOVING;
                else
                    player.charState = CharacterState.MOVING;
            }
            return true;
        }
        return false;
    }

}

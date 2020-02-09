package squares;

import java.io.BufferedReader;
import java.util.TreeMap;
import java.util.Map;
import java.util.logging.Logger;

import squares.api.CharacterState;
import squares.api.ResourceLoader;
import squares.block.Block;

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

    public boolean doTasStuff(int startx, int starty, int timestamp, Player player) {
        if (script.containsKey(timestamp)) {
            player.callMove(script.get(timestamp));
            player.xTarg = startx + player.xPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
            player.yTarg = starty + player.yPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
            if (player.charState == CharacterState.NORMAL) {
                if (player.level instanceof Level.BossLevel) 
                    player.charState = CharacterState.FASTMOVING;
                else
                    player.charState = CharacterState.MOVING;
            }
            return true;
        }
        return false;
    }

}

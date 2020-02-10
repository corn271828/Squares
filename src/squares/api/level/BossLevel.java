package squares.api.level;

import java.util.List;

import squares.api.Coordinate;
import squares.api.block.Projectile;
import squares.api.ResourceLoader;

import static squares.level.BaseLevel.LineExploder; // stopgap

public interface BossLevel {
    default String[] parseControls(ResourceLoader input) {
        try (java.io.BufferedReader br = input.asBufferedReader()) {
            StringBuilder hold = new StringBuilder();
            while(br.ready()) 
                hold.append(br.readLine());
            String[] controls = hold.toString().split(",");
            for (int i = 0; i < controls.length; i++)
                controls[i] = controls[i].trim();
            return controls;
        } catch (java.io.IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
    List<? extends Projectile> generateBlasts(int timestamp,  Coordinate render, Coordinate start);
    List<? extends LineExploder> generateLines(int timestamp, Coordinate render, Coordinate start);

    int getEndTime();
    int getPlayerHealth();
}

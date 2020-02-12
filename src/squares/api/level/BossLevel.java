package squares.api.level;

import squares.api.Coordinate;
import squares.api.entity.Projectile;
import squares.api.ResourceLoader;

public interface BossLevel {
    int getEndTime();

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
}

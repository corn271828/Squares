/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares.level;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import javax.swing.ImageIcon;

import squares.api.ResourceLoader;
import squares.api.Clock;
import squares.api.Coordinate;
import squares.api.entity.Resettable;

import static squares.api.RenderingConstants.SPACING_BETWEEN_BLOCKS;
import static squares.api.RenderingConstants.STANDARD_ICON_WIDTH;

/**
 *
 * @author piercelai
 */
public class LineExploderTester extends BaseLevel.LineExploder implements Resettable {
    public static final int sans_width = 70;
    public static final int sans_height = 100;
    public static final int[] opacities = new int[] {50, 100, 200, 255, 255, 255, 200, 100, 50};

    public static final ImageIcon sansImage = new ImageIcon(new ResourceLoader("sprites", "sans_thumbnail2").asImageIcon().getImage().getScaledInstance(sans_width, sans_height, java.awt.Image.SCALE_SMOOTH));
    public static final ImageIcon sansSrous = new ImageIcon(new ResourceLoader("sprites", "sans_srs").asImageIcon().getImage().getScaledInstance(sans_width, sans_height, java.awt.Image.SCALE_SMOOTH));
    public final ImageIcon plody;

    public final Clock clock;
    private Coordinate origin;

    public LineExploderTester(int stt, int tl, double ang, int sxp, int syp, Clock c) {
        this(stt, tl, ang, sxp, syp, c, sansImage);
    }

    public LineExploderTester(int stt, int tl, double ang, int sxp, int syp, Clock c, ImageIcon ico) {
        super(stt, tl, ang, sxp, syp);
        clock = c;
        plody = ico;
    }

    @Override
    public void draw(Graphics g, Component c) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(angle);

        if (clock.time() <= starttime + 10) {
            plody.paintIcon(c, g2d, xregister - sans_width / 2, yregister - sans_height / 2);
        } else if (clock.time() < starttime + timelength - 2) {
            int opacity = opacities[clock.time() - starttime - 10];
            g2d.setColor(new Color(100, 0, 0, opacity));
            g2d.fillRect(xregister - sans_width / 2, yregister, sans_width, 1000);
            g2d.setColor(new Color(200, 0, 0, opacity));
            g2d.fillRect(xregister - 2 * sans_width / 5, yregister, sans_width - sans_width / 5, 1000);
            g2d.setColor(new Color(255, 0, 0, opacity));
            g2d.fillRect(xregister - sans_width / 4, yregister, sans_width - sans_width / 2, 1000);
        }
        g2d.rotate(-angle);
    }

    @Override
    public Area getCollision() {
        if (clock.time() > starttime + 10 && clock.time() < starttime + timelength - 2)
            return new Area(tx.createTransformedShape(new Rectangle(xregister - sans_width / 2, yregister - sans_height / 2, sans_width, 1000)));
        else
            return new Area();
    }

    
    @Override
    public void resetToOrigin() {
        moveTo(origin.x, origin.y);
    }

    @Override
    public void updateToPanel(Coordinate render, Coordinate start) {
        moveTo(start.x + SPACING_BETWEEN_BLOCKS * getX() / 2 + STANDARD_ICON_WIDTH / 2,
                   start.y + SPACING_BETWEEN_BLOCKS * getY() / 2 + STANDARD_ICON_WIDTH / 2);
    }

    @Override
    public void setOrigin() {
        origin = new Coordinate(getX(), getY());
    }
}

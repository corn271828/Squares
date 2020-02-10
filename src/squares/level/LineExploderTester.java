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
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Area;
import javax.swing.ImageIcon;

/**
 *
 * @author piercelai
 */
public class LineExploderTester extends BaseLevel.LineExploder {
    public static final int sans_width = 70;
    public static final int sans_height = 100;
    public static final int[] opacities = new int[] {50, 100, 200, 255, 255, 255, 200, 100, 50};

    public static final ImageIcon sansImage = new ImageIcon(new ImageIcon("Pics/sans_thumbnail2.png", "sanspic").getImage().getScaledInstance(sans_width, sans_height, java.awt.Image.SCALE_SMOOTH));
    public static final ImageIcon sansSrous = new ImageIcon(new ImageIcon("Pics/sans_srs.png", "sanssrspic").getImage().getScaledInstance(sans_width, sans_height, java.awt.Image.SCALE_SMOOTH));
    ImageIcon plody;


    public LineExploderTester(int stt, int tl, double ang, int sxp, int syp) {
        super(stt, tl, ang, sxp, syp);
        plody = sansImage;
    }

    public LineExploderTester(int stt, int tl, double ang, int sxp, int syp, ImageIcon ico) {
        super(stt, tl, ang, sxp, syp);
        plody = ico;
    }

    @Override
    public void drawXPlosion(Component c, Graphics g, int timestamp) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(angle);

        if (timestamp <= starttime + 10) {
            plody.paintIcon(c, g2d, xregister - sans_width / 2, yregister - sans_height / 2);
        } else if (timestamp < starttime + timelength - 2) {
            int opacity = opacities[timestamp - starttime - 10];
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
    public Area xplosionOuchArea(int timestamp) {
        if (timestamp > starttime + 10 && timestamp < starttime + timelength - 2)
            return new Area(tx.createTransformedShape(new Rectangle(xregister - sans_width / 2, yregister - sans_height / 2, sans_width, 1000)));
        else
            return new Area();
    }

    @Override
    public Area xplosionClipArea(int timestamp) {
        if (timestamp <= starttime + 10) {
            return new Area(tx.createTransformedShape(new Rectangle(xregister - sans_width / 2, yregister - sans_height / 2, sans_width, sans_height)));
        } else {
            return new Area(tx.createTransformedShape(new Rectangle(xregister - sans_width / 2, yregister - sans_height / 2, sans_width, 1000)));
        }
    }


    @Override
    public LineExploderTester clone() {
        return new LineExploderTester(starttime, timelength, angle, startPos.x, startPos.y, plody);
    }


}

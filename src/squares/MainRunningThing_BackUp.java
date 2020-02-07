/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

/**
 *
 * @author lai_889937
 */
public class MainRunningThing_BackUp extends javax.swing.JFrame {
    
    public static enum CharacterState {
        NORMAL, DEAD, MOVING, LOCKED, WINE, RESTARTING, FASTMOVING;
    }

    public CharacterState charState;
    public int xPosition; //Target position of the character in grid coordinates
    public int yPosition;
    public int xTarg; // Target position of the character in panel coordinates
    public int yTarg;
    public int xCoordinates; //Position of the upper left hand corner of the character pic in panel coordinates
    public int yCoordinates;
    
    public AudioInputStream backgroundStream;
    public Clip clip;
    public long clipTimeHolder = 0;
    public AudioInputStream bossMusicStream;
    public Clip bossClip;
    
    public int currentLevelIndex = 0; // INDEX of current level (add one to get level number)
    public Block[][] levelBlocks = new Block[0][0];
    
    public static final int STANDARD_ICON_WIDTH = 81; // Constants
    public static final int SPACING_BETWEEN_BLOCKS = 120;
    public static final int BORDER_WIDTH = 16;
    public static final int CHARACTER_WIDTH = STANDARD_ICON_WIDTH - 2 * BORDER_WIDTH;
    public static final int RIGHT_KEY_PRESS = 39;
    public static final int LEFT_KEY_PRESS = 37;
    public static final int DOWN_KEY_PRESS = 40;
    public static final int UP_KEY_PRESS = 38;
    public static final int FRAME_WIDTH = 1100;
    public static final int FRAME_HEIGHT = 780;
    public static final int CHARACTER_SPEED = 30;
    public static final int CHARACTER_FASTSPEED = 60;
    
    public ArrayList<Block.BlasterBlock.Blast> blasts = new ArrayList<>();
    public ArrayList<Level.BossLevel.LineExploder> lineExplosions = new ArrayList<>();
    
    public Color transitioning = null;
    public boolean isSwitching = false;
    public int opacity = 0;
    public boolean letsseeifthisworks = true;
    public int middlex; // middle of panel
    public int middley;
    public int startx; // where to start drawing blocks
    public int starty;
    public boolean shouldRepaint = false;
    public int timestamp;
    public int endx; //Border of block drawings; used to determine when ammunition disappears
    public int endy;
    
    public boolean isIFrame = false;
    public int iframeStart = 0;
    public int iframeTime = 10;
    public int currentLevelHealth = 1;
    public int currentHP = 1;
    
    public Area clipholder;
    public Area ouchArea;
    
    public static final ImageIcon characterIconAlive = new ImageIcon("Pics/Character.png", "Character image");
    
    public static final boolean SEE_OVERLAP = false;
    public boolean musicOn = true;
    public static final int PRACTICE_MODE_LIVES = 100;
    public boolean isPracticeMode = false;
    
    public static final int bossTestStartTime = 0;
            
    public Level[] levels = new Level[] {
        
        
        // ang,  xvel,  yvel,  xa,  ya,  angvel
        
        new Level(new String[][] {
            new String[] {  "X",    "N",    "O"}
        }
        , "1"),
        
        new SJBossFight(new String[][] {
            new String[] {  "", "", "",     "",    "",    "",   "", "", ""},
            new String[] {  "", "", "",     "",    "",    "",   "", "", ""},
            new String[] {  "", "", "N",    "N",    "N",  "N",  "N", "", ""},
            new String[] {  "", "", "N",    "N",    "N",  "N",  "N", "", ""},
            new String[] {  "", "", "N",    "N",    "X",  "N",  "N", "", ""},
            new String[] {  "", "", "N",    "N",    "N",  "N",  "N", "", ""},
            new String[] {  "", "", "N",    "N",    "N",  "N",  "N", "", ""},
            new String[] {  "", "",  "",     "",    "",    "",   "", "", ""},
            new String[] {  "", "", "",     "",    "",    "",   "", "", ""}
        }
        , "BOSS", 10, new File("sjbossfight.txt")),
        
        new Level(new String[][] {
            new String[] {  "", "",     "",    "",    "",   "", ""},
            new String[] {  "", "X",    "N",    "N",  "N",  "N", ""},
            new String[] {  "", "N",    "N",    "N",  "N",  "N", ""},
            new String[] {  "", "C201010",     "",    "",    "",   "N", ""},
            new String[] {  "", "N",    "N",    "N",  "N",  "N", ""},
            new String[] { "", "O",    "N",    "N",  "N",  "N", ""},
            new String[] {  "", "",     "",    "",    "",   "", ""}
        }
        , "-4"),
        
        new Level(new String[][] {
            new String[] {  "", "",     "",    "",    "",   "", ""},
            new String[] {  "", "X",    "N",    "N",  "N",  "N", ""},
            new String[] {  "", "N",    "N",    "N",  "N",  "N", ""},
            new String[] {  "", "C201010",     "",    "",    "",   "N", ""},
            new String[] {  "", "N",    "N",    "N",  "N",  "N", ""},
            new String[] { "", "O",    "N",    "N",  "N",  "N", ""},
            new String[] {  "", "",     "",    "",    "",   "", ""}
        }
        , "-3"),
        
        new Level(new String[][] {
            new String[] {  "X",    "N",    "N",  "N",  "N"},
            new String[] {  "N",    "N",    "N",  "N",  "N"},
            new String[] {  "C201010",     "",    "",  "",     "N"},
            new String[] {  "N",    "N",    "N",  "N",  "N"},
            new String[] {  "O",    "N",    "N",  "N",  "N"}
        }
        , "-2"),
        
        new Level(new String[][] {
            new String[] {  "X",    "N",    "",  "N",  "N", "N"},
            new String[] {  "",    "N",    "N",  "N",  "",  "N"},
            new String[] {  "C201005",     "",    "",    "C201005",   "N", "N"},
            new String[] {  "N",    "N",    "N",  "",  "N",  ""},
            new String[] {  "O",    "",    "N",  "N",  "N",  ""}
        }
        , "-1"),
        
        new Level(new String[][] {
            new String[] {  "X",    "N",    "N",  "N",  "N"},
            new String[] {  "N",    "N",    "N",  "N",  "N"},
            //new String[] {  "C2030","C2035", "C254005",   "C302010",  "N"},
            new String[] {  "C201005",     "",    "",  "",  "N"},
            new String[] {  "N",    "N",    "N",  "N",  "N"},
            new String[] {  "O",    "N",    "N",  "N",  "N"}
        }
        , "0"),
        
        new Level(new String[][] {
            new String[] {  "X",    "N",    "O"}
        }
        , "1"),
        
        new Level(new String[][] {
            new String[] {  "X",    "" ,    "N",    "O"},
            new String[] {  "N",    "" ,    "N",    ""},
            new String[] {  "N",    "N",    "N",    ""}
        }
        , "2"),
        
        new Level(new String[][] {
            new String[] {  "X",    "L>" ,  "",     "N",    "O"}
        }
        , "3"),
        
        new Level(new String[][] {
            new String[] {  "X",    "N",    "Lv"},
            new String[] {  "",     "",     ""},
            new String[] {  "Lv",   "N",    "N"},
            new String[] {  "",     "",     ""},
            new String[] {  "N",    "N",    "O"}
        }
        , "4"),
        
        new Level(new String[][] {
            new String[] {  "X",    "" ,    "N",    "Lv", "N",   "O"},
            new String[] {  "N",    "" ,    "",     "",   "",    ""},
            new String[] {  "L>",   "" ,    "",     "",   "",    "N"},
            new String[] {  "N",    "" ,    "",     "",   "",    "L<"},
            new String[] {  "L>",   "" ,    "",     "",   "",    "N"},
            new String[] {  "N",    "" ,    "",     "",   "",    "L<"},
            new String[] {  "N",    "N" ,   "L^",   "N", "L^",   ""}
        }
        , "5"),
        
        new Level(new String[][] {
            new String[] {  "",     "" ,    "L>",   "",     "Lv"},
            new String[] {  "X",    "L>" ,  "",     "Lv",   ""},
            new String[] {  "",     "" ,    "",     "",     ""},
            new String[] {  "O",    "" ,    "",     "",     "L<"},
            new String[] {  "",     "" ,    "L^",   "L<",   ""},
        }
        , "6"),
        
        new Level(new String[][] {
            new String[] {  "X",    "L>" ,  "Lv",   "N",    "Lv",   "L>",   "N"},
            new String[] {  "Lv",   "" ,    "",     "L^",   "",     "",     "L<"},
            new String[] {  "L>",   "" ,    "",     "",     "",     "",     "Lv"},
            new String[] {  "N",    "L^" ,  "",     "",     "",     "",     "N"},
            new String[] {  "L^",   "Lv" ,  "",     "",     "",     "",     "L<"},
            new String[] {  "L^",   "" ,    "",     "L<",   "L>",   "",     "Lv"},
            new String[] {  "N",    "L<" ,  "L>",   "N",    "L>",   "L^",   "O"}
        }
        , "7"),
        
        new Level(new String[][] {
            new String[] {  "",     "",     "Bv153005",   ""},
            new String[] {  "",     "",      "",     ""},
            new String[] {  "X",    "N",    "N",    "O"}
        }
        , "8"),
        
        new Level(new String[][] {
            new String[] {  "X",    "Bv2030" ,      "N",    "O"},
            new String[] {  "N",    "B>1530" ,      "N",    "B<1530"},
            new String[] {  "N",    "N",            "N",    ""}
        }
        , "9"),
        
        new Level(new String[][] {
            new String[] {  "X",    "Bv2540" ,  "N",    "O"},
            new String[] {  "Lv",   "" ,        "",     "B<1530"},
            new String[] {  "",     "",         "",     "B<2025"},
            new String[] {  "N",    "N",        "L^",   ""}
        }
        , "10"),
        
        new Level(new String[][] {
            new String[] {  "X",    "" ,      "",    ""},
            new String[] {  "N",    "N" ,      "B<3020",  ""},
            new String[] {  "N",    "",       "",    ""},
            new String[] {  "N",    "N",      "N",    ""},
            new String[] {  "",    "",        "N",    "B<1530"},
            new String[] {  "B^3520",    "",  "O",    ""}
        }
        , "11"),
        
        new Level(new String[][] {
            new String[] {  "X",    "Bv2015" ,    "N",    "Lv", "N",   "O"},
            new String[] {  "N",    "Bv3020" ,    "",     "",   "",    ""},
            new String[] {  "L>",   "" ,    "",     "",   "",    "N"},
            new String[] {  "N",    "" ,    "",     "",   "",    "L<"},
            new String[] {  "L>",   "" ,    "",     "",   "",    "N"},
            new String[] {  "N",    "" ,    "B>3040",     "",   "",    "L<"},
            new String[] {  "N",    "N" ,   "L^",   "N", "L^",   "B<4015"}
        }
        , "12"),
        
        new Level(new String[][] {
            new String[] {  "X",    "Bv104001" ,    "Bv104002",    "Bv104003", ""},
            new String[] {  "N",    "" ,    "",     "",   "B<2015"},
            new String[] {  "N",   "" ,    "",     "",    "B<201503"},
            new String[] {  "N",    "" ,    "",     "",   "B<201505"},
            new String[] {  "L>",   "" ,    "",     "",   "N"},
            new String[] {  "O",    "" ,    "",     "",    "L<"}
        }
        , "13"),
            
        new Level(new String[][] {
            new String[] {  "X",    "" ,      "N",    "O"},
            new String[] {  "N",    "" ,      "N",    "B<1520"},
            new String[] {  "N",    "N",      "N",    "B<2525"},
            new String[] {  "",     "N",      "B^3010",     ""}
        }
        , "C14"),
            
        new Level(new String[][] {
            new String[] {  "",    "" ,      "C153005",    ""},
            new String[] {  "",    "" ,      "",    ""},
            new String[] {  "X",    "N",      "N",    "O"}
        }
        , "15"),
        
        new Level(new String[][] {
            new String[] {  "X",    "C2020" ,      "N",    "O"},
            new String[] {  "N",    "C1510" ,      "N",    "C1510"},
            new String[] {  "N",    "N",           "N",    ""}
        }
        , "16"),
        
        new Level(new String[][] {
            new String[] {  "",    "X" ,      "N",    "",       "" ,    "Bv302025",   ""},
            new String[] {  "",    "N" ,      "",     "C202004",  "",     "",         ""},
            new String[] {  "N",   "N",       "",     "",       "",     "N",        "O"},
            new String[] {  "",    "N",       "",     "",       "N",    "N",        ""},
            new String[] { "N",    "N" ,      "",     "",       "",     "N",        "N"},
            new String[] { "",     "N" ,      "N",    "N",      "N",    "N",        "B<3020"},
            new String[] { "",     "B^302005" , "N",    "",       "N",     "",        ""}
        }
        , "C17")
    };
    
    public int[] deathCount = new int[levels.length];
    public int totalDeathCount = 0;
    
    /**
     * Creates new form MainRunningThing
     */
    public MainRunningThing_BackUp() {
        initComponents();
        jPanel1.setFocusable(true);
        jButton1.setFocusable(false);
        toggle_practice.setFocusable(false);
        jButton1.setVisible(true);
        middlex = jPanel1.getWidth() / 2;
        middley = jPanel1.getHeight() / 2;
        try {
            if (musicOn) {
                backgroundStream = AudioSystem.getAudioInputStream(new File("Sounds/Canon_in_D_Swing.wav"));
                clip = AudioSystem.getClip();
                clip.open(backgroundStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                bossMusicStream = AudioSystem.getAudioInputStream(new File("Sounds/Megalovania_Swing.wav"));
                bossClip = AudioSystem.getClip();
                bossClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(MainRunningThing_BackUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainRunningThing_BackUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(MainRunningThing_BackUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        toggle_practice = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                formPropertyChange(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jPanel1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jPanel1FocusLost(evt);
            }
        });
        jPanel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jPanel1PropertyChange(evt);
            }
        });
        jPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPanel1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 894, Short.MAX_VALUE)
        );

        jButton1.setText("R");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        toggle_practice.setText("Practice Mode");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(toggle_practice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 724, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(toggle_practice)))
                .addGap(32, 32, 32)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        
    }//GEN-LAST:event_formKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (charState == CharacterState.NORMAL || charState == CharacterState.MOVING || charState == CharacterState.FASTMOVING) {
            charState = CharacterState.RESTARTING;
            isSwitching = true;
            opacity = 10;
            transitioning = new Color(180, 180, 180);
            repaint();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanel1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel1KeyReleased
        // TODO add your handling code here:
        if (levelBlocks.length == 0)
            return;
        //System.out.println(evt.getKeyCode());
        
        switch(evt.getKeyCode()) {
            case 'R':
                if (charState == CharacterState.NORMAL || charState == CharacterState.MOVING || charState == CharacterState.FASTMOVING) {
                    charState = CharacterState.RESTARTING;
                    isSwitching = true;
                    opacity = 10;
                    transitioning = new Color(180, 180, 180);
                    repaint();
                }
                break;
            case 'P':
                toggle_practice.setSelected(!toggle_practice.isSelected());
                break;
            case 'K':
                levelFinished();
                break;
                
        }
        if(charState != CharacterState.NORMAL)
            return;
        switch(evt.getKeyCode()) {
            case RIGHT_KEY_PRESS: 
            case 'D':
                if (xPosition == levelBlocks[0].length - 1)
                    return;
                if (levelBlocks[yPosition][xPosition + 1] == null || !levelBlocks[yPosition][xPosition + 1].stepable)
                    return;
                xPosition++;
                break;
            case LEFT_KEY_PRESS: 
            case 'A':
                if (xPosition == 0)
                    return;
                if (levelBlocks[yPosition][xPosition - 1] == null || !levelBlocks[yPosition][xPosition - 1].stepable)
                    return;
                xPosition--;
                break;
            case DOWN_KEY_PRESS: 
            case 'S':
                if (yPosition == levelBlocks.length - 1)
                    return;
                if (levelBlocks[yPosition + 1][xPosition] == null || !levelBlocks[yPosition + 1][xPosition].stepable)
                    return;
                yPosition++;
                break;
            case UP_KEY_PRESS: 
            case 'W':
                if (yPosition == 0)
                    return;
                if (levelBlocks[yPosition - 1][xPosition] == null || !levelBlocks[yPosition - 1][xPosition].stepable)
                    return;
                yPosition--;
                break;
        }
        xTarg = startx + xPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        yTarg = starty + yPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        if (charState == CharacterState.NORMAL) {
            if (levels[currentLevelIndex] instanceof Level.BossLevel) 
                charState = CharacterState.FASTMOVING;
            else
                charState = CharacterState.MOVING;
        }
        repaint();
    }//GEN-LAST:event_jPanel1KeyReleased

    private void jPanel1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel1FocusLost
        // TODO add your handling code here:
        jPanel1.requestFocus();
    }//GEN-LAST:event_jPanel1FocusLost

    private void jPanel1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jPanel1PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1PropertyChange

    private void formPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_formPropertyChange
        // TODO add your handling code here:
        middlex = jPanel1.getWidth() / 2;
        middley = jPanel1.getHeight() / 2;
    }//GEN-LAST:event_formPropertyChange

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws InterruptedException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainRunningThing_BackUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainRunningThing_BackUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainRunningThing_BackUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainRunningThing_BackUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

                
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainRunningThing_BackUp().setVisible(true);
            }
        });
    }
    
    // Loads levelBlocks
    public void loadLevel() {
        timestamp = 0;
        blasts.clear();
        Level currentLevel = levels[currentLevelIndex];
        if (currentLevel instanceof Level.BossLevel) {
            timestamp = bossTestStartTime;
        }
            
        String[][] currentLevelDesign = currentLevel.design;
        
        levelBlocks = new Block[currentLevelDesign.length][currentLevelDesign[0].length];
        
        // Set up levelBlocks - the design of the level in Blocks
        for (int rowNumber = 0; rowNumber < currentLevelDesign.length; rowNumber++) {
            for (int columnNumber = 0; columnNumber < currentLevelDesign[0].length; columnNumber++) {
                
                Block hold = null;
                if(currentLevelDesign[rowNumber][columnNumber] == null || 
                        "".equals(currentLevelDesign[rowNumber][columnNumber]))
                    continue;
                switch(currentLevelDesign[rowNumber][columnNumber].charAt(0)) {
                    case Level.START_CHAR:
                        hold = new Block.NormalBlock();
                        break;
                        
                    case Level.END_CHAR:
                        hold = new Block.EndingBlock();
                        break;
                        
                    case Level.NORMAL_BLOCK_CHAR:
                        hold = new Block.NormalBlock();
                        break;
                        
                    case Level.LAUNCHER_BLOCK_CHAR:
                        switch(currentLevelDesign[rowNumber][columnNumber].charAt(1)) {
                            case '^':
                                hold = new Block.LauncherBlock(Block.Direction.UP);
                                break;
                            case '>':
                                hold = new Block.LauncherBlock(Block.Direction.RIGHT);
                                break;
                            case 'v':
                                hold = new Block.LauncherBlock(Block.Direction.DOWN);
                                break;
                            case '<':
                                hold = new Block.LauncherBlock(Block.Direction.LEFT);
                                break;
                        }
                        break;
                        
                    case Level.BLASTER_BLOCK_CHAR:
                        int dbs = Integer.parseInt(currentLevelDesign[rowNumber][columnNumber].substring(2,4));
                        int fbs = Integer.parseInt(currentLevelDesign[rowNumber][columnNumber].substring(4,6));
                        int delay = 1;
                        if (currentLevelDesign[rowNumber][columnNumber].length() > 6)
                            delay = Integer.parseInt(currentLevelDesign[rowNumber][columnNumber].substring(6, 8));
                        
                        switch(currentLevelDesign[rowNumber][columnNumber].charAt(1)) {
                            case '^':
                                hold = new Block.BlasterBlock(Block.Direction.UP, dbs, fbs, delay);
                                break;
                            case '>':
                                hold = new Block.BlasterBlock(Block.Direction.RIGHT, dbs, fbs, delay);
                                break;
                            case 'v':
                                hold = new Block.BlasterBlock(Block.Direction.DOWN, dbs, fbs, delay);
                                break;
                            case '<':
                                hold = new Block.BlasterBlock(Block.Direction.LEFT, dbs, fbs, delay);
                                break;
                        }
                        break;
                        
                    case Level.CANNON_BLOCK_CHAR:
                        dbs = Integer.parseInt(currentLevelDesign[rowNumber][columnNumber].substring(1,3));
                        fbs = Integer.parseInt(currentLevelDesign[rowNumber][columnNumber].substring(3,5));
                        delay = 1;
                        if (currentLevelDesign[rowNumber][columnNumber].length() > 5)
                            delay = Integer.parseInt(currentLevelDesign[rowNumber][columnNumber].substring(5, 7));
                        
                        hold = new Block.CannonBlock(dbs, fbs, delay);
                        
                        break;
                }
                levelBlocks[rowNumber][columnNumber] = hold;
            }
            
        }
        xPosition = currentLevel.startPosCol;
        yPosition = currentLevel.startPosRow;
        startx = middlex - (levelBlocks[0].length - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
        starty = middley - (levelBlocks.length - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
        endx = 2 * middlex - startx;
        endy = 2 * middley - starty;
        xTarg = startx + xPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        yTarg = starty + yPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        xCoordinates = xTarg;
        yCoordinates = yTarg;
        
        if (currentLevel instanceof Level.BossLevel) {
            currentLevelHealth = ((Level.BossLevel) currentLevel).levelHP;
            if (musicOn) {
                clipTimeHolder = clip.getMicrosecondPosition();
                clip.stop();
                try {
                    bossClip.open(bossMusicStream);
                    bossClip.start();
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(MainRunningThing_BackUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainRunningThing_BackUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        } else {
            currentLevelHealth = 1;
            if (musicOn) {
                if (!clip.isActive()) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    clip.start();
                }
                if (bossClip.isActive())
                    bossClip.stop();
            }
        }
        currentHP = currentLevelHealth;
        isPracticeMode = toggle_practice.isSelected();
        if (isPracticeMode)
            currentHP = PRACTICE_MODE_LIVES;
        iframeStart = 0;
        isIFrame = false;
        lineExplosions.clear();
    }
    
    // Resets the level without reloading
    public void resetLevel() {
        charState = CharacterState.RESTARTING;
        for (Block[] row : levelBlocks)
            for (Block cell : row)
                if (cell != null) cell.reset();
        xPosition = levels[currentLevelIndex].startPosCol;
        yPosition = levels[currentLevelIndex].startPosRow;
        xTarg = startx + xPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        yTarg = starty + yPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        xCoordinates = xTarg;
        yCoordinates = yTarg;
        timestamp = 0;
        if (levels[currentLevelIndex] instanceof Level.BossLevel) {
            timestamp = bossTestStartTime;
        }
        blasts.clear();
        lineExplosions.clear();
        
        isIFrame = false;
        iframeStart = 0;
        currentHP = currentLevelHealth;
        isPracticeMode = toggle_practice.isSelected();
        if (isPracticeMode)
            currentHP = PRACTICE_MODE_LIVES;
        if (levels[currentLevelIndex] instanceof SJBossFight) {
            bossClip.stop();
            bossClip.setMicrosecondPosition(0);
            bossClip.start();
        }
    }
    
    public void levelFinished() {
        if (currentLevelIndex >= levels.length - 1)
            return;
        charState = CharacterState.WINE;
        isSwitching = true;
        opacity = 10;
        transitioning = new Color(255, 255, 255);
        repaint();
    }
    
    public void ouch() {
        if (!isIFrame && (charState == CharacterState.NORMAL || charState == CharacterState.MOVING || charState == CharacterState.FASTMOVING)) {
            currentHP--;
            if (currentHP <= 0)
                death();
            else {
                isIFrame = true;
                iframeStart = timestamp;
            }
        }
    }
    
    public void death() {
        if (charState == CharacterState.NORMAL || charState == CharacterState.MOVING || charState == CharacterState.FASTMOVING){
            charState = CharacterState.DEAD;
            isSwitching = true;
            opacity = 10;
            transitioning = new Color(0, 0, 0);
            deathCount[currentLevelIndex]++;
            totalDeathCount++;
            repaint();
        }
    }
    
    public void landChecker() {
         if (xPosition < 0 || yPosition < 0 || xPosition >= levelBlocks[0].length || yPosition >= levelBlocks.length) {
            death();
        } else if (levelBlocks[yPosition][xPosition] instanceof Block.LauncherBlock) {
            switch(((Block.LauncherBlock) levelBlocks[yPosition][xPosition]).direction) {
                case UP :
                    while(yPosition > 0 && (levelBlocks[yPosition - 1][xPosition] != null && !levelBlocks[yPosition - 1][xPosition].stepable
                            || levelBlocks[yPosition - 1][xPosition] == null))
                        yPosition--;
                    yPosition--;
                    charState = CharacterState.FASTMOVING;
                    break;
                case RIGHT :
                    while(xPosition < levelBlocks[0].length && (levelBlocks[yPosition][xPosition + 1] != null &&
                            !levelBlocks[yPosition][xPosition + 1].stepable || levelBlocks[yPosition][xPosition + 1] == null )) {
                         xPosition++;
                    }
                    xPosition++;
                    charState = CharacterState.FASTMOVING;
                    break;
                case DOWN :
                    while(yPosition < levelBlocks.length && (levelBlocks[yPosition + 1][xPosition] != null && 
                            !levelBlocks[yPosition + 1][xPosition].stepable  || levelBlocks[yPosition + 1][xPosition] == null))
                        yPosition++;
                    yPosition++;
                    charState = CharacterState.FASTMOVING;
                    break;
                case LEFT :
                    while(xPosition > 0 && (levelBlocks[yPosition][xPosition - 1] != null 
                            && !levelBlocks[yPosition][xPosition - 1].stepable  || levelBlocks[yPosition][xPosition - 1] == null ))
                        xPosition--;
                    xPosition--;
                    charState = CharacterState.FASTMOVING;
                    break;
            }
            xTarg = startx + xPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
            yTarg = starty + yPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        } else {
            charState = CharacterState.NORMAL;
            shouldRepaint = true;
        }
    }
    
    @Override
    public void paint(Graphics g) {
        clipholder = new Area();
        ouchArea = new Area();
        if (middlex != jPanel1.getWidth() / 2) {
            middlex = jPanel1.getWidth() / 2;
            startx = middlex - (levelBlocks[0].length - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
            endx = middlex * 2 - startx;
            clipholder.add(new Area(new Rectangle(0, 0, jPanel1.getWidth(), jPanel1.getHeight())));
        }
        if (middley != jPanel1.getHeight() / 2) {
            middley = jPanel1.getHeight() / 2;
            starty = middley - (levelBlocks.length - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
            endx = middlex * 2 - startx;
            clipholder.add(new Area(new Rectangle(0, 0, jPanel1.getWidth(), jPanel1.getHeight())));
        }
        
        if (levelBlocks.length == 0) {
            System.out.println("Instantiate levelBlocks!");
            loadLevel();
            charState = CharacterState.NORMAL;
        }
        if (startx == 0 || starty == 0) {
            startx = middlex - (levelBlocks[0].length - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
            starty = middley - (levelBlocks.length - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
        }
        
        if (isSwitching) {
            if (opacity < 240)
                opacity += 48;
            else {
                isSwitching = false;
                switch (charState) {
                    case WINE:
                        currentLevelIndex++;
                        loadLevel();
                        break;
                    case RESTARTING:
                        resetLevel();
                        break;
                    case DEAD:
                        resetLevel();
                        break;
                }
                System.out.println(charState);
            }
        } else if (opacity > 15) {
            opacity -= 48;
            if (opacity < 15)
                charState = CharacterState.NORMAL;
            clipholder.add(new Area(new Rectangle(0, 0, jPanel1.getWidth(), jPanel1.getHeight())));
        }
        
        
        
            /*
        for (Block[] row : levelBlocks)
            for (Block cell : row)
                System.out.println(cell);*/
        jPanel1.setBackground(Color.white);
        Graphics currG = jPanel1.getGraphics();
        g.setColor(new Color(207, 226, 243));
        g.fillRect(0, 0, this.getWidth(), 120);
        g.setColor(Color.BLACK);
        g.fillRect(this.getWidth() / 2 - 27, 48, 54, 54);
        g.setColor(Color.WHITE);
        g.fillRect(this.getWidth() / 2 - 25, 50, 50, 50);
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(18f));
        g.drawString(levels[currentLevelIndex].levelLabel, this.getWidth() / 2 - 20, 70);
        g.drawString("TimeStamp: "+ timestamp, this.getWidth() - 300, 80);
        g.drawString("Practice Mode: "+ (isPracticeMode ? "On" : "Off"), this.getWidth() - 300, 100);
        g.drawString("Health: " + currentHP, 300, 100);
        g.drawString("Death Count (Level): " + deathCount[currentLevelIndex], 300, 80);
        g.drawString("Death Count (Total): " + totalDeathCount, 300, 60);
        
        
        BlastCalculator bc = new BlastCalculator();
        bc.start();
        
        
        
        
        
        
        
        /// Levelspecific testing
        
        
        if (levels[currentLevelIndex].levelLabel.equals("0")) {
            if (timestamp % 30 == 10)
                lineExplosions.add(new LineExploderTester(timestamp, 20, Math.PI, 
                    startx + SPACING_BETWEEN_BLOCKS * ((int) (Math.random() * 4)) + 2 * STANDARD_ICON_WIDTH, starty - 80 + SPACING_BETWEEN_BLOCKS * 6));

            if (timestamp % 30 == 5)
                lineExplosions.add(new LineExploderTester(timestamp, 20, 0, 
                        startx + SPACING_BETWEEN_BLOCKS * ((int) (Math.random() * 4)) + 2 * STANDARD_ICON_WIDTH, starty - 80));

            if (timestamp % 15 == 10)
                lineExplosions.add(new LineExploderTester(timestamp, 20, 0, 
                        startx + SPACING_BETWEEN_BLOCKS * ((int) (Math.random() * 4)) + 2 * STANDARD_ICON_WIDTH, starty - 80));

            if (timestamp % 15 == 5)
                lineExplosions.add(new LineExploderTester(timestamp, 20, Math.PI * .5, 
                        startx + SPACING_BETWEEN_BLOCKS * 4 + 2 * STANDARD_ICON_WIDTH, starty - 80 + SPACING_BETWEEN_BLOCKS * ((int) (Math.random() * 5) + 1)));
        
        }
        
        if (levels[currentLevelIndex].levelLabel.equals("-1")) {
             
            if (timestamp % 12 == 1) {
                blasts.add(new SJBossFight.FlyingBone(0, 20));
                blasts.get(blasts.size() - 1).setCoords(startx, starty + 100);
                blasts.add(new SJBossFight.FlyingBone(0, 20));
                blasts.get(blasts.size() - 1).setCoords(startx, starty + 100 + SPACING_BETWEEN_BLOCKS * 3);
            }
            
            if (timestamp % 30 == 8) {
                blasts.add(new SJBossFight.FlyingBone(Math.PI, 40));
                blasts.get(blasts.size() - 1).setCoords(endx, starty + 40);
            }
            
            if (timestamp % 30 == 5) {
                blasts.add(new SJBossFight.FlyingBone(Math.PI, 40));
                blasts.get(blasts.size() - 1).setCoords(endx, starty + 40 + SPACING_BETWEEN_BLOCKS * 3);
            }
            if (timestamp % 30 == 17) {
                blasts.add(new SJBossFight.FlyingBone(Math.PI, 40));
                blasts.get(blasts.size() - 1).setCoords(endx, starty + 40 + SPACING_BETWEEN_BLOCKS * 4);
            }
            
            if (timestamp % 12 == 5) {
                blasts.add(new SJBossFight.FlyingBone(Math.PI / 2, 20));
                blasts.get(blasts.size() - 1).setCoords(startx + 100, starty);
                blasts.add(new SJBossFight.FlyingBone(Math.PI / 2, 20));
                blasts.get(blasts.size() - 1).setCoords(startx + 100 + SPACING_BETWEEN_BLOCKS * 3, starty);
            }
        }
          
        
        if (levels[currentLevelIndex].levelLabel.equals("-2")) {
            
            if (timestamp % 30 == 4) {
                blasts.add(new SJBossFight.ArcingBone(Math.PI / 2, 40, 0, -4, 0));
                blasts.get(blasts.size() - 1).setCoords(startx, starty + 40);
            }
            
            if (timestamp % 20 == 4) {
                blasts.add(new SJBossFight.ArcingBone(Math.PI / 2, -20, 0, -3, 0));
                blasts.get(blasts.size() - 1).setCoords(endx, starty + 40);
            }
            
            if (timestamp % 20 == 14) {
                blasts.add(new SJBossFight.ArcingBone(Math.PI / 2, -20, 0, -3, 0));
                blasts.get(blasts.size() - 1).setCoords(endx, starty + 40 + SPACING_BETWEEN_BLOCKS);
            }
            
            if (timestamp % 25 == 2) {
                blasts.add(new SJBossFight.ArcingBone(0, 0, 20, 0, 3));
                blasts.get(blasts.size() - 1).setCoords(startx + 40 + SPACING_BETWEEN_BLOCKS, starty);
            }
            
            if (timestamp % 30 == 4) {
                blasts.add(new SJBossFight.ArcingBone(3 * Math.PI / 4, 30, 30, -2, -2));
                blasts.get(blasts.size() - 1).setCoords(startx + 2, starty);
            }
            
            if (timestamp % 30 == 20) {
                blasts.add(new SJBossFight.RotatingBone(3 * Math.PI / 4, -28, -28, 0, 0, 0.4));
                blasts.get(blasts.size() - 1).setCoords(endx, endy);
            }
            
            if (timestamp % 30 == 20) {
                blasts.add(new SJBossFight.RotatingBone(3 * Math.PI / 4, 28, 0, 2, 0, 0.4));
                blasts.get(blasts.size() - 1).setCoords(startx, endy - STANDARD_ICON_WIDTH / 2);
            }
            
            if (timestamp % 30 == 5) {
                blasts.add(new SJBossFight.RotatingBone(3 * Math.PI / 4, 28, 0, 2, 0, 0.4));
                blasts.get(blasts.size() - 1).setCoords(startx, endy - STANDARD_ICON_WIDTH / 2 - SPACING_BETWEEN_BLOCKS);
            }
            
        }
        
        if (levels[currentLevelIndex].levelLabel.equals("-3")) {
            if (timestamp % 30 == 5) {
                blasts.add(new SJBossFight.BoneExploder(0, 0, 0, 0, 2, .4, STANDARD_ICON_WIDTH));
                blasts.get(blasts.size() - 1).setCoords(startx + STANDARD_ICON_WIDTH / 2 + SPACING_BETWEEN_BLOCKS * (int) (Math.random() * 5 + 1), starty - 8);
            }
            
            if (timestamp % 30 == 20) {
                blasts.add(new SJBossFight.BoneExploder(0, 0, 0, 0, -2, .4, STANDARD_ICON_WIDTH));
                blasts.get(blasts.size() - 1).setCoords(startx + STANDARD_ICON_WIDTH / 2 + SPACING_BETWEEN_BLOCKS * (int) (Math.random() * 5 + 1), endy + 8);
            }
            
            if (timestamp % 30 == 13) {
                blasts.add(new SJBossFight.BoneExploder(0, 0, 0, 2, 0, .4, STANDARD_ICON_WIDTH));
                blasts.get(blasts.size() - 1).setCoords(startx - 8, starty + STANDARD_ICON_WIDTH / 2 + SPACING_BETWEEN_BLOCKS * (int) (Math.random() * 5 + 1));
            }
            
            if (timestamp % 30 == 28) {
                blasts.add(new SJBossFight.BoneExploder(0, 0, 0, -2, 0, .4, STANDARD_ICON_WIDTH));
                blasts.get(blasts.size() - 1).setCoords(endx + 8, starty + STANDARD_ICON_WIDTH / 2 + SPACING_BETWEEN_BLOCKS * (int) (Math.random() * 5 + 1));
            }
            
            if (timestamp % 20 == 10) {
                double ang = getAngle(xCoordinates - middlex, yCoordinates - middley);
                blasts.add(new SJBossFight.ArcingBone(ang + Math.PI / 2, -20 * Math.cos(ang), -20 * Math.sin(ang),  5 * Math.cos(ang), 5 * Math.sin(ang)));
                blasts.get(blasts.size() - 1).setCoords(middlex, middley);
            }
        }
        
        
        if (levels[currentLevelIndex].levelLabel.equals("-4")) {
            if (timestamp == 2) {
                blasts.add(new SJBossFight.RotatingBone(0, 0, 0, 0, 0, -.055));
                blasts.get(blasts.size() - 1).setCoords(middlex, middley);
                ((SJBossFight.RotatingBone) blasts.get(blasts.size() - 1)).setDimensions(500, 30);
            }
            
            if (timestamp % 20 == 2) {
                blasts.add(new SJBossFight.RotatingBone(0, 0, -3, 0, -2, 0.1));
                blasts.get(blasts.size() - 1).setCoords(startx + STANDARD_ICON_WIDTH / 2 + SPACING_BETWEEN_BLOCKS * 5 / 2, endy);
                ((SJBossFight.RotatingBone) blasts.get(blasts.size() - 1)).setDimensions(200, 30);
            }
            
            if (timestamp % 20 == 4) {
                blasts.add(new SJBossFight.RotatingBone(0, 0, 3, 0, 2, -0.1));
                blasts.get(blasts.size() - 1).setCoords(startx + STANDARD_ICON_WIDTH / 2 + SPACING_BETWEEN_BLOCKS * 9 / 2, starty);
                ((SJBossFight.RotatingBone) blasts.get(blasts.size() - 1)).setDimensions(200, 30);
            }
        }
        
        
        if (levels[currentLevelIndex] instanceof Level.BossLevel) {
            lineExplosions.addAll(((Level.BossLevel) levels[currentLevelIndex]).generateLines(timestamp, xCoordinates, yCoordinates, startx, starty, STANDARD_ICON_WIDTH, SPACING_BETWEEN_BLOCKS));
            
        }
        
        
        
        
        
        for (int i = 0; i < lineExplosions.size(); i++) {
            Level.BossLevel.LineExploder currentLineExplosion = lineExplosions.get(i);
            ouchArea.add(currentLineExplosion.xplosionOuchArea(timestamp));
        }
        
        // Checks to see if the character is still moving
        if (charState == CharacterState.MOVING) {
            if (xCoordinates != xTarg) {
                if (Math.abs(xCoordinates - xTarg) <= CHARACTER_SPEED) {
                    xCoordinates = xTarg;
                } else 
                    xCoordinates += xCoordinates > xTarg ? -CHARACTER_SPEED : CHARACTER_SPEED;
            }
            if (yCoordinates != yTarg) {
                if (Math.abs(yCoordinates - yTarg) <= CHARACTER_SPEED) {
                    yCoordinates = yTarg;
                } else 
                    yCoordinates += yCoordinates > yTarg ? -CHARACTER_SPEED : CHARACTER_SPEED;
            }
            if (xCoordinates == xTarg && yCoordinates == yTarg) {
                landChecker();
            }
            clipholder.add(new Area(new Rectangle(xCoordinates - CHARACTER_SPEED, yCoordinates - CHARACTER_SPEED, 
                    CHARACTER_WIDTH + 2 * CHARACTER_SPEED,  CHARACTER_WIDTH + 2 * CHARACTER_SPEED)));
        }

        // Checks to see if the character is fastmoving
        if (charState == CharacterState.FASTMOVING) {
            if (xCoordinates != xTarg) {
                if (Math.abs(xCoordinates - xTarg) <= CHARACTER_FASTSPEED) {
                    xCoordinates = xTarg;
                } else 
                    xCoordinates += xCoordinates > xTarg ? -CHARACTER_FASTSPEED : CHARACTER_FASTSPEED;
            }
            if (yCoordinates != yTarg) {
                if (Math.abs(yCoordinates - yTarg) <= CHARACTER_FASTSPEED) {
                    yCoordinates = yTarg;
                } else 
                    yCoordinates += yCoordinates > yTarg ? -CHARACTER_FASTSPEED : CHARACTER_FASTSPEED;
            }
            if (xCoordinates == xTarg && yCoordinates == yTarg) {
                landChecker();
            }
            clipholder.add(new Area(new Rectangle(xCoordinates - CHARACTER_FASTSPEED, yCoordinates - CHARACTER_FASTSPEED, 
                    CHARACTER_WIDTH + 2 * CHARACTER_FASTSPEED,  CHARACTER_WIDTH + 2 * CHARACTER_FASTSPEED)));
        }

        if (xPosition == levels[currentLevelIndex].endPosCol && yPosition == levels[currentLevelIndex].endPosRow && !isSwitching && !(opacity > 15) && charState == CharacterState.NORMAL) {
            if (isPracticeMode) {
                charState = CharacterState.RESTARTING;
                isSwitching = true;
                opacity = 10;
                transitioning = new Color(180, 180, 180);
                repaint();
            }
            else
                levelFinished();
        }
        
        if (levels[currentLevelIndex] instanceof Level.BossLevel && ((Level.BossLevel) levels[currentLevelIndex]).endtime <= timestamp && !isSwitching && !(opacity > 15) && charState == CharacterState.NORMAL) {
            
            System.out.println("Hey times up");
            if (isPracticeMode) {
                charState = CharacterState.RESTARTING;
                isSwitching = true;
                opacity = 10;
                transitioning = new Color(180, 180, 180);
                repaint();
            }
            else
                levelFinished();
        }
        
        
        
        try {
            bc.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MainRunningThing_BackUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        if (opacity < 15 || charState == CharacterState.DEAD || charState == CharacterState.WINE && isSwitching)
            timestamp += 1;
        
        if (ouchArea.intersects(new Rectangle(xCoordinates, yCoordinates, CHARACTER_WIDTH, CHARACTER_WIDTH))) {
            ouch();
        }
        
        // Setting clip
        if (levels[currentLevelIndex] instanceof Level.BossLevel) {
            clipholder.add(new Area(new Rectangle(jPanel1.getWidth(), jPanel1.getHeight())));
        } else {
            for (int i = 0; i < blasts.size(); i ++) {
                Block.BlasterBlock.Blast bla = blasts.get(i);
                clipholder.add(bla.getClip());
            }
            
            if (opacity > 15)
                clipholder.add(new Area(new Rectangle(0, 0, jPanel1.getWidth(), jPanel1.getHeight())));

            if (isIFrame && iframeStart + iframeTime >= timestamp) {
                clipholder.add(new Area(new Rectangle(xCoordinates, yCoordinates, CHARACTER_WIDTH, CHARACTER_WIDTH)));
            }

            for (int i = 0; i < lineExplosions.size(); i++) {
                clipholder.add(lineExplosions.get(i).xplosionClipArea(timestamp));
            }
        }
        
        ///PAINTINGG!!!
        currG.setClip(clipholder);
        currG.clearRect(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        currG.setColor(Color.WHITE);
        currG.fillRect(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        
        if (levels[currentLevelIndex] instanceof Level.BossLevel)
            ((Level.BossLevel) levels[currentLevelIndex]).drawBackground(currG, timestamp, jPanel1, startx, starty, STANDARD_ICON_WIDTH, SPACING_BETWEEN_BLOCKS);
        
        for (int rowNumber = 0; rowNumber < levelBlocks.length; rowNumber++) 
            for (int columnNumber = 0; columnNumber < levelBlocks[0].length; columnNumber++) 
                if (levelBlocks[rowNumber][columnNumber] != null)
                    levelBlocks[rowNumber][columnNumber].icon.paintIcon(jPanel1, currG, startx + columnNumber * SPACING_BETWEEN_BLOCKS, starty + rowNumber * SPACING_BETWEEN_BLOCKS);
        
        characterIconAlive.paintIcon(jPanel1, currG, xCoordinates, yCoordinates);
        
        if (isIFrame && iframeStart + iframeTime >= timestamp) {
            currG.setColor(new Color(0, 0, 255, timestamp % 2 == 0 ? 50 : 100));
            currG.fillRect(xCoordinates, yCoordinates, CHARACTER_WIDTH, CHARACTER_WIDTH);
            if (iframeStart + iframeTime <= timestamp) {
                isIFrame = false;
            }
        }
        
        if (SEE_OVERLAP) {
            currG.setColor(Color.black);
            ((Graphics2D) currG).draw(ouchArea);
            currG.setColor(Color.red);
            ((Graphics2D) currG).draw(clipholder);
        }
        
        
        for (int i = 0; i < blasts.size(); i++) {
            Block.BlasterBlock.Blast bla = blasts.get(i);
            
            if (bla.xcoord < startx - 10 || bla.ycoord < starty - 10 || bla.xcoord > endx + 10 || bla.ycoord > endy + 10) {

                if (bla instanceof Block.HighExplosion) {
                    double holdangle = 0;
                    if (bla.xcoord > endx + 10) 
                        holdangle = Math.PI / 2;
                    else if (bla.xcoord < startx - 10)
                        holdangle = 3 * Math.PI / 2;
                    else if (bla.ycoord > endy + 10)
                        holdangle = Math.PI;
                    else
                        holdangle = 0;
                    lineExplosions.add(((Block.HighExplosion) bla).getLineExplosion(timestamp, holdangle, bla.xcoord, bla.ycoord));
                }

                blasts.remove(i);
                i--;
                letsseeifthisworks = true;
            } else {
                bla.draw(currG, jPanel1);
            }
        }
        
        for (int i = 0; i < lineExplosions.size(); i++) {
            Level.BossLevel.LineExploder le = lineExplosions.get(i);
            le.drawXPlosion(jPanel1, currG, timestamp);
            if (le.starttime + le.timelength <= timestamp)
                lineExplosions.remove(i);
        }
        
        
        
        // Does the fading animations
        if (opacity > 15) {
            currG.setColor(new Color(transitioning.getRed(), transitioning.getGreen(), transitioning.getBlue(), opacity));
            currG.fillRect(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        }
        
        if (levels[currentLevelIndex] instanceof Level.BossLevel)
            ((Level.BossLevel) levels[currentLevelIndex]).drawForeground(currG, timestamp, jPanel1, startx, starty, STANDARD_ICON_WIDTH, SPACING_BETWEEN_BLOCKS);
        
        // Gets the blocks to show up on first run
        if (opacity > 15 || xCoordinates != xTarg || yCoordinates != yTarg || shouldRepaint || blasts.size() > 0 || lineExplosions.size() > 0 || letsseeifthisworks || levels[currentLevelIndex] instanceof Level.BossLevel) {
            if (letsseeifthisworks)
                letsseeifthisworks = false;
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainRunningThing_BackUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            shouldRepaint = false;
            repaint();
        }
        
        
            
    }
    
    public double getAngle(int xdif, int ydif) {
        return Math.PI / 2 - Math.atan2(xdif, ydif);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton toggle_practice;
    // End of variables declaration//GEN-END:variables
    public class BlastCalculator extends Thread {

        @Override
        public void run() {
            for (int rowNumber = 0; rowNumber < levelBlocks.length; rowNumber++) {
                for (int columnNumber = 0; columnNumber < levelBlocks[0].length; columnNumber++) {
                    Block currBlock = levelBlocks[rowNumber][columnNumber];
                    if (currBlock == null)
                        continue;
                    
                    //Generates blasts at correct time
                    if (!(charState == CharacterState.DEAD) && !(charState == CharacterState.RESTARTING) && !(charState == CharacterState.WINE)) {
                        
                        if (currBlock instanceof Block.BlasterBlock) { 

                            if (timestamp % ((Block.BlasterBlock) currBlock).period == ((Block.BlasterBlock) currBlock).delay){
                                blasts.add(new Block.BlasterBlock.Blast(((Block.BlasterBlock) currBlock).direction, ((Block.BlasterBlock) currBlock).blastSpeed));

                                switch(((Block.BlasterBlock) currBlock).direction) {
                                    case UP:
                                        blasts.get(blasts.size() - 1).setCoords(startx + columnNumber * SPACING_BETWEEN_BLOCKS + 32, starty + rowNumber * SPACING_BETWEEN_BLOCKS);
                                        break;
                                    case LEFT:
                                        blasts.get(blasts.size() - 1).setCoords(startx + columnNumber * SPACING_BETWEEN_BLOCKS, starty + rowNumber * SPACING_BETWEEN_BLOCKS + 32);
                                        break;
                                    case RIGHT:
                                        blasts.get(blasts.size() - 1).setCoords(startx + columnNumber * SPACING_BETWEEN_BLOCKS + 30, starty + rowNumber * SPACING_BETWEEN_BLOCKS + 32);
                                        break;
                                    case DOWN:
                                        blasts.get(blasts.size() - 1).setCoords(startx + columnNumber * SPACING_BETWEEN_BLOCKS + 32, starty + rowNumber * SPACING_BETWEEN_BLOCKS + 30);
                                        break;
                                }

                                ((Block.BlasterBlock) currBlock).primed = false;
                            }
                        }
                        
                        
                        if (currBlock instanceof Block.CannonBlock) {
                            if (timestamp % ((Block.CannonBlock) currBlock).period == ((Block.CannonBlock) currBlock).delay) {
                                int xcenter = startx + columnNumber * SPACING_BETWEEN_BLOCKS + STANDARD_ICON_WIDTH / 2;
                                int ycenter = starty + rowNumber * SPACING_BETWEEN_BLOCKS + STANDARD_ICON_WIDTH / 2;
                                blasts.add(new Block.CannonBlock.Cannonball(((Block.CannonBlock) currBlock).cannonballSpeed, 
                                        getAngle(xCoordinates + CHARACTER_WIDTH / 2 - xcenter, yCoordinates + CHARACTER_WIDTH / 2 - ycenter)));
                                blasts.get(blasts.size() - 1).setCoords(xcenter, ycenter);
                            }
                        }
                        
                    }
                    
                }
            }
            
            if (levels[currentLevelIndex] instanceof Level.BossLevel) {
                blasts.addAll(((Level.BossLevel) levels[currentLevelIndex]).generateBlasts(timestamp, xCoordinates, yCoordinates, startx, starty, STANDARD_ICON_WIDTH, SPACING_BETWEEN_BLOCKS));
            }

            // Calculates where the blasts would be
            for (int i = 0; i < blasts.size(); i++) {
                Block.BlasterBlock.Blast bla = blasts.get(i);
                bla.move();
                ouchArea.add(bla.getOuchArea());
            }
        }
        
    }


}

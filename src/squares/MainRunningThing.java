/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package squares;

import squares.block.LauncherBlock;
import squares.block.Block;
import squares.block.HighExplosion;
import squares.block.BlasterBlock;
import squares.block.CannonBlock;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

import squares.api.CharacterState;
import squares.api.ResourceLocator;

import static squares.api.RenderingConstants.*;

/**
 *
 * @author lai_889937
 */
public class MainRunningThing extends javax.swing.JFrame {
    private static final long serialVersionUID = 3017531681090479808L;

    Instant timestart;
    Instant timeend;

    public Player player;

    // Music!!
    public AudioManager audio = new AudioManager();

    // Level indices
    public int currentLevelIndex = 0; // INDEX of current level (add one to get level number)
    public int maxLevelIndex = currentLevelIndex;
    public int holdLevelIndex = currentLevelIndex;

    public static final int RIGHT_KEY_PRESS = 39;
    public static final int LEFT_KEY_PRESS = 37;
    public static final int DOWN_KEY_PRESS = 40;
    public static final int UP_KEY_PRESS = 38;

    public ArrayList<BlasterBlock.Blast> blasts = new ArrayList<>();
    public ArrayList<Level.BossLevel.LineExploder> lineExplosions = new ArrayList<>();

    public Color transitioning = null;
    public boolean isSwitching = false;
    public int opacity = 0;
    public boolean letsseeifthisworks = true;
    public int middlex; // middle of panel
    public int middley;
    public int startx; // where to start drawing block
    public int starty;
    public boolean shouldRepaint = false;
    public int timestamp;
    public int endx; //Border of block drawings; used to determine when ammunition disappears
    public int endy;

    public int currentLevelHealth = 1;

    public Area clipholder;
    public Area ouchArea;

    public static final ImageIcon characterIconAlive = new ImageIcon("Pics/Character.png", "Character image");

    // Dev tools for testing stuff
    public static final int bossTestStartTime = 0;
    public static final int sleepTime = 100;
    public static final boolean SEE_OVERLAP = false;
    public boolean musicOn = true;

    // Checkpoints
    public TreeSet<Integer> checkpointTimes = new TreeSet<>();
    public boolean tasActive = false;
    public TasGenerator sjbossTas = new TasGenerator(new ResourceLocator("data", "sjbossscript.txt"));

    // Easter eggs
    public static final String KONAMI_CODE = "uuddlrlrba";
    public static final ImageIcon LEAGIF = new ImageIcon("Pics/lea.gif");
    public boolean isLeaGif = false;
    public static final ImageIcon PIEPNG = new ImageIcon(new ImageIcon("Pics/pie.png").getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));

    // All the levels. All of them.
    public Level[] levels = new Level[]{

        new Level(new String[][]{
            new String[]{"X", "N", "O"}
        },
         "1", "Ap_lEs"),
        new Level(new String[][]{
            new String[]{"X", "", "N", "O"},
            new String[]{"N", "", "N", ""},
            new String[]{"N", "N", "N", ""}
        },
         "2", "BaKAr_Na"),
        new Level(new String[][]{
            new String[]{"X", "L>", "", "N", "O"}
        },
         "3", "P0TaTo_$"),
        new Level(new String[][]{
            new String[]{"X", "N", "Lv"},
            new String[]{"", "", ""},
            new String[]{"Lv", "N", "N"},
            new String[]{"", "", ""},
            new String[]{"N", "N", "O"}
        },
         "4", "C_oSsC0dE"),
        new Level(new String[][]{
            new String[]{"X", "", "N", "Lv", "N", "O"},
            new String[]{"N", "", "", "", "", ""},
            new String[]{"L>", "", "", "", "", "N"},
            new String[]{"N", "", "", "", "", "L<"},
            new String[]{"L>", "", "", "", "", "N"},
            new String[]{"N", "", "", "", "", "L<"},
            new String[]{"N", "N", "L^", "N", "L^", ""}
        },
         "5", "_eLe$tE"),
        new Level(new String[][]{
            new String[]{"", "", "L>", "", "Lv"},
            new String[]{"X", "L>", "", "Lv", ""},
            new String[]{"", "", "", "", ""},
            new String[]{"O", "", "", "", "L<"},
            new String[]{"", "", "L^", "L<", ""},},
         "6", "UnD_rTAle"),
        new Level(new String[][]{
            new String[]{"X", "L>", "Lv", "N", "Lv", "L>", "N"},
            new String[]{"Lv", "", "", "L^", "", "", "L<"},
            new String[]{"L>", "", "", "", "", "", "Lv"},
            new String[]{"N", "L^", "", "", "", "", "N"},
            new String[]{"L^", "Lv", "", "", "", "", "L<"},
            new String[]{"L^", "", "", "L<", "L>", "", "Lv"},
            new String[]{"N", "L<", "L>", "N", "L>", "L^", "O"}
        },
         "7", "BoWsEtTe"),
        new Level(new String[][]{
            new String[]{"", "", "Bv153005", ""},
            new String[]{"", "", "", ""},
            new String[]{"X", "N", "N", "O"}
        },
         "8", "0vER_Ord"),
        new Level(new String[][]{
            new String[]{"X", "Bv2030", "N", "O"},
            new String[]{"N", "B>1530", "N", "B<1530"},
            new String[]{"N", "N", "N", ""}
        },
         "9", "KoN0sUb_"),
        new Level(new String[][]{
            new String[]{"X", "Bv2540", "N", "O"},
            new String[]{"Lv", "", "", "B<1530"},
            new String[]{"", "", "", "B<2025"},
            new String[]{"N", "N", "L^", ""}
        },
         "10", "NGaMeNL_fe"),
        new Level(new String[][]{
            new String[]{"X", "", "", ""},
            new String[]{"N", "N", "B<3020", ""},
            new String[]{"N", "", "", ""},
            new String[]{"N", "N", "N", ""},
            new String[]{"", "", "N", "B<1530"},
            new String[]{"B^3520", "", "O", ""}
        },
         "11", "BonG0cAT"),
        new Level(new String[][]{
            new String[]{"X", "Bv2015", "N", "Lv", "N", "O"},
            new String[]{"N", "Bv3020", "", "", "", ""},
            new String[]{"L>", "", "", "", "", "N"},
            new String[]{"N", "", "", "", "", "L<"},
            new String[]{"L>", "", "", "", "", "N"},
            new String[]{"N", "", "B>3040", "", "", "L<"},
            new String[]{"N", "N", "L^", "N", "L^", "B<4015"}
        },
         "12", "HyPErfAnG"),
        new Level(new String[][]{
            new String[]{"X", "Bv104001", "Bv104002", "Bv104003", ""},
            new String[]{"N", "", "", "", "B<2015"},
            new String[]{"N", "", "", "", "B<201503"},
            new String[]{"N", "", "", "", "B<201505"},
            new String[]{"L>", "", "", "", "N"},
            new String[]{"O", "", "", "", "L<"}
        },
         "13", "SHaDoWs"),
        new Level(new String[][]{
            new String[]{"X", "", "N", "O"},
            new String[]{"N", "", "N", "B<1520"},
            new String[]{"N", "N", "N", "B<2525"},
            new String[]{"", "N", "B^3010", ""}
        },
         "14", "SwRDrTnLn"),
        new Level(new String[][]{
            new String[]{"", "", "C153005", ""},
            new String[]{"", "", "", ""},
            new String[]{"X", "N", "N", "O"}
        },
         "15", "L0gHriZn"),
        new Level(new String[][]{
            new String[]{"X", "C2020", "N", "O"},
            new String[]{"N", "C1510", "N", "C1510"},
            new String[]{"N", "N", "N", ""}
        },
         "16", "1PnchMaN"),
        new Level(new String[][]{
            new String[]{"B>302505", "X", "C2020", "N", "Lv", "N", "Lv"},
            new String[]{"B>201505", "Lv", "", "", "", "", ""},
            new String[]{"O", "", "", "", "", "", "L<"},
            new String[]{"B>302520", "N", "N", "L^", "N", "L^", "C2020"}
        },
         "17", "c6H12o6"),
        new Level(new String[][]{
            new String[]{"", "X", "N", "", "", "Bv302025", ""},
            new String[]{"", "N", "", "C202004", "", "", ""},
            new String[]{"N", "N", "", "", "", "N", "O"},
            new String[]{"", "N", "", "", "N", "N", ""},
            new String[]{"N", "N", "", "", "", "N", "N"},
            new String[]{"", "N", "N", "N", "N", "N", "B<3020"},
            new String[]{"", "B^302005", "N", "", "N", "", ""}
        },
         "18", "CelsAtwRk"),
        new SJBossFight(new String[][]{
            new String[]{"", "", "", "", "", "", "", "", ""},
            new String[]{"", "", "", "", "", "", "", "", ""},
            new String[]{"", "", "N", "N", "N", "N", "N", "", ""},
            new String[]{"", "", "N", "N", "N", "N", "N", "", ""},
            new String[]{"", "", "N", "N", "X", "N", "N", "", ""},
            new String[]{"", "", "N", "N", "N", "N", "N", "", ""},
            new String[]{"", "", "N", "N", "N", "N", "N", "", ""},
            new String[]{"", "", "", "", "", "", "", "", ""},
            new String[]{"", "", "", "", "", "", "", "", ""}
        },
         "BOSS", 20, new ResourceLocator("data", "sjbossfight_easy.txt"), "YoUJ0snKi"),
        new Level(new String[][]{
            new String[]{"", "", "", "", "", "", ""},
            new String[]{"", "X", "N", "N", "N", "N", ""},
            new String[]{"", "N", "N", "N", "N", "N", ""},
            new String[]{"", "C201010", "", "", "", "N", ""},
            new String[]{"", "N", "N", "N", "N", "N", ""},
            new String[]{"", "O", "N", "N", "N", "N", ""},
            new String[]{"", "", "", "", "", "", ""}
        },
         "-4", "No!n0!i"),
        new Level(new String[][]{
            new String[]{"", "", "", "", "", "", ""},
            new String[]{"", "X", "N", "N", "N", "N", ""},
            new String[]{"", "N", "N", "N", "N", "N", ""},
            new String[]{"", "C201010", "", "", "", "N", ""},
            new String[]{"", "N", "N", "N", "N", "N", ""},
            new String[]{"", "O", "N", "N", "N", "N", ""},
            new String[]{"", "", "", "", "", "", ""}
        },
         "-3", "foRKnSpO0n"),
        new Level(new String[][]{
            new String[]{"X", "N", "N", "N", "N"},
            new String[]{"N", "N", "N", "N", "N"},
            new String[]{"C201010", "", "", "", "N"},
            new String[]{"N", "N", "N", "N", "N"},
            new String[]{"O", "N", "N", "N", "N"}
        },
         "-2", "CppErHeD"),
        new Level(new String[][]{
            new String[]{"X", "N", "", "N", "N", "N"},
            new String[]{"", "N", "N", "N", "", "N"},
            new String[]{"C201005", "", "", "C201005", "N", "N"},
            new String[]{"N", "N", "N", "", "N", ""},
            new String[]{"O", "", "N", "N", "N", ""}
        },
         "-1", "ThIsiShaRD"),
        new Level(new String[][]{
            new String[]{"X", "N", "N", "N", "N"},
            new String[]{"N", "N", "N", "N", "N"},
            new String[]{"C201005", "", "", "", "N"},
            new String[]{"N", "N", "N", "N", "N"},
            new String[]{"O", "N", "N", "N", "N"}
        },
         "0", "cHaoSchA0s"),
        new Level(new String[][]{
            new String[]{"X", "N", "O"}
        },
         "1", "soEasyyy"),
        new SJBossFight(new String[][]{
            new String[]{"", "", "", "", "", "", "", "", ""},
            new String[]{"", "", "", "", "", "", "", "", ""},
            new String[]{"", "", "N", "N", "N", "N", "N", "", ""},
            new String[]{"", "", "N", "N", "N", "N", "N", "", ""},
            new String[]{"", "", "N", "N", "X", "N", "N", "", ""},
            new String[]{"", "", "N", "N", "N", "N", "N", "", ""},
            new String[]{"", "", "N", "N", "N", "N", "N", "", ""},
            new String[]{"", "", "", "", "", "", "", "", ""},
            new String[]{"", "", "", "", "", "", "", "", ""}
        },
         "BOSS", 10, new ResourceLocator("data", "sjbossfight.txt"), "wHThvIdn"),
        new Level(new String[][]{
            new String[]{"Lv", "N", "", "L<", "N"},
            new String[]{"", "Lv", "N", "N", "N"},
            new String[]{"", "", "X", "", ""},
            new String[]{"N", "N", "N", "L^", ""},
            new String[]{"N", "L>", "", "N", "L^"}
        },
         "CONGRATS!", "tHxF0rPlynG")
    };

    // Deaths
    public int[] deathCount = new int[levels.length];
    public int totalDeathCount = 0;

    /**
     * Creates new form MainRunningThing
     */
    public MainRunningThing() {
        initComponents();
        jPanel1.setFocusable(true);
        jButton1.setFocusable(false);
        toggle_practice.setFocusable(false);
        jText_levelCode.setFocusable(false);
        jSpinner_Level.setFocusable(false);
        jButton1.setVisible(true);
        middlex = jPanel1.getWidth() / 2;
        middley = jPanel1.getHeight() / 2;
        player = new Player();
        player.level = levels[0];

        if (musicOn) {
            audio.addClip("normal", new ResourceLocator("bgm", "Canon_in_D_Swing.wav"))
                 .addClip("boss",   new ResourceLocator("bgm", "Megalovania_Swing.wav"));
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
        jText_levelCode = new javax.swing.JTextField();
        jSpinner_Level = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();

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
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
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
            .addGap(0, 873, Short.MAX_VALUE)
        );

        jButton1.setText("R");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        toggle_practice.setText("Practice Mode");

        jText_levelCode.setText("Level Code");
        jText_levelCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jText_levelCodeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jText_levelCodeMouseEntered(evt);
            }
        });
        jText_levelCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jText_levelCodeActionPerformed(evt);
            }
        });

        jSpinner_Level.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jSpinner_Level.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_LevelStateChanged(evt);
            }
        });
        jSpinner_Level.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jSpinner_LevelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jSpinner_LevelMouseEntered(evt);
            }
        });
        jSpinner_Level.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSpinner_LevelPropertyChange(evt);
            }
        });

        jLabel1.setText("Level Select");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(toggle_practice)
                            .addComponent(jText_levelCode, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 706, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addComponent(jSpinner_Level, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(861, 861, 861))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jText_levelCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(toggle_practice)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jSpinner_Level, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased

    }//GEN-LAST:event_formKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (player.charState.vulnerable) {
            player.charState = CharacterState.RESTARTING;
            isSwitching = true;
            opacity = 10;
            transitioning = new Color(180, 180, 180);
            repaint();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    @SuppressWarnings("fallthrough")
    private void jPanel1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel1KeyReleased
        // TODO add your handling code here:
        if (player.level.blocks.length == 0) {
            return;
        }

        switch (evt.getKeyCode()) {
            case 'T': // toggles autoplay
                if (player.level instanceof SJBossFight) {
                    tasActive = !tasActive;
                } else {
                    break;
                }
            case 'R':
                if (player.charState.vulnerable) {
                    player.charState = CharacterState.RESTARTING;
                    isSwitching = true;
                    opacity = 10;
                    transitioning = new Color(180, 180, 180);
                    repaint();
                }
                break;
            case 'P': // toggle practice mode
                toggle_practice.setSelected(!toggle_practice.isSelected());
                break;
            case 'K': // skip level
                levelFinished();
                break;
            case 'C': // add checkpoint
                if (player.level instanceof Level.BossLevel && player.isPracticeMode) {
                    checkpointTimes.add(timestamp);
                }
                break;
            case 'V': // remove checkpoint
                if (player.level instanceof Level.BossLevel && player.isPracticeMode) {
                    if (checkpointTimes.size() > 0) {
                        checkpointTimes.pollLast();
                    }
                }
            case 'B': // clear all checkpoints
                checkpointTimes.clear();

        }
        if (tasActive) {
            return;
        }
        if(player.charState != CharacterState.NORMAL) {
            return;
        }
        switch (evt.getKeyCode()) { // move
            case RIGHT_KEY_PRESS:
            case 'D':
                if (player.xPosition == player.level.blocks[0].length - 1)
                    return;
                if (player.level.blocks[player.yPosition][player.xPosition + 1] == null || !player.level.blocks[player.yPosition][player.xPosition + 1].stepable)
                    return;
                player.xPosition++;
                break;
            case LEFT_KEY_PRESS:
            case 'A':
                if (player.xPosition == 0)
                    return;
                if (player.level.blocks[player.yPosition][player.xPosition - 1] == null || !player.level.blocks[player.yPosition][player.xPosition - 1].stepable)
                    return;
                player.xPosition--;
                break;
            case DOWN_KEY_PRESS:
            case 'S':
                if (player.yPosition == player.level.blocks.length - 1)
                    return;
                if (player.level.blocks[player.yPosition + 1][player.xPosition] == null || !player.level.blocks[player.yPosition + 1][player.xPosition].stepable)
                    return;
                player.yPosition++;
                break;
            case UP_KEY_PRESS:
            case 'W':
                if (player.yPosition == 0)
                    return;
                if (player.level.blocks[player.yPosition - 1][player.xPosition] == null || !player.level.blocks[player.yPosition - 1][player.xPosition].stepable)
                    return;
                player.yPosition--;
                break;
        }
        player.xTarg = startx + player.xPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        player.yTarg = starty + player.yPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        if (player.charState == CharacterState.NORMAL) {
            if (player.level instanceof Level.BossLevel) 
                player.charState = CharacterState.FASTMOVING;
            else
                player.charState = CharacterState.MOVING;
        }
        repaint();
    }//GEN-LAST:event_jPanel1KeyReleased

    private void jPanel1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel1FocusLost
        // TODO add your handling code here:
        //jPanel1.requestFocus();
    }//GEN-LAST:event_jPanel1FocusLost

    private void jPanel1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jPanel1PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1PropertyChange

    private void formPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_formPropertyChange
        // TODO add your handling code here:
        middlex = jPanel1.getWidth() / 2;
        middley = jPanel1.getHeight() / 2;
    }//GEN-LAST:event_formPropertyChange

    private void jText_levelCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jText_levelCodeActionPerformed
        // This does all of the level code input calculations
        String code = jText_levelCode.getText();
        System.out.println(code);
        if (code.toLowerCase().equals(KONAMI_CODE)) {
            isLeaGif = true;
            return;
        }
        int hold = -1;
        for (int i = 0; i < levels.length; i++) {
            if (code.equals(levels[i].getCode())) {
                hold = i;
                break;
            }
        }
        if (hold == -1) {
            System.out.println("Not a code.");
        } else {
            holdLevelIndex = hold;
            levelFinished();
        }
        //jText_levelCode.setFocusable(false);
    }//GEN-LAST:event_jText_levelCodeActionPerformed

    private void jText_levelCodeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jText_levelCodeMouseEntered
        // TODO add your handling code here:
        jText_levelCode.setVisible(true);
    }//GEN-LAST:event_jText_levelCodeMouseEntered

    private void jText_levelCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jText_levelCodeMouseClicked
        // TODO add your handling code here:
        jText_levelCode.setVisible(true);
        jText_levelCode.setFocusable(true);
        jText_levelCode.requestFocus();
    }//GEN-LAST:event_jText_levelCodeMouseClicked

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        // TODO add your handling code here:
        jPanel1.requestFocus();
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jSpinner_LevelPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSpinner_LevelPropertyChange

    }//GEN-LAST:event_jSpinner_LevelPropertyChange

    private void jSpinner_LevelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSpinner_LevelMouseClicked
        // TODO add your handling code here:
        jSpinner_Level.setVisible(true);
        jSpinner_Level.setFocusable(true);
        jSpinner_Level.requestFocus();
    }//GEN-LAST:event_jSpinner_LevelMouseClicked

    private void jSpinner_LevelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_LevelStateChanged
        // TODO add your handling code here:
        int requested = Integer.parseInt(jSpinner_Level.getValue().toString()) - 1;
        if (requested <= maxLevelIndex && currentLevelIndex != requested) {
            holdLevelIndex = requested;
            levelFinished();
        } else {
            jSpinner_Level.setValue(currentLevelIndex + 1);
        }
    }//GEN-LAST:event_jSpinner_LevelStateChanged

    private void jSpinner_LevelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSpinner_LevelMouseEntered
        // TODO add your handling code here:
        jSpinner_Level.setVisible(true);
    }//GEN-LAST:event_jSpinner_LevelMouseEntered

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
            java.util.logging.Logger.getLogger(MainRunningThing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainRunningThing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainRunningThing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainRunningThing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainRunningThing().setVisible(true);
            }
        });
    }

    // Loads player.level.blocks
    public void loadLevel() {

        tasActive = false;
        checkpointTimes.clear();
        timestamp = 0;
        blasts.clear();
        Level currentLevel = player.level;

        if (player.level instanceof Level.BossLevel && !tasActive) {
            if (player.isPracticeMode && checkpointTimes.size() > 0) {
                timestamp = checkpointTimes.last();
            } else {
                timestamp = bossTestStartTime;
            }
        }

        String[][] currentLevelDesign = currentLevel.design;

        // Set up currentLevel.block - the design of the level in Blocks
        player.xPosition = currentLevel.startPosCol;
        player.yPosition = currentLevel.startPosRow;
        startx = middlex - (currentLevel.blocks[0].length - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
        starty = middley - (currentLevel.blocks.length - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
        endx = 2 * middlex - startx;
        endy = 2 * middley - starty;
        player.xTarg = startx + player.xPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        player.yTarg = starty + player.yPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        player.xCoordinates = player.xTarg;
        player.yCoordinates = player.yTarg;

        if (currentLevel instanceof Level.BossLevel) {
            currentLevelHealth = ((Level.BossLevel) currentLevel).levelHP;
            if (musicOn) {
                audio.setPlaying("boss", timestamp * audio.getClip("boss").getMicrosecondLength() / 1556);
            }
        } else {
            currentLevelHealth = 1;
            if (musicOn) {
                audio.setPlaying("normal");
            }
        }
        player.hp = currentLevelHealth;
        player.isPracticeMode = toggle_practice.isSelected();
        if (player.isPracticeMode) {
            player.hp = Player.PRACTICE_MODE_LIVES;
        }
        player.iftime = -11;
        lineExplosions.clear();
    }

    // Resets the level without reloading
    public void resetLevel() {
        player.charState = CharacterState.RESTARTING;
        for (Block[] row : player.level.blocks)
            for (Block cell : row)
                if (cell != null) cell.reset();
        player.xPosition = player.level.startPosCol;
        player.yPosition = player.level.startPosRow;
        player.xTarg = startx + player.xPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        player.yTarg = starty + player.yPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        player.xCoordinates = player.xTarg;
        player.yCoordinates = player.yTarg;
        timestamp = 0;
        if (player.level instanceof Level.BossLevel && !tasActive) {
            if (player.isPracticeMode && checkpointTimes.size() > 0) {
                timestamp = checkpointTimes.last();
            } else {
                timestamp = bossTestStartTime;
            }
        }
        blasts.clear();
        lineExplosions.clear();

        player.iftime = 0;
        player.hp = currentLevelHealth;
        player.isPracticeMode = toggle_practice.isSelected();
        if (player.isPracticeMode) {
            player.hp = Player.PRACTICE_MODE_LIVES;
        }
        if (player.level instanceof SJBossFight && musicOn) {
            audio.setPlaying("boss", timestamp * audio.getClip("boss").getMicrosecondLength() / 1556);
        }
    }

    public void levelFinished() {
        if (currentLevelIndex >= levels.length - 1) {
            return;
        }
        player.charState = CharacterState.WINE;
        isSwitching = true;
        opacity = 10;
        transitioning = new Color(255, 255, 255);
        repaint();
    }

    public void ouch() {
        if (!player.isInvincible(timestamp) && player.charState.vulnerable) {
            player.hp--;
            if (player.hp <= 0) {
                death();
            } else {
                player.iftime = timestamp;
            }
        }
    }

    // on death
    public void death() {
        if (player.charState.vulnerable) {
            player.charState = CharacterState.DEAD;
            isSwitching = true;
            opacity = 10;
            transitioning = new Color(0, 0, 0);
            deathCount[currentLevelIndex]++;
            totalDeathCount++;
            repaint();
        }
    }

    //  checks the character when the character lands at its destination
    public void landChecker() {
         if (player.xPosition < 0 || player.yPosition < 0 || player.xPosition >= player.level.blocks[0].length || player.yPosition >= player.level.blocks.length) {
            death();
        } else if (player.level.blocks[player.yPosition][player.xPosition] instanceof LauncherBlock) {
            switch(((LauncherBlock) player.level.blocks[player.yPosition][player.xPosition]).getDirection()) {
                case UP :
                    while(player.yPosition > 0 && (player.level.blocks[player.yPosition - 1][player.xPosition] != null && !player.level.blocks[player.yPosition - 1][player.xPosition].stepable
                            || player.level.blocks[player.yPosition - 1][player.xPosition] == null))
                        player.yPosition--;
                    player.yPosition--;
                    player.charState = CharacterState.FASTMOVING;
                    break;
                case RIGHT :
                    while(player.xPosition < player.level.blocks[0].length && (player.level.blocks[player.yPosition][player.xPosition + 1] != null &&
                            !player.level.blocks[player.yPosition][player.xPosition + 1].stepable || player.level.blocks[player.yPosition][player.xPosition + 1] == null )) {
                         player.xPosition++;
                    }
                    player.xPosition++;
                    player.charState = CharacterState.FASTMOVING;
                    break;
                case DOWN :
                    while(player.yPosition < player.level.blocks.length && (player.level.blocks[player.yPosition + 1][player.xPosition] != null && 
                            !player.level.blocks[player.yPosition + 1][player.xPosition].stepable  || player.level.blocks[player.yPosition + 1][player.xPosition] == null))
                        player.yPosition++;
                    player.yPosition++;
                    player.charState = CharacterState.FASTMOVING;
                    break;
                case LEFT :
                    while(player.xPosition > 0 && (player.level.blocks[player.yPosition][player.xPosition - 1] != null 
                            && !player.level.blocks[player.yPosition][player.xPosition - 1].stepable  || player.level.blocks[player.yPosition][player.xPosition - 1] == null ))
                        player.xPosition--;
                    player.xPosition--;
                    player.charState = CharacterState.FASTMOVING;
                    break;
            }
            player.xTarg = startx + player.xPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
            player.yTarg = starty + player.yPosition * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        } else {
            player.charState = CharacterState.NORMAL;
            shouldRepaint = true;
        }
    }

    @Override
    public void paint(Graphics g) {
        clipholder = new Area();
        ouchArea = new Area();
        if (middlex != jPanel1.getWidth() / 2) {
            middlex = jPanel1.getWidth() / 2;
            startx = middlex - (player.level.blocks[0].length - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
            endx = middlex * 2 - startx;
            clipholder.add(new Area(new Rectangle(0, 0, jPanel1.getWidth(), jPanel1.getHeight())));
        }
        if (middley != jPanel1.getHeight() / 2) {
            middley = jPanel1.getHeight() / 2;
            starty = middley - (player.level.blocks.length - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
            endx = middlex * 2 - startx;
            clipholder.add(new Area(new Rectangle(0, 0, jPanel1.getWidth(), jPanel1.getHeight())));
        }

        if (player.level.blocks.length == 0) {
            System.out.println("Instantiate player.level.blocks!");
            loadLevel();
            player.charState = CharacterState.NORMAL;
        }
        if (startx == 0 || starty == 0) {
            startx = middlex - (player.level.blocks[0].length - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
            starty = middley - (player.level.blocks.length - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
        }

        // Does the level switching / restarting stuff
        if (isSwitching) {
            if (opacity < 240) {
                opacity += 48;
            } else {
                isSwitching = false;
                switch (player.charState) {
                    case WINE:
                        if (holdLevelIndex != currentLevelIndex) {
                            currentLevelIndex = holdLevelIndex;
                            player.level = levels[currentLevelIndex]; // Fuck.
                        } else { 
                            holdLevelIndex = ++currentLevelIndex;
                            player.level = levels[currentLevelIndex]; // Fuck.
                        }
                        if (currentLevelIndex > maxLevelIndex) {
                            maxLevelIndex = currentLevelIndex;
                        }
                        loadLevel();
                        break;
                    case RESTARTING:
                        resetLevel();
                        break;
                    case DEAD:
                        resetLevel();
                        break;
                }
                System.out.println(player.charState);
                isLeaGif = false;
            }
        } else if (opacity > 15) {
            opacity -= 48;
            if (opacity < 15)
                player.charState = CharacterState.NORMAL;
            clipholder.add(new Area(new Rectangle(0, 0, jPanel1.getWidth(), jPanel1.getHeight())));
        }

        BlastCalculator bc = new BlastCalculator(); // opens new thread for blasts
        bc.start();

        /// Levelspecific testing
        if (player.level.levelLabel.equals("0")) {
            if (timestamp % 30 == 10) {
                lineExplosions.add(new LineExploderTester(timestamp, 20, Math.PI,
                        startx + SPACING_BETWEEN_BLOCKS * ((int) (Math.random() * 4)) + 2 * STANDARD_ICON_WIDTH, starty - 80 + SPACING_BETWEEN_BLOCKS * 6));
            }

            if (timestamp % 30 == 5) {
                lineExplosions.add(new LineExploderTester(timestamp, 20, 0,
                        startx + SPACING_BETWEEN_BLOCKS * ((int) (Math.random() * 4)) + 2 * STANDARD_ICON_WIDTH, starty - 80));
            }

            if (timestamp % 15 == 10) {
                lineExplosions.add(new LineExploderTester(timestamp, 20, 0,
                        startx + SPACING_BETWEEN_BLOCKS * ((int) (Math.random() * 4)) + 2 * STANDARD_ICON_WIDTH, starty - 80));
            }

            if (timestamp % 15 == 5) {
                lineExplosions.add(new LineExploderTester(timestamp, 20, Math.PI * .5,
                        startx + SPACING_BETWEEN_BLOCKS * 4 + 2 * STANDARD_ICON_WIDTH, starty - 80 + SPACING_BETWEEN_BLOCKS * ((int) (Math.random() * 5) + 1)));
            }

        }

        if (player.level.levelLabel.equals("-1")) {

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

        if (player.level.levelLabel.equals("-2")) {

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

        if (player.level.levelLabel.equals("-3")) {
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
                double ang = getAngle(player.xCoordinates - middlex, player.yCoordinates - middley);
                blasts.add(new SJBossFight.ArcingBone(ang + Math.PI / 2, -20 * Math.cos(ang), -20 * Math.sin(ang),  5 * Math.cos(ang), 5 * Math.sin(ang)));
                blasts.get(blasts.size() - 1).setCoords(middlex, middley);
            }
        }

        if (player.level.levelLabel.equals("-4")) {
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

        if (player.level instanceof Level.BossLevel) {
            lineExplosions.addAll(((Level.BossLevel) player.level).generateLines(timestamp, player.xCoordinates, player.yCoordinates, startx, starty, STANDARD_ICON_WIDTH, SPACING_BETWEEN_BLOCKS));
        }

        if (timestamp == 1200 && timestart != null) {
            timeend = Instant.now();
            System.out.println(Duration.between(timestart, timeend).toMillis());
        }


        int charYUpper = player.yCoordinates;
        int charXLeft = player.xCoordinates;
        int charYLower = player.yCoordinates + CHARACTER_WIDTH;
        int charXRight = player.xCoordinates + CHARACTER_WIDTH;
        for (int i = 0; i < lineExplosions.size(); i++) {
            Level.BossLevel.LineExploder currentLineExplosion = lineExplosions.get(i);
            Area ouchyline = currentLineExplosion.xplosionOuchArea(timestamp);
            Rectangle rect = ouchyline.getBounds();
            if (rect.getMinX() <= charXRight && rect.getMaxX() >= charXLeft && rect.getMinY() <= charYUpper && rect.getMaxY() >= charYLower) {
                ouchArea.add(currentLineExplosion.xplosionOuchArea(timestamp));
            }
        }

        if (tasActive && player.level instanceof SJBossFight) {
            if(sjbossTas.doTasStuff(startx, starty, timestamp, player))
                repaint();
        }

        // Checks to see if the character is still moving
        if (player.charState == CharacterState.MOVING) {
            if (player.xCoordinates != player.xTarg) {
                if (Math.abs(player.xCoordinates - player.xTarg) <= CHARACTER_SPEED) {
                    player.xCoordinates = player.xTarg;
                } else 
                    player.xCoordinates += player.xCoordinates > player.xTarg ? -CHARACTER_SPEED : CHARACTER_SPEED;
            }
            if (player.yCoordinates != player.yTarg) {
                if (Math.abs(player.yCoordinates - player.yTarg) <= CHARACTER_SPEED) {
                    player.yCoordinates = player.yTarg;
                } else 
                    player.yCoordinates += player.yCoordinates > player.yTarg ? -CHARACTER_SPEED : CHARACTER_SPEED;
            }
            if (player.xCoordinates == player.xTarg && player.yCoordinates == player.yTarg) {
                landChecker();
            }
            clipholder.add(new Area(new Rectangle(player.xCoordinates - CHARACTER_SPEED, player.yCoordinates - CHARACTER_SPEED, 
                    CHARACTER_WIDTH + 2 * CHARACTER_SPEED,  CHARACTER_WIDTH + 2 * CHARACTER_SPEED)));
        }

        // Checks to see if the character is fastmoving
        if (player.charState == CharacterState.FASTMOVING) {
            if (player.xCoordinates != player.xTarg) {
                if (Math.abs(player.xCoordinates - player.xTarg) <= CHARACTER_FASTSPEED) {
                    player.xCoordinates = player.xTarg;
                } else 
                    player.xCoordinates += player.xCoordinates > player.xTarg ? -CHARACTER_FASTSPEED : CHARACTER_FASTSPEED;
            }
            if (player.yCoordinates != player.yTarg) {
                if (Math.abs(player.yCoordinates - player.yTarg) <= CHARACTER_FASTSPEED) {
                    player.yCoordinates = player.yTarg;
                } else 
                    player.yCoordinates += player.yCoordinates > player.yTarg ? -CHARACTER_FASTSPEED : CHARACTER_FASTSPEED;
            }
            if (player.xCoordinates == player.xTarg && player.yCoordinates == player.yTarg) {
                landChecker();
            }
            clipholder.add(new Area(new Rectangle(player.xCoordinates - CHARACTER_FASTSPEED, player.yCoordinates - CHARACTER_FASTSPEED, 
                    CHARACTER_WIDTH + 2 * CHARACTER_FASTSPEED,  CHARACTER_WIDTH + 2 * CHARACTER_FASTSPEED)));
        }

        if (player.xPosition == player.level.endPosCol && player.yPosition == player.level.endPosRow && !isSwitching && !(opacity > 15) && player.charState == CharacterState.NORMAL) {
            if (player.isPracticeMode) {
                player.charState = CharacterState.RESTARTING;
                isSwitching = true;
                opacity = 10;
                transitioning = new Color(180, 180, 180);
                repaint();
            } else {
                levelFinished();
            }
        }

        if (player.level instanceof Level.BossLevel && ((Level.BossLevel) player.level).endtime <= timestamp && !isSwitching && !(opacity > 15) && player.charState == CharacterState.NORMAL) {
            System.out.println("Hey times up");
            if (tasActive || player.isPracticeMode) {
                tasActive = false;
                player.charState = CharacterState.RESTARTING;
                isSwitching = true;
                opacity = 10;
                transitioning = new Color(180, 180, 180);
                repaint();
            } else {
                levelFinished();
            }
        }

        try {
            bc.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MainRunningThing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        if (opacity < 15 || player.charState == CharacterState.DEAD || (player.charState == CharacterState.WINE || player.charState == CharacterState.RESTARTING) && isSwitching)
            timestamp += 1;

        if (ouchArea.intersects(new Rectangle(player.xCoordinates, player.yCoordinates, CHARACTER_WIDTH, CHARACTER_WIDTH))) {
            ouch();
        }

        // Setting clip
        if (player.level instanceof Level.BossLevel) {
            clipholder = new Area(new Rectangle(0, 0, jPanel1.getWidth(), jPanel1.getHeight()));
        } else {
            for (int i = 0; i < blasts.size(); i++) {
                BlasterBlock.Blast bla = blasts.get(i);
                clipholder.add(bla.getClip());
            }

            if (opacity > 15) {
                clipholder.add(new Area(new Rectangle(0, 0, jPanel1.getWidth(), jPanel1.getHeight())));
            }

            if (player.isInvincible(timestamp)) {
                clipholder.add(new Area(new Rectangle(player.xCoordinates, player.yCoordinates, CHARACTER_WIDTH, CHARACTER_WIDTH)));
            }

            for (int i = 0; i < lineExplosions.size(); i++) {
                clipholder.add(lineExplosions.get(i).xplosionClipArea(timestamp));
            }

            if (timestamp >= 314 && timestamp <= 335) { // Sets clip for the pie
                clipholder.add(new Area(new Rectangle(0, 0, PIEPNG.getIconWidth(), PIEPNG.getIconHeight())));
            }

        }

        ///PAINTINGG!!!
        
        // Draws the game panel
        jPanel1.setBackground(Color.white);
        Graphics currG = jPanel1.getGraphics();
        currG.setClip(clipholder);
        if (player.level instanceof Level.BossLevel) {
            ((Graphics2D) currG).setBackground(((Level.BossLevel) player.level).getBackgroundColor(timestamp));
        } else {
            ((Graphics2D) currG).setBackground(Color.white);
        }

        currG.clearRect(0, 0, jPanel1.getWidth(), jPanel1.getHeight());

        if (player.level instanceof Level.BossLevel) {
            ((Level.BossLevel) player.level).drawBackground(currG, timestamp, jPanel1, startx, starty, STANDARD_ICON_WIDTH, SPACING_BETWEEN_BLOCKS);
        }

        for (int rowNumber = 0; rowNumber < player.level.blocks.length; rowNumber++) 
            for (int columnNumber = 0; columnNumber < player.level.blocks[0].length; columnNumber++) 
                if (player.level.blocks[rowNumber][columnNumber] != null)
                    player.level.blocks[rowNumber][columnNumber].getIcon().paintIcon(jPanel1, currG, startx + columnNumber * SPACING_BETWEEN_BLOCKS, starty + rowNumber * SPACING_BETWEEN_BLOCKS);

        characterIconAlive.paintIcon(jPanel1, currG, player.xCoordinates, player.yCoordinates);

        if (player.isInvincible(timestamp)) {
            currG.setColor(new Color(0, 0, 255, timestamp % 2 == 0 ? 50 : 100));
            currG.fillRect(player.xCoordinates, player.yCoordinates, CHARACTER_WIDTH, CHARACTER_WIDTH);
        }

        // Dev tool to see ouchArea and the clip
        if (SEE_OVERLAP) {
            currG.setColor(Color.black);
            ((Graphics2D) currG).draw(ouchArea);
            currG.setColor(Color.red);
            ((Graphics2D) currG).draw(clipholder);
        }
        
        // Draws all the blasts and explosions and stuff
        for (int i = 0; i < blasts.size(); i++) {
            BlasterBlock.Blast bla = blasts.get(i);

            if (bla.xcoord < startx - 10 || bla.ycoord < starty - 10 || bla.xcoord > endx + 10 || bla.ycoord > endy + 10) {

                if (bla instanceof HighExplosion) {
                    double holdangle = 0;
                    if (bla.xcoord > endx + 10) {
                        holdangle = Math.PI / 2;
                    } else if (bla.xcoord < startx - 10) {
                        holdangle = 3 * Math.PI / 2;
                    } else if (bla.ycoord > endy + 10) {
                        holdangle = Math.PI;
                    } else {
                        holdangle = 0;
                    }
                    lineExplosions.add(((HighExplosion) bla).getLineExplosion(timestamp, holdangle, bla.xcoord, bla.ycoord));
                }

                blasts.remove(i);
                i--;
                letsseeifthisworks = true;
            } else {
                bla.draw(currG, jPanel1);
            }
        }

        // Gets rid of explosions at correct time
        for (int i = 0; i < lineExplosions.size(); i++) {
            Level.BossLevel.LineExploder le = lineExplosions.get(i);
            le.drawXPlosion(jPanel1, currG, timestamp);
            if (le.starttime + le.timelength <= timestamp) {
                lineExplosions.remove(i);
            }
        }

        // Does the fading animations
        if (opacity > 15) {
            currG.setColor(new Color(transitioning.getRed(), transitioning.getGreen(), transitioning.getBlue(), opacity));
            currG.fillRect(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        }

        if (player.level instanceof Level.BossLevel) {
            ((Level.BossLevel) player.level).drawForeground(currG, timestamp, jPanel1, startx, starty, STANDARD_ICON_WIDTH, SPACING_BETWEEN_BLOCKS);
        }

        // Draws the pie at frame 314
        if (timestamp >= 314 && timestamp <= 334) {
            PIEPNG.paintIcon(jPanel1, currG, 10, 10);
        }

        // Draws the stuff at the top of the screen
        g.setColor(new Color(207, 226, 243));
        g.fillRect(0, 0, this.getWidth(), 120);
        g.setColor(Color.BLACK);
        g.fillRect(this.getWidth() / 2 - 27, 48, 54, 54);
        g.setColor(Color.WHITE);
        g.fillRect(this.getWidth() / 2 - 25, 50, 50, 50);
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(18f));
        g.drawString(player.level.levelLabel, this.getWidth() / 2 - 20, 70);
        g.drawString(String.format("TimeStamp: %d", timestamp), this.getWidth() - 300, 80);
        g.drawString("Practice Mode: ".concat(player.isPracticeMode ? "On" : "Off"), this.getWidth() - 300, 100);
        g.drawString(String.format("Death Count (Total): %d", totalDeathCount), 300, 50);
        g.drawString(String.format("Death Count (Level): %d", deathCount[currentLevelIndex]), 300, 70);
        g.drawString(String.format("Health: %d", player.hp), 300, 90);
        g.drawString(String.format("Level Code: %s", player.level.getCode()), 300, 110);
        
        // Draws Lea
        if (isLeaGif) {
            LEAGIF.paintIcon(this, g, 0, 30);
        }

        // Gets the block to show up on first run
        if (opacity > 15 || player.xCoordinates != player.xTarg || player.yCoordinates != player.yTarg || shouldRepaint || blasts.size() > 0 || lineExplosions.size() > 0 || letsseeifthisworks || player.level instanceof Level.BossLevel) {
            if (letsseeifthisworks) {
                letsseeifthisworks = false;
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainRunningThing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner jSpinner_Level;
    private javax.swing.JTextField jText_levelCode;
    private javax.swing.JToggleButton toggle_practice;
    // End of variables declaration//GEN-END:variables
    public class BlastCalculator extends Thread {

        @Override
        public void run() {
            for (int rowNumber = 0; rowNumber < player.level.blocks.length; rowNumber++) {
                for (int columnNumber = 0; columnNumber < player.level.blocks[0].length; columnNumber++) {
                    Block currBlock = player.level.blocks[rowNumber][columnNumber];
                    if (currBlock == null) {
                        continue;
                    }

                    //Generates blasts at correct time
                    if (!(player.charState == CharacterState.DEAD) && !(player.charState == CharacterState.RESTARTING) && !(player.charState == CharacterState.WINE)) {

                        if (currBlock instanceof BlasterBlock) {

                            if (timestamp % ((BlasterBlock) currBlock).period == ((BlasterBlock) currBlock).delay) {
                                blasts.add(new BlasterBlock.Blast(((BlasterBlock) currBlock).direction, ((BlasterBlock) currBlock).blastSpeed));

                                switch (((BlasterBlock) currBlock).direction) {
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

                                ((BlasterBlock) currBlock).primed = false;
                            }
                        }

                        if (currBlock instanceof CannonBlock) {
                            if (timestamp % ((CannonBlock) currBlock).period == ((CannonBlock) currBlock).delay) {
                                int xcenter = startx + columnNumber * SPACING_BETWEEN_BLOCKS + STANDARD_ICON_WIDTH / 2;
                                int ycenter = starty + rowNumber * SPACING_BETWEEN_BLOCKS + STANDARD_ICON_WIDTH / 2;
                                blasts.add(new CannonBlock.Cannonball(((CannonBlock) currBlock).cannonballSpeed, 
                                        getAngle(player.xCoordinates + CHARACTER_WIDTH / 2 - xcenter, player.yCoordinates + CHARACTER_WIDTH / 2 - ycenter)));
                                blasts.get(blasts.size() - 1).setCoords(xcenter, ycenter);
                            }
                        }

                    }

                }
            }

            if (player.level instanceof Level.BossLevel) {
                blasts.addAll(((Level.BossLevel) player.level).generateBlasts(timestamp, player.xCoordinates, player.yCoordinates, startx, starty, STANDARD_ICON_WIDTH, SPACING_BETWEEN_BLOCKS));
            }

            // Calculates where the blasts would be
            int charYUpper = player.yCoordinates;
            int charXLeft = player.xCoordinates;
            int charYLower = player.yCoordinates + CHARACTER_WIDTH;
            int charXRight = player.xCoordinates + CHARACTER_WIDTH;
            for (int i = 0; i < blasts.size(); i++) {
                BlasterBlock.Blast bla = blasts.get(i);
                bla.move();
                Area blaouch = bla.getOuchArea();
                Rectangle bound = blaouch.getBounds();
                if (bound.getX() > charXRight || bound.getX() + bound.width < charXLeft || bound.getY() > charYLower || bound.getY() + bound.height < charYUpper) {
                } else {
                    ouchArea.add(blaouch);
                }
            }
        }


    }

}

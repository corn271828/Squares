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
import java.awt.geom.Area;
import java.time.Duration;
import java.time.Instant;
import java.util.TreeSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

import squares.api.AABB;
import squares.api.AudioManager;
import squares.api.CharacterState;
import squares.api.Clock;
import squares.api.ResourceLoader;
import squares.api.Coordinate;
import squares.api.entity.Entity;
import squares.api.block.BlockFactory;
import squares.api.level.Level;
import squares.api.level.BossLevel;
import squares.level.LevelLoader;
import squares.level.SJBossFight;

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
    public int maxLevelIndex = 0;
    public int holdLevelIndex = maxLevelIndex;


    public Color transitioning = null;
    public boolean isSwitching = false;
    public int opacity = 0;
    public boolean letsseeifthisworks = true;
    public Coordinate middle = new Coordinate(0, 0); // middle of panel
    public Coordinate start  = new Coordinate(0, 0); // where to start drawing block
    public Coordinate end    = new Coordinate(0, 0); // Border of block drawings; used to determine when ammunition disappears
    public boolean shouldRepaint = false;
    public Clock clock;

    public int currentLevelHealth = 1;

    public Area clipholder;
    public Area ouchArea;

    public static final ImageIcon characterIconAlive = new ResourceLoader("sprites", "Character").asImageIcon();

    // Dev tools for testing stuff
    public static final int bossTestStartTime = 0;
    public static final int sleepTime = 104;
    public static final boolean SEE_OVERLAP = false;
    public boolean musicOn = true;

    // Checkpoints
    public TreeSet<Integer> checkpointTimes = new TreeSet<>();
    public boolean tasActive = false;
    public TasGenerator sjbossTas = new TasGenerator(new ResourceLoader("bossdata", "sjbossscript"));

    // Easter eggs
    public static final String KONAMI_CODE = "uuddlrlrba";
    public static final ImageIcon LEAGIF = new ResourceLoader("sprites", "lea.gif").asImageIcon();
    public boolean isLeaGif = false;
    public static final ImageIcon PIEPNG = new ImageIcon(new ResourceLoader("sprites", "pie").asImageIcon().getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));

    // All the levels. All of them.
    public LevelLoader levelLoader;

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
        middle.x = jPanel1.getWidth() / 2;
        middle.y = jPanel1.getHeight() / 2;
        RegistrationHandler.init();
        clock = new Clock();
        player = new Player(clock, start);
        squares.level.SJBossFight.clock = clock;
        levelLoader = new LevelLoader(new ResourceLoader("data", "leveldata"), new BlockFactory());
        player.level = levelLoader.getCurrent();
        player.deathCb = this::death;

        if (musicOn) {
            audio.addClip("normal", new ResourceLoader("bgm", "Canon_in_D_Swing"))
                 .addClip("boss",   new ResourceLoader("bgm", "Megalovania_Swing"));
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
        if (player.level.ySize() == 0) {
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
                if (player.level instanceof BossLevel && player.isPracticeMode) {
                    checkpointTimes.add(clock.time());
                }
                break;
            case 'V': // remove checkpoint
                if (player.level instanceof BossLevel && player.isPracticeMode) {
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
        
        player.setQueueKey(evt.getKeyCode());
        
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
        middle.x = jPanel1.getWidth() / 2;
        middle.y = jPanel1.getHeight() / 2;
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
        for (int i = 0; i < levelLoader.getNumLevels(); i++) {
            if (code.equals(levelLoader.getCodeForLevel(i))) {
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
        if (requested <= maxLevelIndex && levelLoader.getLevelIndex() != requested) {
            holdLevelIndex = requested;
            levelFinished();
        } else {
            jSpinner_Level.setValue(levelLoader.getLevelIndex() + 1);
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
        clock.reset();
        Level currentLevel = player.level;

        if (player.level instanceof BossLevel && !tasActive) {
            if (player.isPracticeMode && checkpointTimes.size() > 0) {
                clock.setTime(checkpointTimes.last());
            } else {
                clock.setTime(bossTestStartTime);
            }
        }

        // Set up currentLevel.block - the design of the level in Blocks
        player.isPracticeMode = toggle_practice.isSelected();

        player.position.x = currentLevel.getStartPos().x;
        player.position.y = currentLevel.getStartPos().y;
        start.x = middle.x - (currentLevel.xSize() - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
        start.y = middle.y - (currentLevel.ySize() - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
        end.x = 2 * middle.x - start.x;
        end.y = 2 * middle.y - start.y;
        player.target.x = start.x + player.position.x * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        player.target.y = start.y + player.position.y * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        player.render.x = player.target.x;
        player.render.y = player.target.y;

        player.level.setup(player);
        if(musicOn)
            player.level.setupMusic(audio, clock);
    }

    // Resets the level without reloading
    public void resetLevel() {
        player.charState = CharacterState.RESTARTING;
        player.position.x = player.level.getStartPos().x;
        player.position.y = player.level.getStartPos().y;
        player.target.x = start.x + player.position.x * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        player.target.y = start.y + player.position.y * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
        player.render.x = player.target.x;
        player.render.y = player.target.y;
        clock.reset();
        if (player.level instanceof BossLevel && !tasActive) {
            if (player.isPracticeMode && checkpointTimes.size() > 0) {
                clock.setTime(checkpointTimes.last());
            } else {
                clock.setTime(bossTestStartTime);
            }
        }
        player.isPracticeMode = toggle_practice.isSelected();
        player.level.setup(player);
        if (player.isPracticeMode) {
            player.hp = Player.PRACTICE_MODE_LIVES;
        }
        if (musicOn) {
            player.level.setupMusic(audio, clock);
        }
    }

    public void levelFinished() {
        if (levelLoader.getLevelIndex() >= levelLoader.getNumLevels() - 1) {
            return;
        }
        player.charState = CharacterState.WINE;
        isSwitching = true;
        opacity = 10;
        transitioning = new Color(255, 255, 255);
        repaint();
    }


    // death cb
    public void death(Player p) {
        isSwitching = true;
        opacity = 10;
        transitioning = new Color(0, 0, 0);
        repaint();
    }

    //  checks the character when the character lands at its destination
    public void landChecker() {
         if (player.position.x < 0 || player.position.y < 0 || player.position.x >= player.level.xSize() || player.position.y >= player.level.ySize()) {
            player.die();
        } else {
            player.level.blockAt(player.position.x, player.position.y).onLand(player);
            player.target.x = start.x + player.position.x * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
            player.target.y = start.y + player.position.y * SPACING_BETWEEN_BLOCKS + BORDER_WIDTH;
            shouldRepaint = true;
        }
    }

    @Override
    public void paint(Graphics g) {
        timestart = Instant.now();
        clipholder = new Area();
        ouchArea = new Area();
        if (middle.x != jPanel1.getWidth() / 2) {
            middle.x = jPanel1.getWidth() / 2;
            start.x = middle.x - (player.level.xSize() - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
            end.x = middle.x * 2 - start.x;
            clipholder.add(new Area(new Rectangle(0, 0, jPanel1.getWidth(), jPanel1.getHeight())));
        }
        if (middle.y != jPanel1.getHeight() / 2) {
            middle.y = jPanel1.getHeight() / 2;
            start.y = middle.y - (player.level.ySize() - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
            end.x = middle.x * 2 - start.x;
            clipholder.add(new Area(new Rectangle(0, 0, jPanel1.getWidth(), jPanel1.getHeight())));
        }

        if (player.level.ySize() == 0) {
            System.out.println("Instantiate player.level.blocks!");
            loadLevel();
            player.charState = CharacterState.NORMAL;
        }
        
        if (player.render.x == 0 && player.render.y == 0) {
            System.out.println("Oi, why's it at zero?");
            loadLevel();
        }
        
        if (start.x == 0 || start.y == 0) {
            start.x = middle.x - (player.level.xSize() - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
            start.y = middle.y - (player.level.ySize() - 1) * SPACING_BETWEEN_BLOCKS / 2 - STANDARD_ICON_WIDTH / 2;
        }

        // Does the level switching / restarting stuff
        if (isSwitching) {
            if (opacity < 240) {
                opacity += 48;
            } else {
                isSwitching = false;
                switch (player.charState) {
                    case WINE:
                        if (holdLevelIndex != levelLoader.getLevelIndex()) {
                            levelLoader.setLevelIndex(holdLevelIndex);
                            player.level = levelLoader.getCurrent(); // Fuck. but why?
                        } else { 
                            player.level = levelLoader.getNext(); // Fuck.
                            holdLevelIndex = levelLoader.getLevelIndex();
                        }
                        if (levelLoader.getLevelIndex() > maxLevelIndex) {
                            maxLevelIndex = levelLoader.getLevelIndex();
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

        player.level.tickBlocks(player, start);
        player.level.tickEntities(player, new AABB(start, end), clipholder);


        // Calculates where the blasts would be
        int charYUpper = player.render.y;
        int charXLeft = player.render.x;
        int charYLower = player.render.y + CHARACTER_WIDTH;
        int charXRight = player.render.x + CHARACTER_WIDTH;
        for (Entity bla: player.level.getEntities()) {
            bla.moveTick();
            Area blaouch = bla.getCollision();
            Rectangle bound = blaouch.getBounds();
            if (bound.getX() > charXRight || bound.getX() + bound.width < charXLeft || bound.getY() > charYLower || bound.getY() + bound.height < charYUpper) {
            } else {
                ouchArea.add(blaouch);
            }
        }

        if (tasActive && player.level instanceof SJBossFight) {
            sjbossTas.doTasStuff(start, clock.time(), player);
        }

        if (ouchArea.intersects(new Rectangle(player.render.x, player.render.y, CHARACTER_WIDTH, CHARACTER_WIDTH))) {
            player.hurt();
        }
        
        if (clock.time() >= 1 || player.charState == CharacterState.MOVING || player.charState == CharacterState.FASTMOVING || tasActive || !isSwitching && player.level instanceof SJBossFight)
            clock.increment();

        if (player.level.winCond(player) && !isSwitching && !(opacity > 15) && player.charState == CharacterState.NORMAL) {
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
        

        // Setting clip
        if (player.level instanceof BossLevel || SEE_OVERLAP) {
            clipholder = new Area(new Rectangle(0, 0, jPanel1.getWidth(), jPanel1.getHeight()));
        } else {
            for (Entity bla: player.level.getEntities()) {
                clipholder.add(bla.getClip());
            }

            if (opacity > 15) {
                clipholder.add(new Area(new Rectangle(0, 0, jPanel1.getWidth(), jPanel1.getHeight())));
            }

            if (player.isInvincible()) {
                clipholder.add(new Area(new Rectangle(player.render.x, player.render.y, CHARACTER_WIDTH, CHARACTER_WIDTH)));
            }

            if (clock.time() >= 314 && clock.time() <= 335) { // Sets clip for the pie
                clipholder.add(new Area(new Rectangle(0, 0, PIEPNG.getIconWidth(), PIEPNG.getIconHeight())));
            }

        }
        
        if (player.charState == CharacterState.NORMAL)
            player.callMove();
        player.checkFlushQueueKey();
        if (player.moveAnim(clipholder))
            landChecker();

        ///PAINTINGG!!!
        
        // Draws the game panel
        jPanel1.setBackground(Color.white);
        Graphics currG = jPanel1.getGraphics();
        currG.setClip(clipholder);
        
        ((Graphics2D) currG).setBackground(player.level.getBackgroundColor(clock.time()));

        currG.clearRect(0, 0, jPanel1.getWidth(), jPanel1.getHeight());

        player.level.drawBackground(currG, clock.time(), jPanel1, start);

        for (int rowNumber = 0; rowNumber < player.level.ySize(); rowNumber++) 
            for (int columnNumber = 0; columnNumber < player.level.xSize(); columnNumber++) 
                if (player.level.blockAt(columnNumber, rowNumber) != null)
                    player.level.blockAt(columnNumber, rowNumber).getIcon().paintIcon(jPanel1, currG, start.x + columnNumber * SPACING_BETWEEN_BLOCKS, start.y + rowNumber * SPACING_BETWEEN_BLOCKS);

        characterIconAlive.paintIcon(jPanel1, currG, player.render.x, player.render.y);

        if (player.isInvincible()) {
            currG.setColor(new Color(0, 0, 255, clock.time() % 2 == 0 ? 50 : 100));
            currG.fillRect(player.render.x, player.render.y, CHARACTER_WIDTH, CHARACTER_WIDTH);
        }
        

        // Dev tool to see ouchArea and the clip
        if (SEE_OVERLAP) {
            /*currG.setColor(Color.red);
            ((Graphics2D) currG).draw(clipholder);*/
            currG.setColor(Color.black);
            for (Entity e : player.level.getEntities())
                ((Graphics2D) currG).draw(e.getCollision());
        }
        
        // Draws all the blasts and explosions and stuff
        for (Entity e: player.level.getEntities()) {
                e.draw(currG, jPanel1);
        }

        // Does the fading animations
        if (opacity > 15) {
            currG.setColor(new Color(transitioning.getRed(), transitioning.getGreen(), transitioning.getBlue(), opacity));
            currG.fillRect(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        }
        
        player.level.drawForeground(currG, clock.time(), jPanel1, start);
        

        // Draws the pie at frame 314
        if (clock.time() >= 314 && clock.time() <= 334) {
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
        g.drawString(player.level.label, this.getWidth() / 2 - 20, 70);
        g.drawString(String.format("TimeStamp: %d", clock.time()), this.getWidth() - 300, 80);
        g.drawString("Practice Mode: ".concat(player.isPracticeMode ? "On" : "Off"), this.getWidth() - 300, 100);
        g.drawString(String.format("Death Count (Total): %d", player.deaths), 300, 50);
        g.drawString(String.format("Death Count (Level): %d", player.level.getDeaths()), 300, 70);
        g.drawString(String.format("Health: %d", player.hp), 300, 90);
        g.drawString(String.format("Level Code: %s", player.level.code), 300, 110);
        
        // Draws Lea
        if (isLeaGif) {
            LEAGIF.paintIcon(this, g, 0, 30);
        }

        timeend = Instant.now();
        // Gets the block to show up on first run
        if (opacity > 15 || player.render.x != player.target.x || player.render.y != player.target.y || shouldRepaint || player.level.getEntities().iterator().hasNext() || letsseeifthisworks || player.level instanceof BossLevel) {
            if (letsseeifthisworks) {
                letsseeifthisworks = false;
            }
            long millitime = Duration.between(timestart, timeend).toMillis();
            try {
                if (millitime < sleepTime)
                    Thread.sleep(sleepTime - millitime);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainRunningThing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            shouldRepaint = false;
            repaint();
        }

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
 

}

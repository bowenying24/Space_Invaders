package org.cis1200;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.*;

public class FirstLevel extends JFrame {

    private static final int ALIENSPERROW = 8;

    private static final int ALIENROWS = 5;

    private static final int SHIELDNUMBER = 3;

    private static final int ALIENWIDTH = 50;

    private static final int ALIENHEIGHT = 40;

    private static int totAlien = ALIENSPERROW * ALIENROWS;

    private static int alienSpeed = 60;

    private AlienThread[][] alienArray = new AlienThread[ALIENROWS][ALIENSPERROW];

    private JPanel alienPanel;

    private JLabel livesLab, scoreLab, recordLab;

    private MenuController menuKeyListener;

    private Ship ship;

    private Alien alien;

    private ShipThread sT;

    private AlienThread aT;

    private Font f1;

    private Menu menu;

    private FirstLevelController firstLevelController;

    private FirstLevelPaintComponent mainPanel;

    private JLabel[] livesArray = new JLabel[menu.getLIVESNUMBER()];

    private int remainingLives = menu.getLIVESNUMBER();
    private int shipNumber;
    private int totScore = 0;

    private Toolkit toolKit = Toolkit.getDefaultToolkit();

    private ImageIcon alienType0 = new ImageIcon(toolKit.getImage("src/Sprite/alien1_0.png"));
    private ImageIcon alienType0One = new ImageIcon(toolKit.getImage("src/Sprite/alien1_1.png"));
    private ImageIcon alienType1 = new ImageIcon(toolKit.getImage("src/Sprite/alien2_0.png"));
    private ImageIcon alienType1One = new ImageIcon(toolKit.getImage("src/Sprite/alien2_1.png"));
    private ImageIcon alienType2 = new ImageIcon(toolKit.getImage("src/Sprite/alien3_0.png"));
    private ImageIcon alienType2One = new ImageIcon(toolKit.getImage("src/Sprite/alien3_1.png"));

    private ImageIcon shieldZero = new ImageIcon(toolKit.getImage("src/Sprite/shield_0_0.png"));

    private ImageIcon shieldZeroDestroyed = new ImageIcon(
            toolKit.getImage("src/Sprite/shield_0_0_destroyed.png"));

    private ImageIcon shipIcon2;

    private FileWriter fw;
    private PrintWriter pw;

    private EntireShield[] shieldArray = new EntireShield[SHIELDNUMBER];

    private boolean firstTimeGameOver = true;
    private boolean firstTimeNewLevel = true;
    private boolean doNextLevel = true;
    private boolean mute = false;

    /**
     * Firts parameterized constructor
     * @param title, title
     * @param menu, menu reference
     * @param mute, audio on or off
     */
    public FirstLevel(String title, Menu menu, boolean mute) {

        super(title);
        this.menu = menu;
        this.mute = mute;

        //setting the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(550, 50, 650, 800);
        setVisible(true);

        //setting fonts
        f1 = new Font("Courier", Font.BOLD, 25); // start

        //initializing panels
        mainPanel = new FirstLevelPaintComponent(getWidth(), getHeight());
        alienPanel = new JPanel();

        //initializing labels
        livesLab = new JLabel("LIVES:");
        scoreLab = new JLabel("SCORE: " + totScore);
        recordLab = new JLabel("RECORD: " + menu.getRecord());

        //setting labels
        livesLab.setFont(f1);
        livesLab.setForeground(Color.WHITE);
        livesLab.setBounds(20, getHeight() - 120, 150, 100);

        scoreLab.setFont(f1);
        scoreLab.setForeground(Color.WHITE);
        scoreLab.setBounds(20, 0, 250, 50);

        recordLab.setFont(f1);
        recordLab.setForeground(Color.WHITE);
        recordLab.setBounds(350, 0, 250, 50);

        //setting panels
        mainPanel.setLayout(null);
        mainPanel.setOpaque(true);
        mainPanel.setBackground(Color.BLACK);

        alienPanel.setBounds(0, 58, getWidth(), 225);
        alienPanel.setLayout(null);
        alienPanel.setOpaque(false);

        //initializing ship
        ship = new Ship(40, 50, getWidth() / 2 - 40,
                getHeight() - 150, getWidth(), menu, mainPanel, mute);
        ship.movement(0, false);

        shieldCreation(); //creating shields

        //adding components
        mainPanel.add(ship);
        mainPanel.add(livesLab);
        mainPanel.add(scoreLab);
        mainPanel.add(recordLab);

        shipIcon2 = ship.getImageIcon();
        livesDrawing(shipIcon2); //drawing lives

        //creating ship thread and other stuff
        sT = new ShipThread(ship, this);
        sT.start();

        alienThreadCreation();
        alienThreadStart();

        alienAdd(alienPanel);
        mainPanel.add(alienPanel);
        this.add(mainPanel);
        firstLevelController = new FirstLevelController(ship, this);
        this.addKeyListener(firstLevelController);
    }

    /**
     * Second parameterized constuctor for new levels
     * @param title, title
     * @param menu, menu reference
     * @param remainingLives, player's remaining lives
     * @param totScore, player's score
     * @param livesArray, array of lives
     * @param shipNumber, the chosen ship
     * @param alienSpeed, alien speed increasead in each new level
     * @param mute, audio on or off
     */
    public FirstLevel(String title, Menu menu, int remainingLives, int totScore,
                      JLabel[] livesArray, int shipNumber, int alienSpeed, boolean mute) {

        super(title);
        this.menu = menu;
        this.remainingLives = remainingLives;
        this.livesArray = livesArray;
        this.totScore = totScore;
        this.shipNumber = shipNumber;
        this.alienSpeed = alienSpeed;
        this.mute = mute;

        //setting the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(550, 50, 650, 800);
        setVisible(true);

        //setting fonts
        f1 = new Font("Courier", Font.BOLD, 25); // start

        //initializing panels
        mainPanel = new FirstLevelPaintComponent(getWidth(), getHeight());
        alienPanel = new JPanel();

        //initializing labels
        livesLab = new JLabel("LIVES:");
        scoreLab = new JLabel("SCORE: " + totScore);
        recordLab = new JLabel("RECORD: " + menu.getRecord());

        //setting labels
        livesLab.setFont(f1);
        livesLab.setForeground(Color.WHITE);
        livesLab.setBounds(20, getHeight() - 120, 150, 100);

        scoreLab.setFont(f1);
        scoreLab.setForeground(Color.WHITE);
        scoreLab.setBounds(20, 0, 250, 50);

        recordLab.setFont(f1);
        recordLab.setForeground(Color.WHITE);
        recordLab.setBounds(350, 0, 250, 50);

        //setting panels
        mainPanel.setLayout(null);
        mainPanel.setOpaque(true);
        mainPanel.setBackground(Color.BLACK);

        alienPanel.setBounds(0, 58, getWidth(), 225);
        alienPanel.setLayout(null);
        alienPanel.setOpaque(false);

        //initializing ship
        ship = new Ship(40, 50, getWidth() / 2 - 40,
                getHeight() - 150, getWidth(), menu, mainPanel, mute);
        ship.movement(0, false);

        shieldCreation(); //creating shields

        //adding components and other stuff
        mainPanel.add(ship);
        mainPanel.add(livesLab);
        mainPanel.add(scoreLab);
        mainPanel.add(recordLab);

        shipIcon2 = ship.getImageIcon();
        livesDrawing(shipIcon2);

        sT = new ShipThread(ship, this);
        sT.start();
        alienThreadCreation();
        alienThreadStart();

        alienAdd(alienPanel);

        mainPanel.add(alienPanel);
        this.add(mainPanel);
        firstLevelController = new FirstLevelController(ship, this);
        this.addKeyListener(firstLevelController);
    }

    /**
     * Method to draw lives in the level
     * @param ship, ship image
     */
    public void livesDrawing(ImageIcon ship) {

        int x = getWidth() - 520;
        int y = getHeight() - 90;
        int xVar = 0;

        for (int i = 0; i < remainingLives; i++) {

            JLabel livesPanel = new JLabel();
            livesArray[i] = livesPanel;
            livesPanel.setIcon(ship);
            Image g = ship.getImage().getScaledInstance(50, 40, 50);
            ship.setImage(g);
            livesPanel.setBounds((x + xVar), y, 50, 30);
            xVar += 60;
            mainPanel.add(livesPanel);
            repaint();
        }
    }

    /**
     * Method to clean lives before the next drawing
     */
    public void livesCleaning() {
        for (int i = 0; i < remainingLives; i++) {

            livesArray[i].setVisible(false);
            livesArray[i] = null;
        }
    }

    /**
     * To create shield in the level
     */
    public void shieldCreation() {

        int x = 75;
        int y = getHeight() - 220;

        int height = 60;
        int width = 40;

        for (int i = 0; i < SHIELDNUMBER; i++) {
            EntireShield eS = new EntireShield(height, width, x, y, this, mainPanel);
            x += 200;
            shieldArray[i] = eS;
        }
    }

    /**
     * To create alien threads
     */
    public void alienThreadCreation() {
        int x = 75;
        int y = 2;
        for (int i = 0; i < ALIENROWS; i++) {
            for (int j = 0; j < ALIENSPERROW; j++) {
                //Note: there is a different alien in different rows

                //row 1
                if (i == 0) {
                    Rectangle rectangle = new Rectangle(x, y + alienPanel.getY(),
                            ALIENWIDTH, ALIENHEIGHT);
                    alien = new Alien(ALIENHEIGHT, ALIENWIDTH, x, y, getWidth(),
                            getHeight() - 150,
                            alienPanel, alienType0, rectangle, 30, mute);
                    alienArray[i][j] = new AlienThread(alien, alienType0,
                            alienType0One, totAlien, this);
                }

                //rows 2&3
                if (i == 1 || i == 2) {
                    Rectangle rectangle = new Rectangle(x, y + alienPanel.getY(),
                            ALIENWIDTH, ALIENHEIGHT);
                    alien = new Alien(ALIENHEIGHT, ALIENWIDTH, x, y,
                            getWidth(), getHeight() - 150,
                            alienPanel, alienType1, rectangle, 20, mute);
                    alienArray[i][j] = new AlienThread(alien, alienType1,
                            alienType1One, totAlien, this);
                }

                //rows 4&5
                if (i == 3 || i == 4) {
                    Rectangle rectangle = new Rectangle(x, y + alienPanel.getY(),
                            ALIENWIDTH, ALIENHEIGHT);
                    alien = new Alien(ALIENHEIGHT, ALIENWIDTH, x, y,
                            getWidth(), getHeight() - 150,
                            alienPanel, alienType2, rectangle, 10, mute);
                    alienArray[i][j] = new AlienThread(alien, alienType2,
                            alienType2One, totAlien, this);
                }

                x += 62;

                if ((x + 90) >= getWidth()) {
                    x = 75;
                }

            }
            y += 45;

        }


        //creating alien array
        for (int i = 0; i < ALIENROWS; i++) {
            for (int j = 0; j < ALIENSPERROW; j++) {
                alienArray[i][j].setAlienArray(alienArray);
            }
        }
    }

    /**
     * Method to start aliens
     */
    public void alienThreadStart() {
        for (int i = 0; i < ALIENROWS; i++) {
            for (int j = 0; j < ALIENSPERROW; j++) {
                alienArray[i][j].start();
            }
        }
    }

    /**
     * Method to add aliens to the display panel
     * @param panel, alien panel
     */
    public void alienAdd(JPanel panel) {
        for (int i = 0; i < ALIENROWS; i++) {
            for (int j = 0; j < ALIENSPERROW; j++) {
                panel.add(alienArray[i][j].getAlien());
            }
        }
    }

    /**
     * Method to respawn the ship
     */
    public void shipRelive() {
        ship.movement(0, false);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("Sleep error");
        }

        ship.setBounds(40, 50, getWidth() / 2 - 40, getHeight() - 150);
        ship.setIcon(shipIcon2);
        ship.setVisible(true);
    }

    /**
     * Mwthod to check if there are living alien
     * @return boolean, true o false
     */
    public boolean livingAlien() {
        boolean alive = false;
        for (int i = 0; i < ALIENROWS; i++) {
            for (int j = 0; j < ALIENSPERROW; j++) {
                if (alienArray[i][j].getAlien().isVisible()) {
                    alive = true;
                }
            }
        }
        return alive;
    }


    /**
     * Method to write the record on a file
     */
    public void recordWriter() {

        recordLab.setText("RECORD: " + totScore);

        // opening file
        try {
            fw = new FileWriter("Record.txt");
            pw = new PrintWriter(fw);
        } catch (Exception ex) {
            System.out.println("Opening file error");
        }

        String s = null;
        try {
            s = String.valueOf(totScore);
            pw.print(s);
            fw.close(); // closing file
        } catch (IOException ex) {
            System.out.println("Writing file error");
        }
    }

    /**
     * Method to create game over display on screen. Ensures that this will happen only once.
     */
    public void createGameOver() {
        int a = 1;

        if (firstTimeGameOver) {
            firstTimeGameOver = false;
            doNextLevel = false;
            for (int i = 0; i < ALIENROWS; i++) {
                for (int j = 0; j < ALIENSPERROW; j++) {

                    if (!alienArray[i][j].getAlive()) {
                        a++;
                        if (a == totAlien) {
                            for (int k = 0; k < ALIENROWS; k++) {
                                for (int l = 0; l < ALIENSPERROW; l++) {
                                    if (alienArray[k][l].getGameOverCount() == 1) {
                                        GameOver gO = new GameOver("org.cis1200.Game Over", mute);
                                        gO.setVisible(true);
                                        break;
                                    }
                                }


                            }
                        }
                    }


                }
            }
        }
    }

    /**
     * Method to set attributes and other stuff to create the next level
     */
    public void newLevel() {
        boolean alive = false;

        for (int i = 0; i < ALIENROWS; i++) {
            for (int j = 0; j < ALIENSPERROW; j++) {
                if (alienArray[i][j].isAlive()) {
                    alive = true;
                }
            }
        }

        //note: increments speed for next level
        if (!alive && firstTimeNewLevel) {
            firstTimeNewLevel = false;
            FirstLevel newFL = new FirstLevel("Space Invaders - org.cis1200.Game",
                    menu, remainingLives,
                    totScore, livesArray,
                    shipNumber, alienSpeed + 20, mute);
            dispose();
            newFL.setVisible(true);

        }
    }

    //Getters and Setters
    public FirstLevelPaintComponent getMainPanel() {
        return mainPanel;
    }
    public void setMainPanel(FirstLevelPaintComponent mainPanel) {
        this.mainPanel = mainPanel;
    }
    public AlienThread[][] getAlienArray() {
        return alienArray;
    }
    public Ship getShip() {
        return ship;
    }
    public JLabel getRecord() {
        return recordLab;
    }
    public Menu getMenu() {
        return menu;
    }
    public int getRemainingLives() {
        return remainingLives;
    }
    public void setRemainingLives(int remainingLives) {
        this.remainingLives = remainingLives;
    }
    public ImageIcon getShipIcon2() {
        return shipIcon2;
    }
    public void setShipNumber(int shipNumber) {
        this.shipNumber = shipNumber;
    }
    public int getShipNumber() {
        return shipNumber;
    }
    public ShipThread getsT() {
        return sT;
    }
    public FirstLevelController getFirstLevelController() {
        return firstLevelController;
    }
    public int getTotScore() {
        return totScore;
    }
    public void setTotScore(int totScore) {
        this.totScore = totScore;
        scoreLab.setText("SCORE: " + totScore);
    }
    public void setLivesArray(JLabel[] livesArray) {
        this.livesArray = livesArray;
    }
    public JLabel[] getLivesArray() {
        return livesArray;
    }
    public boolean isFirstTimeGameOver() {
        return firstTimeGameOver;
    }
    public boolean isDoNextLevel() {
        return doNextLevel;
    }
    public void setDoNextLevel(boolean doNextLevel) {
        this.doNextLevel = doNextLevel;
    }
    public static int getAlienSpeed() {
        return alienSpeed;
    }
    public static void setAlienSpeed(int alienSpeed) {
        FirstLevel.alienSpeed = alienSpeed;
    }
    public EntireShield[] getShieldArray() {
        return shieldArray;
    }
    public boolean isMute() {
        return mute;
    }
}
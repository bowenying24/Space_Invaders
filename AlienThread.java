package org.cis1200;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class AlienThread extends Thread {

    private Alien alien;

    private int alienAnimation = 0;
    private int randomShot = 850;
    private static int totAlien;
    private int gameOverCount = 2;
    private int speed;

    private Toolkit toolKit = Toolkit.getDefaultToolkit();

    private ImageIcon alienType0;
    private ImageIcon alienType1;
    private ImageIcon alienBulletImage0 = new
            ImageIcon(toolKit.getImage("src/Sprite/alien_shot0.png"));

    private Image image0, image1;

    private AlienThread[][] alienArray;

    private JPanel panel;

    private FirstLevel fL;

    private Random random = new Random();

    private boolean alive = true;
    private boolean doOnce = true; // do only once the downLimit control

    private Rectangle r;

    /**
     * Parameterized constructor
     * @param alien, alien reference
     * @param alienType0, first alien image for animation
     * @param alienType1, second alien image for animation
     * @param totAlien, total alien number
     * @param fL, first level reference
     */
    public AlienThread(Alien alien,
                       ImageIcon alienType0,
                       ImageIcon alienType1, int totAlien, FirstLevel fL) {
        this.alien = alien;
        this.alienType0 = alienType0;
        this.alienType1 = alienType1;
        this.totAlien = totAlien;
        this.fL = fL;
        this.panel = alien.getPanel();

        speed = fL.getAlienSpeed();

        setPriority(MAX_PRIORITY);

        image0 = alienType0.getImage().getScaledInstance(50, 40, 50);
        image1 = alienType1.getImage().getScaledInstance(50, 40, 50);

        this.alienType0.setImage(image0);
        this.alienType1.setImage(image1);
    }

    /**
     * Thread running
     */
    @Override
    public void run() {
        long frameRate = 1000 / speed;
        long previousTimeAnimation = System.currentTimeMillis();
        long previousTimeShoots = System.currentTimeMillis();


        while (alive) {

            //alien thread refresh synchronization and speed
            long previousTime = System.currentTimeMillis();

            while (System.currentTimeMillis() - previousTime < frameRate) {
                try {
                    Thread.sleep(1, 15);
                } catch (InterruptedException ex) {
                    System.out.println("Sleep error");
                }
            }

            //animation, to change sprite
            if (System.currentTimeMillis() - previousTimeAnimation >=
                    random.nextInt(200) + 400 && alienAnimation == 0) {

                alien.setIcon(alienType1);
                previousTimeAnimation = System.currentTimeMillis();
                alienAnimation = 1;
            }

            //animation, to change sprite
            if (System.currentTimeMillis() - previousTimeAnimation >=
                    random.nextInt(200) + 400 && alienAnimation == 1) {

                alien.setIcon(alienType0);
                previousTimeAnimation = System.currentTimeMillis();
                alienAnimation = 0;
            }

            //randomized alien shot
            if (alive) {
                if (random.nextInt(randomShot) == 2) {

                    r = new Rectangle((alien.getX() +
                            alien.getWidth() / 2 + alien.getPanel().getX() - 2),
                            (alien.getY() + alien.getHeight() + alien.getPanel().getY()), 10, 20);
                    Bullet alienBullet = new Bullet(20, 10,
                            ((alien.getX() + alien.getWidth() / 2) +
                            alien.getPanel().getX() - 2), ((alien.getY() + alien.getHeight()) +
                            alien.getPanel().getY()), fL.getMainPanel(), alienBulletImage0, r);
                    fL.getMainPanel().add(alienBullet);

                    AlienBulletThread aBT = new AlienBulletThread(alienBullet, fL);
                    aBT.start();
                    previousTimeShoots = System.currentTimeMillis();
                }
            }

            alien.setX(alien.getX() + 1 * alien.getDirection());

            if (alien.isVisible()) {
                //when it reaches the right limit, the entire panel goes down
                if (alien.getX() +
                        alien.getWidth() >= alien.getRightLimit() - 15) {

                    alienArray[0][0].getAlien().borderRSound();
                    panel.setBounds(panel.getX(), panel.getY() + 15,
                            panel.getWidth(), panel.getHeight());
                    fL.alienAdd(panel);

                    for (int i = 0; i < alienArray.length; i++) {
                        for (int j = 0; j < alienArray[0].length; j++) {
                            alienArray[i][j].getAlien().setDirection(-1);
                        }
                    }
                }
            }

            if (alien.isVisible()) {
                //when it reaches the left limit, the entire panel goes down
                if ((alien.getX() - 2) <= 0) {

                    alienArray[0][0].getAlien().borderLSound();
                    panel.setBounds(panel.getX(), panel.getY() + 15,
                            panel.getWidth(), panel.getHeight());
                    fL.alienAdd(panel);

                    for (int i = alienArray.length - 1; i >= 0; i--) {
                        for (int j = alienArray[i].length - 1; j >= 0; j--) {
                            alienArray[i][j].getAlien().setDirection(1);
                        }
                    }
                }
            }

            if (doOnce) { // create the game over display only one time
                for (int i = alienArray.length - 1; i >= 0; i--) {
                    for (int j = alienArray[i].length - 1; j >= 0; j--) {
                        if (alienArray[i][j].getAlien().isVisible()) {

                            // alien coordinates: if it reaches the down limit
                            if (alienArray[i][j].getAlien().getY() + panel.getY() +
                                    alienArray[i][j].getAlien().getHeight()
                                    >= alienArray[i][j].getAlien().getDownLimit()) {

                                alienArray[i][j].setGameOverCount(1);
                                alienArray[i][j].setAlive(false);

                                fL.getsT().setAlive(false);
                                fL.dispose();

                                for (int k = 0; k < fL.getAlienArray().length; k++) {
                                    for (int l = 0; l < fL.getAlienArray()[i].length; l++) {
                                        alienArray[k][l].setAlive(false);
                                    }
                                }

                                fL.createGameOver();
                                doOnce = false;
                                break;
                            }
                        }
                    }

                }
            }

            alien.setBounds(alien.getX(), alien.getY(), alien.getWidth(), alien.getHeight());
            alien.getRectangle().setBounds(alien.getX(), alien.getY() + panel.getY(),
                    alien.getWidth(), alien.getHeight());
        }
    }

    //Getter and Setter
    public Alien getAlien() {
        return alien;
    }
    public void setAlienArray(AlienThread[][] alienArray) {
        this.alienArray = alienArray;
    }
    public boolean getAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public void setAlienType0(ImageIcon alienType0) {
        this.alienType0 = alienType0;
    }
    public void setAlienType1(ImageIcon alienType1) {
        this.alienType1 = alienType1;
    }
    public void setGameOverCount(int gameOverCount) {
        this.gameOverCount = gameOverCount;
    }
    public int getGameOverCount() {
        return gameOverCount;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
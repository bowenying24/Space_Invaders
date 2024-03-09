package org.cis1200;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

public class ShipBulletThread extends Thread {

    private Bullet shipBullet;

    private Toolkit toolKit = Toolkit.getDefaultToolkit();

    private ImageIcon shipBulletImage;

    private ImageIcon alienDestroyedImage = new ImageIcon(
            toolKit.getImage("src/Sprite/alien_destroyed.png"));
    private ImageIcon mysteriousAlienImage = new ImageIcon(
            toolKit.getImage("src/Sprite/saucer.png"));
    private ImageIcon mysteriousAlienImageDestroyed = new ImageIcon(
            toolKit.getImage("src/Sprite/saucer_destroyed_0.png"));
    private ImageIcon mysteriousAlienImageScore = new ImageIcon(
            toolKit.getImage("src/Sprite/saucer_destroyed_1.png"));

    private Image image0, image1;

    private FirstLevel fL;

    private MysteriousAlien mA;

    private AlienThread[][] alienArray;

    private static MysteriousAlienThread mAT;

    private boolean alive = true;

    /**
     * Parameterized constructor
     * @param shipBullet, bullet reference
     * @param shipBulletImage, image
     * @param fL, first level reference
     */
    public ShipBulletThread(Bullet shipBullet, ImageIcon shipBulletImage, FirstLevel fL) {

        this.shipBullet = shipBullet;
        this.shipBulletImage = shipBulletImage;
        this.fL = fL;
        alienArray = fL.getAlienArray();
        setPriority(MAX_PRIORITY);

        //initializing mysterious alien
        mA = new MysteriousAlien(30, 60, 0, 50,
                fL.getMainPanel(), mysteriousAlienImage, fL.isMute());

        image0 = shipBulletImage.getImage().getScaledInstance(7, 13, 7);
        image1 = alienDestroyedImage.getImage().getScaledInstance(50, 40, 50);

        this.shipBulletImage.setImage(image0);
        this.alienDestroyedImage.setImage(image1);
    }

    /**
     * Thread running
     */
    @Override
    public void run() {

        long frameRate = 1000 / 60;
        long previousTimeAnimation = System.currentTimeMillis();

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

            shipBullet.setY(shipBullet.getY() - 12);
            shipBullet.getRectangle().setBounds(shipBullet.getX(),
                    shipBullet.getY(), shipBullet.getWidth(), shipBullet.getHeight());

            //when it reaches the top limit, it dies
            if (shipBullet.getY() <= fL.getRecord().getHeight() - 5) {
                shipBullet.setVisible(false);
                alive = false;
            }

            //checking shields
            for (int i = 0; i < fL.getShieldArray().length; i++) {
                fL.getShieldArray()[i].shieldCheck(shipBullet.getRectangle(), this);
            }

            //checking collision between ship bullet and aliens
            for (int i = alienArray.length - 1; i >= 0; i--) {
                for (int j = alienArray[i].length - 1; j >= 0; j--) {
                    if ((this.shipBullet != null) && alienArray[i][j].getAlien().getRectangle().
                            intersects(shipBullet.getRectangle()) && alienArray[i][j].getAlive()) {

                        int remainingAlienCount = 0;

                        //counting remaining aliens
                        for (int k = 0; k < fL.getAlienArray().length; k++) {
                            for (int l = 0; l < fL.getAlienArray()[i].length; l++) {
                                if (fL.getAlienArray()[k][l].getAlien().isVisible()) {
                                    remainingAlienCount++;
                                }
                            }
                        }

                        //if there are 10 remaining aliens, the mysterious alien appears
                        if (remainingAlienCount == 10) {
                            mAT = new MysteriousAlienThread(mA, fL);
                            mAT.start();
                        }

                        //destroy the alien hit
                        alienArray[i][j].getAlien().sound();
                        alienArray[i][j].getAlien().setIcon(alienDestroyedImage);
                        alienArray[i][j].setAlienType0(alienDestroyedImage);
                        alienArray[i][j].setAlienType1(alienDestroyedImage);
                        try {
                            alienArray[i][j].sleep(100);
                        } catch (InterruptedException ex) {
                            System.out.println("Sleep error");
                        }

                        //updating the score
                        fL.setTotScore(fL.getTotScore() + alienArray[i][j].getAlien().getScore());

                        alienArray[i][j].getAlien().setVisible(false);
                        alienArray[i][j].setAlive(false);

                    /*
                        If the player has beat the level, check if it has beaten the record.
                        In that case, save it.
                    */
                        if (!fL.livingAlien()) {
                            if (fL.getTotScore() > fL.getMenu().getRecord()) {
                                fL.recordWriter();
                            }
                        }

                        shipBullet.setVisible(false);
                        shipBullet = null;
                        alive = false;
                        break;
                    }
                }
            }

            //if the mysterious alien is alive, check the collision
            if (mAT != null && alive && mAT.getAlive()) {

                if (mAT.getmA().getRectangle().intersects(
                        shipBullet.getRectangle()) && mAT.getAlive()) {

                    mAT.getmA().setRectangle(new Rectangle(1, 2, 1, 1));

                    mAT.getmA().sound();

                    mAT.getmA().changeImage(mysteriousAlienImageDestroyed);

                    mAT.setnPixelMovement(0);

                    try {
                        mAT.sleep(180);
                    } catch (InterruptedException ex) {
                        System.out.println("Sleep error");
                    }

                    mAT.getmA().changeImage(mysteriousAlienImageScore);

                    try {
                        mAT.sleep(100);
                    } catch (InterruptedException ex) {
                        System.out.println("Sleep error");
                    }

                    fL.setTotScore(fL.getTotScore() + mAT.getmA().getScore());

                    mAT.getmA().setMute(true);
                    mAT.getmA().movingSound();
                    mAT.getmA().setVisible(false);
                    mAT.setAlive(false);

                    shipBullet.setVisible(false);
                    shipBullet = null;
                    alive = false;
                }
            }

            if (alive) {
                shipBullet.setBounds(shipBullet.getX(), shipBullet.getY(),
                        shipBullet.getWidth(), shipBullet.getHeight());
            }
        }
    }
    //Getter and Setter
    public Bullet getShipBullet() {
        return shipBullet;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
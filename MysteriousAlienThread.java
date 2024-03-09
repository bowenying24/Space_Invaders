package org.cis1200;

import java.awt.Rectangle;
import javax.swing.JPanel;

public class MysteriousAlienThread extends Thread {

    private MysteriousAlien mA;

    private int speed;

    private JPanel panel;

    private FirstLevel fL;

    private int nPixelMovement = 2;

    private boolean alive = true;

    private Rectangle r;

    /**
     * Parameterized constructor
     * @param mA, mysterious alien reference
     * @param fL, first level reference
     */
    public MysteriousAlienThread(MysteriousAlien mA, FirstLevel fL) {
        this.mA = mA;
        this.fL = fL;
        this.panel = mA.getPanel();

        speed = fL.getAlienSpeed();

        mA.setVisible(true);
        panel.add(mA);

        if (!mA.getMute()) {
            mA.movingSound();
        }

        setPriority(MAX_PRIORITY);
    }

    /**
     * Thread running
     */
    @Override
    public void run() {
        long frameRate = 1000 / speed;

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

            mA.setX(mA.getX() + nPixelMovement);
            mA.setBounds(mA.getX(), mA.getY(), mA.getWidth(), mA.getHeight());
            mA.getRectangle().setBounds(mA.getX(), mA.getY(), mA.getWidth(), mA.getHeight());

            //if the mysterious alien has passed the panel, it dies
            mA.updateLocation(alive);
        }
    }

    //Getter and Setter
    public MysteriousAlien getmA() {
        return mA;
    }
    public boolean getAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public void setnPixelMovement(int nPixelMovement) {
        this.nPixelMovement = nPixelMovement;
    }
}
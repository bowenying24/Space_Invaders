package org.cis1200;

import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.util.ArrayList;

public class EntireShield {

    private Toolkit toolKit = Toolkit.getDefaultToolkit();

    private ImageIcon shieldZero = new ImageIcon(toolKit.getImage("src/Sprite/shield_0_3.png"));
    private ImageIcon shieldOne = new ImageIcon(toolKit.getImage("src/Sprite/shield_1_0.png"));
    private ImageIcon shieldTwo = new ImageIcon(toolKit.getImage("src/Sprite/shield_2_3.png"));
    private ImageIcon shieldThree = new ImageIcon(toolKit.getImage("src/Sprite/shield_0_0.png"));
    private ImageIcon shieldFour = new ImageIcon(toolKit.getImage("src/Sprite/shield_2_0.png"));
    private ImageIcon shieldZeroDestroyed = new ImageIcon(toolKit.getImage(
            "src/Sprite/shield_0_3_destroyed.png"));
    private ImageIcon shieldOneDestroyed = new ImageIcon(toolKit.getImage(
            "src/Sprite/shield_1_0_destroyed.png"));
    private ImageIcon shieldTwoDestroyed = new ImageIcon(toolKit.getImage(
            "src/Sprite/shield_2_3_destroyed.png"));
    private ImageIcon shieldThreeDestroyed = new ImageIcon(toolKit.getImage(
            "src/Sprite/shield_0_0_destroyed.png"));
    private ImageIcon shieldFourDestroyed = new ImageIcon(toolKit.getImage(
            "src/Sprite/shield_2_0_destroyed.png"));

    private ArrayList<Shield> entireShield = new ArrayList<>();
    private ArrayList<Shield>  destroyedShield = new ArrayList<>();

    /**
     * parameterized constructor
     * @param height, shield height
     * @param width, shield width
     * @param x, shield x position
     * @param y, shield y position
     * @param fL, level reference
     * @param mainPanel, shield panel
     */
    public EntireShield(int height, int width, int x, int y, FirstLevel fL,
                        FirstLevelPaintComponent mainPanel) {

        /*
            Constructing the entireShield ArrayList with different shield pieces and the array
            with different destroyed shield pieces
        */
        entireShield.add(new Shield(height, width, x, y, mainPanel, shieldZero));
        entireShield.add(new Shield(height, width, x, y - 20, mainPanel, shieldOne));
        entireShield.add(new Shield(height, width, x + 30, y - 20, mainPanel, shieldOne));
        entireShield.add(new Shield(height, width, x + 60, y - 20, mainPanel, shieldOne));
        entireShield.add(new Shield(height, width, x + 30, y - 40, mainPanel, shieldOne));
        entireShield.add(new Shield(height, width, x + 60, y, mainPanel, shieldTwo));
        entireShield.add(new Shield(height, width, x, y - 40, mainPanel, shieldThree));
        entireShield.add(new Shield(height, width, x + 60, y - 40, mainPanel, shieldFour));

        destroyedShield.add(new Shield(height, width, x, y,
                mainPanel, shieldZeroDestroyed, null));
        destroyedShield.add(new Shield(height, width, x, y - 20,
                mainPanel, shieldOneDestroyed, null));
        destroyedShield.add(new Shield(height, width, x + 30, y - 20,
                mainPanel, shieldOneDestroyed, null));
        destroyedShield.add(new Shield(height, width, x + 60, y - 20,
                mainPanel, shieldOneDestroyed, null));
        destroyedShield.add(new Shield(height, width, x + 30, y - 40,
                mainPanel, shieldOneDestroyed, null));
        destroyedShield.add(new Shield(height, width, x + 60, y,
                mainPanel, shieldTwoDestroyed, null));
        destroyedShield.add(new Shield(height, width, x, y - 40,
                mainPanel, shieldThreeDestroyed, null));
        destroyedShield.add(new Shield(height, width, x + 60, y - 40,
                mainPanel, shieldFourDestroyed, null));
    }

    /**
     * Method to check shield integrity by an alien bullet
     * @param alienBulletRect, alien bullet hit-box
     * @param aBT, alien bullet thread that does the check
     */
    public void shieldCheck(Rectangle alienBulletRect, AlienBulletThread aBT) {
        /*
            If the bullet hit a part of the shield, it is changed
            with his destroyed piece or it is completely destroyed.
            The second case happens when one block is hit the second time.
        */
        for (int i = 0; i < entireShield.size(); i++) {
            if (entireShield.get(i).getHitBox().intersects(alienBulletRect)) {
                aBT.getAlienBullet().setVisible(false);
                aBT.setAlive(false);
                if (entireShield.get(i).isNotHitYet()) {
                    destroyedShield.get(i).setHitBox(entireShield.get(i).getHitBox());
                    entireShield.get(i).setVisible(false);
                    entireShield.set(i, destroyedShield.get(i));
                    entireShield.get(i).setVisible(true);
                } else {
                    entireShield.get(i).setVisible(false);
                    entireShield.get(i).setHitBox(new Rectangle(10, 0, 10, 10));
                }
                entireShield.get(i).setNotHitYet(false);
            }
        }
    }

    /**
     * Method to check shield integrity by a ship bullet
     * @param shipBulletRect, ship bullet hitbox
     * @param sBT, ship bullet thread that does the check
     */
    public void shieldCheck(Rectangle shipBulletRect, ShipBulletThread sBT) {
        /*
            If the bullet hit a part of the shield, it is changed
            with his destroyed piece or it is completely destroyed.
            The second case happens when one block is hit the second time.
        */
        for (int i = 0; i < entireShield.size(); i++) {
            if (entireShield.get(i).getHitBox().intersects(shipBulletRect)) {
                sBT.getShipBullet().setVisible(false);
                sBT.setAlive(false);
                if (entireShield.get(i).isNotHitYet()) {
                    destroyedShield.get(i).setHitBox(entireShield.get(i).getHitBox());
                    entireShield.get(i).setVisible(false);
                    entireShield.set(i, destroyedShield.get(i));
                    entireShield.get(i).setVisible(true);
                } else {
                    entireShield.get(i).setVisible(false);
                    entireShield.get(i).setHitBox(new Rectangle(10, 0, 10, 10));
                }
                entireShield.get(i).setNotHitYet(false);
            }
        }
    }
}
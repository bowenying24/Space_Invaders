package org.cis1200;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InfoMenuController implements KeyListener {
    
    private InfoMenu iM;
    
    private int keyCode;
    
    /**
     * Parameterized constructor
     * @param iM, menu view reference 
     */
    public InfoMenuController(InfoMenu iM) {
        this.iM = iM;
    }

    /**
     * Empty
     * @param e, event origin 
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Empty
     * @param e, event origin 
     */
    @Override
    public void keyPressed(KeyEvent e) {}

    /**
     * 
     * @param e, event origin 
     */
    @Override
    public void keyReleased(KeyEvent e) {
        keyCode = e.getKeyCode(); //we get the event origin
        
        if (keyCode == KeyEvent.VK_B) {
            iM.dispose(); //we close the info view
        }
    }
}
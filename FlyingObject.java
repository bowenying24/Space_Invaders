package org.cis1200;

import javax.swing.*;

public interface FlyingObject {

    public void movement(int d, boolean m);

    public void sound();

    public void changeImage(ImageIcon newImage);

    public void updateLocation(boolean alive);

}

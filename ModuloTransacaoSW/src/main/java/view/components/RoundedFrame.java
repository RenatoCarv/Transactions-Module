package view.components;

import javax.swing.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedFrame extends JFrame {

    public RoundedFrame(String title, int width, int height, int arcWidth, int arcHeight) {
        super(title);
        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, width, height, arcWidth, arcHeight));
    }
}

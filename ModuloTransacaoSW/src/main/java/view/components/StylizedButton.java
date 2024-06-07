package view.components;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class StylizedButton extends JButton {

    private Shape shape;
    private final Color hoverColor;
    private float hoverProgress = 0f;
    private Timer hoverTimer;
    private final int cornerRadius = 40;

    public StylizedButton(String text) {
        this(text, new Color(11, 59, 85), new Color(11, 59, 85, 150));
    }

    public StylizedButton(String text, Color backgroundColor, Color hoverColor) {
        super(text);
        setBackground(backgroundColor);
        this.hoverColor = hoverColor;
        setContentAreaFilled(false);
        setFocusPainted(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startHoverAnimation(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startHoverAnimation(false);
            }
        });
    }

    private void startHoverAnimation(boolean entering) {
        if (hoverTimer != null && hoverTimer.isRunning()) {
            hoverTimer.stop();
        }

        hoverTimer = new Timer(10, _ -> {
            if (entering) {
                hoverProgress += 0.05f;
                if (hoverProgress >= 1f) {
                    hoverProgress = 1f;
                    hoverTimer.stop();
                }
            } else {
                hoverProgress -= 0.05f;
                if (hoverProgress <= 0f) {
                    hoverProgress = 0f;
                    hoverTimer.stop();
                }
            }
            repaint();
        });
        hoverTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background with rounded corners
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        // Draw hover effect
        int width = (int) (getWidth() * hoverProgress);
        g2.setColor(hoverColor);
        g2.fillRoundRect(0, 0, width, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }
        return shape.contains(x, y);
    }
}
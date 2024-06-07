package view.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

public class ComboBoxUI extends BasicComboBoxUI {

    @Override
    protected JButton createArrowButton() {
        JButton button = new JButton();
        ImageIcon icon = new ImageIcon("src/main/resources/DropDown.png");
        Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    @Override
    protected ComboPopup createPopup() {
        BasicComboPopup popup = (BasicComboPopup) super.createPopup();
        popup.setBorder(BorderFactory.createEmptyBorder());
        JScrollPane scrollPane = (JScrollPane) popup.getComponent(0);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        JList<Object> list = popup.getList();
        list.setBackground(Color.WHITE);
        list.setForeground(Color.BLACK);
        list.setFont(new Font("Inter", Font.PLAIN, 14));
        list.setBorder(BorderFactory.createEmptyBorder());

        // Adiciona um efeito de hover
        list.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                int index = list.locationToIndex(evt.getPoint());
                if (index != -1) {
                    list.setSelectedIndex(index);
                    list.repaint();
                }
            }
        });

        list.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return popup;
    }
}

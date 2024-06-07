package view.screens.manager;

import controller.BankControllerManager;
import view.components.RoundedFrame;
import view.components.StylizedButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ManagerMenuScreen extends RoundedFrame {
    private static ManagerMenuScreen instance;
    private final int accountLoggedId;
    private JButton btnHistory;
    private JButton btnReviewPending;
    private final BankControllerManager bankControllerManager;

    private ManagerMenuScreen(BankControllerManager bankControllerManager, int accountLoggedId) {
        super("LogIn", 540, 900, 50, 50);
        this.accountLoggedId = accountLoggedId;
        this.bankControllerManager = bankControllerManager;
        initiPanels();
        setVisible(true);
    }

    public static ManagerMenuScreen getInstance(BankControllerManager bankControllerManager, int accountLoggedId) {
        if (instance == null) {
            instance = new ManagerMenuScreen(bankControllerManager, accountLoggedId);
        }
        return instance;
    }

    private void initiPanels() {
        setLayout(new BorderLayout());
        add(createLayeredPane(), BorderLayout.CENTER);
    }

    private JLayeredPane createLayeredPane() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(540, 900));

        try {
            File file = new File("src/main/resources/images/ManagerMenuScreen.png");
            Image image = ImageIO.read(file);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            imageLabel.setBounds(0, 0, image.getWidth(null), image.getHeight(null));
            layeredPane.add(imageLabel, Integer.valueOf(0));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String managerName = bankControllerManager.getManagerName(accountLoggedId);

        JLabel labelClientName = new JLabel(managerName);
        labelClientName.setBounds(78, 42, 300, 30);
        labelClientName.setForeground(Color.WHITE);
        labelClientName.setFont(new Font("Inter", Font.PLAIN, 20));
        layeredPane.add(labelClientName, Integer.valueOf(1));

        btnHistory = new StylizedButton("");
        btnHistory.setBackground(new Color(0,0,0,0));
        btnHistory.setBounds(139, 469, 262, 44);
        btnHistory.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        layeredPane.add(btnHistory, Integer.valueOf(1));

        btnReviewPending = new StylizedButton("");
        btnReviewPending.setBackground(new Color(0, 0, 0,0));
        btnReviewPending.setBounds(139, 603, 262, 44);
        btnReviewPending.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        layeredPane.add(btnReviewPending, Integer.valueOf(1));

        return layeredPane;
    }

    public JButton getBtnHistory() {
        return btnHistory;
    }

    public JButton getBtnReviewPending() {
        return btnReviewPending;
    }
}

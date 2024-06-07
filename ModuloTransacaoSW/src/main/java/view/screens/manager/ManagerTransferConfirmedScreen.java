package view.screens.manager;

import controller.BankControllerManager;
import view.components.RoundedFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ManagerTransferConfirmedScreen extends RoundedFrame {
    private static ManagerTransferConfirmedScreen instance;
    private final BankControllerManager bankControllerManager;

    private ManagerTransferConfirmedScreen(BankControllerManager bankControllerManager) {
        super("Transação Confirmada", 540, 900, 40, 40);
        this.bankControllerManager = bankControllerManager;
        initComponents();

        setVisible(true);
    }

    public static ManagerTransferConfirmedScreen getInstance(BankControllerManager bankControllerManager) {
        if (instance == null) {
            instance = new ManagerTransferConfirmedScreen(bankControllerManager);
        }
        return instance;
    }

    private void initComponents() {
        JLayeredPane layeredPane = createLayeredPane();
        add(layeredPane, BorderLayout.CENTER);
    }

    private JLayeredPane createLayeredPane() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(540, 900));

        try (InputStream inputStream = getClass().getResourceAsStream("/images/ManagerTransferConfirmedScreen.png")) {
            if (inputStream != null) {
                BufferedImage image = ImageIO.read(inputStream);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                imageLabel.setBounds(0, 0, image.getWidth(), image.getHeight());
                layeredPane.add(imageLabel, 0);
            } else {
                System.err.println("Arquivo de imagem não encontrado");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel lblIssuerName = createLabel(bankControllerManager.getIssuerName(), 454, 320, 25);
        layeredPane.add(lblIssuerName, Integer.valueOf(1));

        JLabel lblIssuerCpf = createLabel(bankControllerManager.getIssuerCpf(), 489, 280, 25);
        layeredPane.add(lblIssuerCpf, Integer.valueOf(1));

        String transactionValue = String.valueOf(bankControllerManager.getTransactionValue());
        JLabel lblTransactionValue = createLabel("R$ " + transactionValue, 524, 250, 25);
        layeredPane.add(lblTransactionValue, Integer.valueOf(1));

        JLabel lblConfirmationDate = createLabel(bankControllerManager.getConfirmationDate(), 559, 200, 25);
        layeredPane.add(lblConfirmationDate, Integer.valueOf(1));

        JLabel lblRecieverName = createLabel(bankControllerManager.getRecieverName(), 670, 350, 30);
        layeredPane.add(lblRecieverName, Integer.valueOf(1));

        JLabel lblRecieverCpf = createLabel(bankControllerManager.getRecieverCpf(), 705, 250, 30);
        layeredPane.add(lblRecieverCpf, Integer.valueOf(1));

        JLabel lblBankName = createLabel(bankControllerManager.getBankName(), 744, 200, 25);
        layeredPane.add(lblBankName, Integer.valueOf(1));

        JLabel lblAccountType = createLabel(bankControllerManager.getAccountType(), 779, 300, 25);
        layeredPane.add(lblAccountType, Integer.valueOf(1));

        JButton backButton = new JButton("");
        backButton.setBounds(0, 830, 560, 70);
        backButton.setFont(new Font("Inter", Font.BOLD, 16));
        backButton.setBackground(new Color(0, 0, 0, 0));
        backButton.setForeground(Color.WHITE);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(_ -> System.exit(0));
        layeredPane.add(backButton, Integer.valueOf(2));

        return layeredPane;
    }

    private JLabel createLabel(String text, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Inter", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);
        label.setBounds(165, y, width, height);
        return label;
    }
}


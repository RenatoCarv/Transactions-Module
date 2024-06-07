package view.screens.manager;

import controller.BankControllerManager;
import view.components.RoundedFrame;
import view.components.StylizedButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ManagerReviewTransferScreen extends RoundedFrame {
    private static ManagerReviewTransferScreen instance;
    private final BankControllerManager bankControllerManager;
    private final ManagerTransferPendingScreen managerTransferPendingScreen;

    private ManagerReviewTransferScreen(BankControllerManager bankControllerManager, ManagerTransferPendingScreen managerTransferPendingScreen) {
        super("Revisão da Transação", 540, 900, 40, 40);
        this.bankControllerManager = bankControllerManager;
        this.managerTransferPendingScreen = managerTransferPendingScreen;
        initComponents();
        setVisible(true);
    }

    public static ManagerReviewTransferScreen getInstance(BankControllerManager bankControllerManager, ManagerTransferPendingScreen managerTransferPendingScreen) {
        if (instance == null) {
            instance = new ManagerReviewTransferScreen(bankControllerManager, managerTransferPendingScreen);
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

        try {
            File file = new File("src/main/resources/images/ManagerReviewPendingScreen.png");
            Image image = ImageIO.read(file);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            imageLabel.setBounds(0, 0, image.getWidth(null), image.getHeight(null));
            layeredPane.add(imageLabel);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        JLabel lblIssuerName = createLabel(bankControllerManager.getIssuerName(), 349, 280, 25);
        layeredPane.add(lblIssuerName, Integer.valueOf(1));

        JLabel lblIssuerCpf = createLabel(bankControllerManager.getIssuerCpf(), 376, 250, 25);
        layeredPane.add(lblIssuerCpf, Integer.valueOf(1));

        JLabel lblRecieverName = createLabel(bankControllerManager.getRecieverName(), 470, 350, 30);
        layeredPane.add(lblRecieverName, Integer.valueOf(1));

        JLabel lblRecieverCpf = createLabel(bankControllerManager.getRecieverCpf(), 510, 250, 30);
        layeredPane.add(lblRecieverCpf, Integer.valueOf(1));

        JLabel lblBankName = createLabel(bankControllerManager.getBankName(), 553, 200, 25);
        layeredPane.add(lblBankName, Integer.valueOf(1));

        JLabel lblAccountType = createLabel(bankControllerManager.getAccountType(), 592, 300, 25);
        layeredPane.add(lblAccountType, Integer.valueOf(1));

        String transactionValue = String.valueOf(bankControllerManager.getTransactionValue());
        JLabel lblTransactionValue = createLabel("R$ " + transactionValue, 635, 300, 25);
        layeredPane.add(lblTransactionValue, Integer.valueOf(1));

        JLabel lblConfirmationDate = createLabel(bankControllerManager.getConfirmationDate(), 678, 200, 25);
        layeredPane.add(lblConfirmationDate, Integer.valueOf(1));

        JButton btnApprove = new StylizedButton("");
        btnApprove.setBackground(new Color(0,0,0,0));
        btnApprove.setBounds(60, 767, 180, 44);
        btnApprove.addActionListener(_ -> {
            bankControllerManager.confirmTransaction(managerTransferPendingScreen.getRegistryId());
            bankControllerManager.showTransactionConfirmedScreen();
        });
        layeredPane.add(btnApprove, Integer.valueOf(1));

        JButton btnReject = new StylizedButton("");
        btnReject.setBackground(new Color(0,0,0,0));
        btnReject.setBounds(300, 767, 180, 44);
        btnReject.addActionListener(_ -> {
            bankControllerManager.rejectTransaction(managerTransferPendingScreen.getRegistryId());
            bankControllerManager.showTransactionRejectedScreen();
        });
        layeredPane.add(btnReject, Integer.valueOf(1));

        return layeredPane;
    }

    private JLabel createLabel(String text, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Inter", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);
        label.setBounds(192, y, width, height);
        return label;
    }
}

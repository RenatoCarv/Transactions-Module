package view.screens.client;

import controller.BankController;
import view.components.RoundedFrame;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ConfirmationScreen extends RoundedFrame {
    private static ConfirmationScreen instance;
    private final MenuScreen menuScreen;
    private final TransferScreen transferScreen;
    private final BankController bankController;
    private final int accountRecieverId;

    private ConfirmationScreen(BankController bankController, MenuScreen menuScreen, TransferScreen transferScreen, int accountRecieverId) {
        super("Tela de Confirmação", 540, 900, 40, 40);
        this.menuScreen = menuScreen;
        this.transferScreen = transferScreen;
        this.accountRecieverId = accountRecieverId;
        this.bankController = bankController;
        initiPanels();
        setVisible(true);
    }

    public static ConfirmationScreen getInstance(BankController bankController, MenuScreen menuScreen, TransferScreen transferScreen, int accountRecieverId) {
        if (instance == null) {
            instance = new ConfirmationScreen(bankController, menuScreen, transferScreen, accountRecieverId);
        }
        return instance;
    }

    private void initiPanels() {
        JLayeredPane layeredPane = createLayeredPane();
        add(layeredPane, BorderLayout.CENTER);
    }

    private JLayeredPane createLayeredPane() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(540, 900));

        try {
            File file = new File("src/main/resources/images/TransferConfirmedScreen.png");
            Image image = ImageIO.read(file);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            imageLabel.setBounds(0, 0, image.getWidth(null), image.getHeight(null));
            layeredPane.add(imageLabel, Integer.valueOf(0));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String transactionalValue = transferScreen.getTransactionalValue();

        JLabel label1 = new JLabel("R$ " + transactionalValue);
        label1.setForeground(Color.WHITE);
        label1.setFont(new Font("Inter", Font.PLAIN, 14));
        label1.setBounds(253, 593, 185, 38);
        layeredPane.add(label1, Integer.valueOf(1));

        String recieverCpf = formatarCpf(bankController.getReceiverCpf(accountRecieverId, menuScreen.getSelectedTransactionType()));

        JLabel label2 = new JLabel(recieverCpf);
        label2.setBounds(253, 630, 200, 30);
        label2.setFont(new Font("Inter", Font.PLAIN, 14));
        label2.setForeground(Color.WHITE);
        layeredPane.add(label2, Integer.valueOf(1));

        String recieverName = bankController.getRecieverName(accountRecieverId, menuScreen.getSelectedTransactionType());

        JLabel label3 = new JLabel(recieverName);
        label3.setForeground(Color.WHITE);
        label3.setBounds(253, 665, 300, 30);
        label3.setFont(new Font("Inter", Font.PLAIN, 14));
        layeredPane.add(label3, Integer.valueOf(1));

        String transactionType = menuScreen.getSelectedTransactionType();

        JLabel label4 = new JLabel(transactionType);
        label4.setBounds(253, 705, 260, 20);
        label4.setForeground(Color.WHITE);
        label4.setFont(new Font("Inter", Font.PLAIN, 14));
        layeredPane.add(label4, Integer.valueOf(1));

        JButton backButton = new JButton("");
        backButton.setBounds(0, 830, 560, 70);
        backButton.setFont(new Font("Inter", Font.BOLD, 16));
        backButton.setBackground(new Color(0, 0, 0, 0));
        backButton.setForeground(Color.WHITE);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(_ -> System.exit(0)); // Encerra o programa
        layeredPane.add(backButton, Integer.valueOf(1));

        return layeredPane;
    }

    private String formatarCpf(String cpf) {
        if (cpf != null && cpf.length() == 11) {
            return "***." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-**";
        }
        return cpf;
    }
}
package view.screens.client;

import view.components.CustomCaret;
import view.components.StylizedButton;
import view.components.RoundedFrame;
import controller.BankController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TransferScreen extends RoundedFrame {
    private static TransferScreen instance;
    private final BankController bankController;
    private JLayeredPane currentPanel;
    private final int accountLoggedId;
    private final int accountRecieverId;
    private String transactionalValue;
    private final MenuScreen menuScreen;

    private TransferScreen(BankController bankController, int accountLoggedId, int accountRecieverId, MenuScreen menuScreen) {
        super("Transferência", 540, 900, 40, 40);
        this.bankController = bankController;
        this.accountLoggedId = accountLoggedId;
        this.accountRecieverId = accountRecieverId;
        this.menuScreen = menuScreen;
        initiPanels();
        setVisible(true);
    }

    public static TransferScreen getInstance(BankController bankController, int accountLoggedId, int accountRecieverId, MenuScreen menuScreen) {
        if (instance == null) {
            instance = new TransferScreen(bankController, accountLoggedId, accountRecieverId, menuScreen);
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
            File file = new File("src/main/resources/images/TransferScreen.png");
            Image image = ImageIO.read(file);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            imageLabel.setBounds(0, 0, image.getWidth(null), image.getHeight(null));
            layeredPane.add(imageLabel, Integer.valueOf(0));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        double clientBalance = bankController.getClientBalance(accountLoggedId);

        JLabel label = new JLabel(String.format("%.2f", clientBalance));
        label.setBounds(320, 117, 185, 38);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Inter", Font.BOLD, 14));
        layeredPane.add(label, Integer.valueOf(1));

        // Text Field
        JTextField textFieldValue = createTextField();
        textFieldValue.setBackground(new Color(11, 59, 85));
        textFieldValue.setForeground(Color.WHITE);
        textFieldValue.setFont(new Font("Inter", Font.BOLD, 14));

        CustomCaret customCaret = new CustomCaret(Color.WHITE);
        customCaret.setBlinkRate(textFieldValue.getCaret().getBlinkRate());
        textFieldValue.setCaret(customCaret);
        textFieldValue.setCaretColor(Color.WHITE);
        layeredPane.add(textFieldValue, Integer.valueOf(1));

        JButton btnReview = new StylizedButton("Revisar");
        btnReview.setBounds(183, 275, 160, 35);
        btnReview.setFont(new Font("Inter", Font.BOLD, 15));
        btnReview.setBackground(Color.WHITE);
        btnReview.setForeground(Color.BLACK);
        btnReview.setBorder(null);
        btnReview.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnReview.addActionListener(_ -> {
            String insertedValue = textFieldValue.getText();

            if (insertedValue.matches("\\d+(\\.\\d+)?") && Double.parseDouble(insertedValue) > 0) {
                double doubleTransactionValue = Double.parseDouble(insertedValue);

                if (doubleTransactionValue <= clientBalance) {
                    transactionalValue = insertedValue;
                    createAndAddNewPanel(layeredPane);
                } else {
                    JOptionPane.showMessageDialog(this, "O valor da transação excede o saldo disponível na conta.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, insira um valor válido para a transação.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        layeredPane.add(btnReview, Integer.valueOf(1));

        JButton btnBack = new StylizedButton("");
        btnBack.setBackground(new Color(0, 0, 0, 0));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Inter", Font.BOLD, 16));
        btnBack.setBounds(30, 35, 30, 30);
        ImageIcon btnIcon = new ImageIcon("src/main/resources/images/Chevron_Left.png");
        Image scaledImage = btnIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        btnBack.setIcon(scaledIcon);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(_ -> bankController.backToMenu());
        layeredPane.add(btnBack, Integer.valueOf(1));

        return layeredPane;
    }

    private void createAndAddNewPanel(JLayeredPane layeredPane) {
        if (currentPanel != null) {
            layeredPane.remove(currentPanel);
        }

        JLayeredPane newLayeredPane = new JLayeredPane();
        newLayeredPane.setPreferredSize(new Dimension(540, 550));

        ImageIcon icon = new ImageIcon("src/main/resources/images/ReviewInformations.png");
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(0, 25, 540, 400);
        newLayeredPane.add(imageLabel, Integer.valueOf(0));

        JLabel label1 = new JLabel("R$ " + getTransactionalValue());
        label1.setForeground(Color.WHITE);
        label1.setFont(new Font("Inter", Font.BOLD, 14));
        label1.setBounds(320, 54, 185, 38);
        newLayeredPane.add(label1, Integer.valueOf(1));

        String recieverName = bankController.getRecieverName(accountRecieverId, menuScreen.getSelectedTransactionType());
        String recieverCpf = cpfFormat(bankController.getReceiverCpf(accountRecieverId, menuScreen.getSelectedTransactionType()));
        String recieverBank = bankController.getBankName(accountRecieverId, menuScreen.getSelectedTransactionType());
        String recieverBankType = bankController.getAccountType(accountRecieverId, menuScreen.getSelectedTransactionType());

        JLabel label2 = new JLabel(recieverName);
        label2.setBounds(116, 213, 300, 30);
        label2.setFont(new Font("Inter", Font.BOLD, 14));
        label2.setForeground(Color.BLACK);
        newLayeredPane.add(label2, Integer.valueOf(1));

        JLabel label3 = new JLabel(recieverCpf);
        label3.setForeground(Color.BLACK);
        label3.setBounds(116, 253, 200, 30);
        label3.setFont(new Font("Inter", Font.BOLD, 14));
        newLayeredPane.add(label3, Integer.valueOf(1));

        JLabel label4 = new JLabel(recieverBank);
        label4.setBounds(116, 293, 100, 30);
        label4.setForeground(Color.BLACK);
        label4.setFont(new Font("Inter", Font.BOLD, 14));
        newLayeredPane.add(label4, Integer.valueOf(1));

        JLabel label5 = new JLabel(recieverBankType);
        label5.setBounds(116, 333 , 100, 30);
        label5.setForeground(Color.BLACK);
        label5.setFont(new Font("Inter", Font.BOLD, 14));
        newLayeredPane.add(label5, Integer.valueOf(1));

        JButton btnConfirm = new StylizedButton("Confirmar");
        btnConfirm.setBackground(new Color(13, 80, 114));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFont(new Font("Inter", Font.BOLD, 16));
        btnConfirm.setBounds(147, 432, 245, 50);
        btnConfirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConfirm.addActionListener(_ -> {
            double valor = Double.parseDouble(transactionalValue);
            bankController.doTransaction(menuScreen.getSelectedTransactionType(), transactionalValue, accountLoggedId, accountRecieverId);

            if (valor >= 50000) {
                bankController.showPendingScreen();
            } else {
                bankController.showConfirmationScreen();
            }

            dispose();
        });

        newLayeredPane.add(btnConfirm, Integer.valueOf(2));

        int panelWidth = 540;
        int panelX = 0;

        newLayeredPane.setBounds(panelX, 350, panelWidth, 550);
        layeredPane.add(newLayeredPane, Integer.valueOf(2));

        currentPanel = newLayeredPane;

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBounds(323, 193, 185, 38);
        textField.setBorder(null);
        textField.setOpaque(false);
        textField.setBackground(new Color(0, 0, 0, 0));
        textField.setForeground(Color.BLACK);
        return textField;
    }

    private String cpfFormat(String cpf) {
        if (cpf != null && cpf.length() == 11) {
            return "***." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-**";
        }
        return cpf;
    }

    public String getTransactionalValue() {
        return transactionalValue;
    }
}
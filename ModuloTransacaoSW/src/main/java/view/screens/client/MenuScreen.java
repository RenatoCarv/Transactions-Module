package view.screens.client;

import controller.BankController;
import view.components.ComboBoxUI;
import view.components.RoundTextField;
import view.components.StylizedButton;
import view.components.RoundedFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MenuScreen extends RoundedFrame {
    private JButton btnNext;
    private JButton btnHistory;
    private JComboBox<String> transferOptions;
    private static MenuScreen instance;
    private JTextField textFieldAccountNumber;
    private JTextField textFieldBankNumber;
    private JTextField textFieldAgencyNumber;
    private final int accountLoggedId;
    private String selectedTransactionType;
    private final BankController bankController;
    private JLabel errorLabel;

    private MenuScreen(BankController bankController, int accountLoggedId) {
        super("main.Main", 540, 900, 50, 50);
        this.accountLoggedId = accountLoggedId;
        this.bankController = bankController;
        initiPanels();
        setVisible(true);
    }

    public static MenuScreen getInstance(BankController bankController, int accountLoggedId) {
        if (instance == null) {
            instance = new MenuScreen(bankController, accountLoggedId);
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
            File file = new File("src/main/resources/images/MenuScreen.png");
            Image image = ImageIO.read(file);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            imageLabel.setBounds(0, 0, image.getWidth(null), image.getHeight(null));
            layeredPane.add(imageLabel, Integer.valueOf(0));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String clientName = bankController.getClientName(accountLoggedId);
        double clientBalance = bankController.getClientBalance(accountLoggedId);

        JLabel labelClientName = new JLabel(clientName);
        labelClientName.setBounds(105, 90, 300, 30);
        labelClientName.setForeground(Color.BLACK);
        labelClientName.setFont(new Font("Inter", Font.BOLD, 12));
        layeredPane.add(labelClientName, Integer.valueOf(1));

        JLabel labelBalance = new JLabel(String.format("%.2f", clientBalance));
        labelBalance.setBounds(135, 225, 200, 30);
        labelBalance.setForeground(Color.BLACK);
        labelBalance.setFont(new Font("Inter", Font.BOLD, 12));
        layeredPane.add(labelBalance, Integer.valueOf(1));

        JButton btnTransfer = new StylizedButton("", new Color(11, 72, 105), new Color(0, 148, 222, 150));
        btnTransfer.setBounds(153, 320, 60, 60);
        ImageIcon btnIcon1 = new ImageIcon("src/main/resources/images/Transfer_Icon.png");
        Image scaledImage1 = btnIcon1.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);
        btnTransfer.setIcon(scaledIcon1);
        btnTransfer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        layeredPane.add(btnTransfer, Integer.valueOf(1));

        btnHistory = new StylizedButton("", new Color(11, 72, 105), new Color(0, 148, 222, 150));
        btnHistory.setBounds(326, 320, 60, 60);
        ImageIcon btnIcon2 = new ImageIcon("src/main/resources/images/Historical_Icon.png");
        Image scaledImage2 = btnIcon2.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon2 = new ImageIcon(scaledImage2);
        btnHistory.setIcon(scaledIcon2);
        btnHistory.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        layeredPane.add(btnHistory, Integer.valueOf(1));

        btnNext = new StylizedButton("", new Color(11, 72, 105), new Color(0, 148, 222, 150));
        btnNext.setForeground(Color.WHITE);
        btnNext.setFont(new Font("Inter", Font.BOLD, 16));
        btnNext.setBounds(450, 800, 60, 60);
        ImageIcon btnIcon3 = new ImageIcon("src/main/resources/images/Arrow_Icon.png");
        Image scaledImage3 = btnIcon3.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon3 = new ImageIcon(scaledImage3);
        btnNext.setIcon(scaledIcon3);
        btnNext.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNext.addActionListener(_ -> {
            clearDynamicComponents(layeredPane);

            String selectedOption = (String) transferOptions.getSelectedItem();
            selectedTransactionType = selectedOption;

            String accountNumber = textFieldAccountNumber.getText();
            String agencyNumber = textFieldAgencyNumber != null ? textFieldAgencyNumber.getText() : null;
            String bankNumber = textFieldBankNumber != null ? textFieldBankNumber.getText() : null;

            try{
                
                switch (Objects.requireNonNull(selectedOption)) {
                    case "Transferência para conta própria":
                        if (accountNumber != null && !accountNumber.isEmpty()) {
                            bankController.verifyOwnAccount(Integer.parseInt(accountNumber));
                        } else {
                            setError("Credenciais inválidas");
                        }
                        break;
                    case "Transferência para terceiros":
                        if (accountNumber != null && !accountNumber.isEmpty() && agencyNumber != null && !agencyNumber.isEmpty()) {
                            bankController.verifyThirdPartyAccount(Integer.parseInt(accountNumber), Integer.parseInt(agencyNumber));
                        } else {
                            setError("Credenciais inválidas");
                        }
                        break;
                    case "Transferência para outro banco":
                        if (accountNumber != null && !accountNumber.isEmpty() && agencyNumber != null && !agencyNumber.isEmpty() && bankNumber != null && !bankNumber.isEmpty()) {
                            bankController.verifyExternalAccount(Integer.parseInt(accountNumber), Integer.parseInt(agencyNumber), Integer.parseInt(bankNumber));
                        } else {
                            setError("Credenciais inválidas");
                        }
                        break;
                    default:
                        break;
                }
            }catch (NumberFormatException e) {
                setError("Por favor, insira um número de transação válido.");
            }

        });

        layeredPane.add(btnNext, Integer.valueOf(1));

        btnTransfer.addActionListener(_ -> {
            transferOptions = new JComboBox<>(new String[]{"Transferência para conta própria", "Transferência para terceiros", "Transferência para outro banco"});
            transferOptions.setBounds(140, 450, 260, 30);

            transferOptions.setBackground(Color.WHITE);
            transferOptions.setForeground(Color.BLACK);
            transferOptions.setFont(new Font("Inter", Font.PLAIN, 14));

            transferOptions.setUI(new ComboBoxUI());

            layeredPane.add(transferOptions, Integer.valueOf(2));

            transferOptions.addActionListener(_ -> {
                String selectedOption = (String) transferOptions.getSelectedItem();
                JPanel panel = new JPanel();
                panel.setBounds(0, 500, 540, 200);
                panel.setBackground(Color.WHITE);
                panel.setLayout(null);

                Component[] components = layeredPane.getComponents();
                for (Component comp : components) {
                    if (comp.getBounds().y >= 500 && comp.getBounds().y <= 700) {
                        layeredPane.remove(comp);
                    }
                }

                switch (Objects.requireNonNull(selectedOption)) {
                    case "Transferência para conta própria":
                        createPanelForSelfTransfer(panel);
                        break;
                    case "Transferência para terceiros":
                        createPanelForThirdPartyTransfer(panel);
                        break;
                    case "Transferência para outro banco":
                        createPanelForOtherBankTransfer(panel);
                        break;
                    default:
                        break;
                }

                layeredPane.add(panel, Integer.valueOf(1));
                revalidate();
                repaint();
            });
        });

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setBounds(70, 730, 400, 30);
        layeredPane.add(errorLabel, Integer.valueOf(2));

        return layeredPane;
    }

    private void clearDynamicComponents(JLayeredPane layeredPane) {
        Component[] components = layeredPane.getComponents();
        for (Component comp : components) {
            if (comp.getBounds().y >= 500 && comp.getBounds().y <= 700) {
                layeredPane.remove(comp);
            }
        }
        revalidate();
        repaint();
    }

    private void createPanelForSelfTransfer(JPanel panel) {
        JLabel label = new JLabel("Número da conta:");
        label.setBounds(60, 70, 200, 30);
        label.setForeground(Color.black);
        panel.add(label);

        JTextField textField = createRoundTextField(70);
        panel.add(textField);

        textFieldAccountNumber = textField;
    }

    private void createPanelForThirdPartyTransfer(JPanel panel) {
        JLabel label1 = new JLabel("Número da conta:");
        label1.setBounds(60, 70, 200, 30);
        label1.setForeground(Color.black);
        panel.add(label1);

        JTextField textField1 = createRoundTextField(70);
        panel.add(textField1);

        textFieldAccountNumber = textField1;

        JLabel label2 = new JLabel("Número da agência:");
        label2.setBounds(60, 110, 200, 30);
        label2.setForeground(Color.black);
        panel.add(label2);

        JTextField textField2 = createRoundTextField(110);
        panel.add(textField2);

        textFieldAgencyNumber = textField2;
    }

    private void createPanelForOtherBankTransfer(JPanel panel) {
        JLabel label1 = new JLabel("Número da conta:");
        label1.setBounds(60, 70, 200, 30);
        label1.setForeground(Color.black);
        panel.add(label1);

        JTextField textField1 = createRoundTextField(70);
        panel.add(textField1);

        textFieldAccountNumber = textField1;

        JLabel label2 = new JLabel("Número da agência:");
        label2.setBounds(60, 110, 200, 30);
        label2.setForeground(Color.black);
        panel.add(label2);

        JTextField textField2 = createRoundTextField(110);
        panel.add(textField2);

        textFieldAgencyNumber = textField2;

        JLabel label3 = new JLabel("Número do banco:");
        label3.setBounds(60, 150, 200, 30);
        label3.setForeground(Color.black);
        panel.add(label3);

        JTextField textField3 = createRoundTextField(150);
        panel.add(textField3);

        textFieldBankNumber = textField3;
    }

    private RoundTextField createRoundTextField(int y) {
        RoundTextField textField = new RoundTextField(20, new Color(0, 0, 0, 100));
        textField.setBounds(260, y, 200, 30);
        textField.setFont(new Font("Roboto", Font.PLAIN, 14));
        textField.setForeground(Color.BLACK);
        textField.setBackground(Color.WHITE);
        textField.setBorderWidth(0.2f);
        return textField;
    }

    public JButton getBtnNext() {
        return btnNext;
    }

    public JButton getBtnHistory() {
        return btnHistory;
    }

    public JTextField getTextFieldAccountNumber() {
        return textFieldAccountNumber;
    }

    public JTextField getTextFieldBankNumber() {
        return textFieldBankNumber;
    }

    public JTextField getTextFieldAgencyNumber() {
        return textFieldAgencyNumber;
    }

    public JComboBox<String> getTransferOptions() {
        return transferOptions;
    }

    public String getSelectedTransactionType() {
        return selectedTransactionType;
    }

    public void setError(String errorMessage) {
        errorLabel.setText(errorMessage);
    }
}

package view.screens.manager;

import controller.BankControllerManager;
import view.components.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ManagerTransferPendingScreen extends RoundedFrame {
    private static ManagerTransferPendingScreen instance;
    private final BankControllerManager bankControllerManager;
    private JTextArea textArea;
    private JTextField textFieldRegistryId;
    private int registryId;
    private JLabel errorLabel;
    private final List<Integer> pendingTransactionIds;

    private ManagerTransferPendingScreen(BankControllerManager bankControllerManager) {
        super("Transações Pendentes", 540, 900, 40, 40);
        this.bankControllerManager = bankControllerManager;
        initComponents();
        List<String> pendingTransactions = bankControllerManager.getBankPendingTransactions();
        showPendingTransactions(pendingTransactions);
        this.pendingTransactionIds = extractTransactionIds(pendingTransactions);
        setVisible(true);
    }

    public static ManagerTransferPendingScreen getInstance(BankControllerManager bankControllerManager) {
        if (instance == null) {
            instance = new ManagerTransferPendingScreen(bankControllerManager);
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
            File file = new File("src/main/resources/images/ManagerTransferPendingScreen.png");
            Image image = ImageIO.read(file);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            imageLabel.setBounds(0, 0, image.getWidth(null), image.getHeight(null));
            layeredPane.add(imageLabel);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFocusable(false);
        textArea.setFont(new Font("Inter", Font.PLAIN, 15));
        textArea.setForeground(Color.WHITE);
        textArea.setBounds(40, 420, 470, 155);
        textArea.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBounds(40, 420, 470, 155);
        scrollPane.setFont(new Font("Inter", Font.BOLD, 14));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0)));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setOpaque(false);
        verticalScrollBar.setUI(new CustomScrollBarUI());

        RoundedPanel roundedPanel = new RoundedPanel(15, new Color(0, 0, 0, 0));
        roundedPanel.setLayout(new BorderLayout());
        roundedPanel.setBounds(40, 420, 470, 155);
        roundedPanel.add(scrollPane, BorderLayout.CENTER);

        layeredPane.add(roundedPanel, Integer.valueOf(1));

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
        btnBack.addActionListener(_ -> bankControllerManager.backToPendingTransactionMenu());
        layeredPane.add(btnBack, Integer.valueOf(1));

        textFieldRegistryId = createRoundTextField();
        layeredPane.add(textFieldRegistryId, Integer.valueOf(1));

        JButton btnNext = new StylizedButton("");
        btnNext.setBackground(new Color(54, 168, 161));
        btnNext.setForeground(Color.WHITE);
        btnNext.setFont(new Font("Inter", Font.BOLD, 16));
        btnNext.setBounds(450, 800, 60, 60);
        ImageIcon btnIcon3 = new ImageIcon("src/main/resources/images/Arrow_Icon.png");
        Image scaledImage3 = btnIcon3.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon3 = new ImageIcon(scaledImage3);
        btnNext.setIcon(scaledIcon3);
        btnNext.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNext.addActionListener(_ -> handleNextButtonClick());
        layeredPane.add(btnNext, Integer.valueOf(1));

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.WHITE);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setBounds(70, 750, 400, 30);
        layeredPane.add(errorLabel, Integer.valueOf(2));

        return layeredPane;
    }

    public void showPendingTransactions(List<String> transactions) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < transactions.size(); i++) {
            text.append("Transação ").append(i + 1).append(":\n");
            text.append(transactions.get(i)).append("\n\n");
        }
        textArea.setText(text.toString());
    }

    private void handleNextButtonClick() {
        try {
            registryId = Integer.parseInt(textFieldRegistryId.getText());

            if (pendingTransactionIds.contains(registryId)) {
                bankControllerManager.getDetailsTransaction(registryId);
                bankControllerManager.showReviewTransactionScreen();
            } else {
                setError("O ID da transação não está na lista de transações pendentes.");
            }
        } catch (NumberFormatException e) {
            setError("Por favor, insira um número de transação válido.");
        }
    }

    private List<Integer> extractTransactionIds(List<String> pendingTransactions) {
        return pendingTransactions.stream()
                .map(transaction -> {
                    String[] lines = transaction.split("\n");
                    for (String line : lines) {
                        if (line.startsWith("ID:")) {
                            return Integer.parseInt(line.replace("ID:", "").trim());
                        }
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    private RoundTextField createRoundTextField() {
        RoundTextField textField = new RoundTextField(20,  new Color(255, 255 ,255, 150));
        textField.setBounds(260, 710, 200, 30);
        textField.setFont(new Font("Roboto", Font.PLAIN, 14));
        textField.setForeground(Color.BLACK);
        textField.setBackground(Color.WHITE);
        textField.setBorderWidth(0.2f);
        return textField;
    }

    public int getRegistryId() {
        return registryId;
    }

    public void setError(String errorMessage) {
        errorLabel.setText(errorMessage);
    }
}

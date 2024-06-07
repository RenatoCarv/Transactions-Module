package view.screens.manager;

import controller.BankControllerManager;
import view.components.CustomScrollBarUI;
import view.components.RoundedFrame;
import view.components.RoundedPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ManagerHistoryScreen extends RoundedFrame {
    private static ManagerHistoryScreen instance;
    private final BankControllerManager bankControllerManager;
    private JTextArea textArea;

    public static synchronized ManagerHistoryScreen getInstance(BankControllerManager bankControllerManager) {
        if (instance == null) {
            instance = new ManagerHistoryScreen(bankControllerManager);
        }
        return instance;
    }

    private ManagerHistoryScreen(BankControllerManager bankControllerManager) {
        super("Histórico de Transações", 540, 900, 40, 40);
        this.bankControllerManager = bankControllerManager;
        initComponents();
        showTransactions(bankControllerManager.getBankTransactions());
        setVisible(true);
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
            File file = new File("src/main/resources/images/ManagerHistoryScreen.png");
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
        textArea.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBounds(35, 470, 480, 270);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setOpaque(false);
        verticalScrollBar.setUI(new CustomScrollBarUI());

        RoundedPanel roundedPanel = new RoundedPanel(15, new Color(0, 0, 0, 0));
        roundedPanel.setLayout(new BorderLayout());
        roundedPanel.setBounds(35, 480, 480, 270);
        roundedPanel.add(scrollPane, BorderLayout.CENTER);

        layeredPane.add(roundedPanel, Integer.valueOf(1));

        JButton backButton = new JButton("");
        backButton.setBounds(0, 830, 560, 70);
        backButton.setFont(new Font("Inter", Font.BOLD, 16));
        backButton.setBackground(new Color(0, 0, 0, 0));
        backButton.setForeground(Color.WHITE);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(_ -> bankControllerManager.backToMenuFromHistory());
        layeredPane.add(backButton, Integer.valueOf(2));

        return layeredPane;
    }

    public void showTransactions(List<String> transacoes) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < transacoes.size(); i++) {
            text.append("Transação ").append(i + 1).append(":\n");
            text.append(transacoes.get(i)).append("\n\n");
        }

        textArea.setText(text.toString());
    }
}


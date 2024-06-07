package view.screens.client;

import controller.BankController;
import view.components.CustomScrollBarUI;
import view.components.RoundedFrame;
import view.components.RoundedPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class HistoryScreen extends RoundedFrame {
    private static HistoryScreen instance;
    private final BankController bankController;
    private JTextArea textArea;

    private HistoryScreen(BankController bankController) {
        super("Histórico de Transações", 540, 900, 40, 40);
        this.bankController = bankController;
        initComponents();
        showTransactions(bankController.getUserTransactions(bankController.getAccountLoggedId()));
        setVisible(true);
    }

    public static HistoryScreen getInstance(BankController bankController) {
        if (instance == null) {
            instance = new HistoryScreen(bankController);
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

        try (InputStream inputStream = getClass().getResourceAsStream("/images/HistoryScreen.png")) {
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
        scrollPane.setFont(new Font("Inter", Font.BOLD, 14));
        scrollPane.setBounds(35, 455, 480, 255);
        scrollPane.setBorder(new EmptyBorder(5,5,5,5));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setOpaque(false);
        verticalScrollBar.setUI(new CustomScrollBarUI());

        RoundedPanel roundedPanel = new RoundedPanel(15, new Color(0, 0, 0, 0));
        roundedPanel.setLayout(new BorderLayout());
        roundedPanel.setBounds(35, 485, 480, 255);
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
        backButton.addActionListener(_ -> bankController.backToMenuFromHistory());
        layeredPane.add(backButton, Integer.valueOf(2));

        return layeredPane;
    }

    public void showTransactions(List<String> transactions) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < transactions.size(); i++) {
            text.append("Transação ").append(i + 1).append(":\n");
            text.append(transactions.get(i)).append("\n\n");
        }

        textArea.setText(text.toString());
    }
}

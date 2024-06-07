package view.screens.manager;

import view.components.RoundedFrame;
import view.components.StylizedButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

public class ManagerLogInScreen extends RoundedFrame {
    private static ManagerLogInScreen instance;
    private JTextField textCpf;
    private JPasswordField textPassword;
    private JButton btnUserLogIn;
    private JLabel errorLabel;

    public static synchronized ManagerLogInScreen getInstance() {
        if (instance == null) {
            instance = new ManagerLogInScreen();
        }
        return instance;
    }

    private ManagerLogInScreen() {
        super("LogIn", 540, 900, 50, 50);
        initiPanels();
        setVisible(true);
    }

    private void initiPanels() {
        setLayout(new BorderLayout());

        add(createLayeredPane(), BorderLayout.CENTER);
    }

    private JLayeredPane createLayeredPane() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(540, 900));

        try {
            File file = new File("src/main/resources/images/ManagerLogInScreen.png");
            Image image = ImageIO.read(file);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            imageLabel.setBounds(0, 0, image.getWidth(null), image.getHeight(null));
            layeredPane.add(imageLabel, Integer.valueOf(0));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        btnUserLogIn = new StylizedButton("Login");
        btnUserLogIn.setBackground(new Color(54, 168, 161));
        btnUserLogIn.setForeground(Color.WHITE);
        btnUserLogIn.setFont(new Font("Inter", Font.BOLD, 16));
        btnUserLogIn.setBounds(70, 750, 400, 50);

        ImageIcon btnIcon = new ImageIcon("src/main/resources/images/Padlock_Icon.png");
        Image scaledImage = btnIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        btnUserLogIn.setIcon(scaledIcon);
        btnUserLogIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        layeredPane.add(btnUserLogIn, Integer.valueOf(1));

        textCpf = createTextField();
        textPassword = createPasswordField();

        textCpf.setBounds(80, 492, 390, 30);
        textPassword.setBounds(80, 642, 390, 30);

        layeredPane.add(textCpf, Integer.valueOf(1));
        layeredPane.add(textPassword, Integer.valueOf(1));

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setBounds(70, 700, 400, 30);
        layeredPane.add(errorLabel, Integer.valueOf(1));

        return layeredPane;
    }


    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBounds(70, 480, 390, 30);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals("Digite o CPF")) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText("Digite o CPF");
                    textField.setForeground(Color.GRAY);
                }
            }
        });

        textField.setFont(new Font("Roboto", Font.PLAIN, 14));
        textField.setText("Digite o CPF");
        textField.setForeground(Color.GRAY);

        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setOpaque(false);
        textField.setBackground(new Color(0, 0, 0, 0));

        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(70, 630, 390, 30);

        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals("Digite a senha")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Digite a senha");
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

        passwordField.setFont(new Font("Roboto", Font.PLAIN, 14));
        passwordField.setText("Digite a senha");
        passwordField.setForeground(Color.GRAY);

        passwordField.setBorder(BorderFactory.createEmptyBorder());
        passwordField.setOpaque(false);
        passwordField.setBackground(new Color(0, 0, 0, 0));

        return passwordField;
    }

    public String getTextCpf() {
        return textCpf.getText();
    }

    public String getTextPassword() {
        return String.valueOf(textPassword.getPassword());
    }

    public JButton getBtnUserLogIn() {
        return btnUserLogIn;
    }

    public void setError(String errorMessage) {
        errorLabel.setText(errorMessage);
    }
}
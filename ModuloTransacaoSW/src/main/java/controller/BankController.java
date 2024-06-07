package controller;

import model.client.BankModel;
import view.screens.client.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;

public class BankController {
    private final LogInScreen loginScreen;
    private final BankModel bankModel;
    private MenuScreen menuScreen;
    private TransferScreen transferScreen;
    private HistoryScreen historyScreen;

    public BankController() {
        this.bankModel = BankModel.getInstance();
        this.loginScreen = LogInScreen.getInstance(this);

        configureLogInButton();
    }

    private void configureLogInButton() {
        loginScreen.getBtnUserLogin().addActionListener(_ -> login(loginScreen.getTextCpf(), loginScreen.getTextPassword()));

        loginScreen.getBtnUserLogin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginScreen.getBtnUserLogin().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginScreen.getBtnUserLogin().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    public void login(String cpf, String password) {
        int accountLoggedId = bankModel.logInUser(cpf, password);

        if (accountLoggedId > 0) {
            if (menuScreen == null) {
                menuScreen = MenuScreen.getInstance(BankController.this, bankModel.getAccountLoggedId());
                bankModel.setAccountLoggedId(accountLoggedId);
                configureMenuButton();
            }
            loginScreen.dispose();
            menuScreen.setVisible(true);
        } else {
            loginScreen.setError("CPF ou senha incorretos.");
        }
    }


    private void configureMenuButton() {
        menuScreen.getBtnNext().addActionListener(_ -> manageOptionsTransactions());
        menuScreen.getBtnHistory().addActionListener(_ -> showHistoricalScreen());
    }

    private void manageOptionsTransactions() {
        if (menuScreen.getTransferOptions() != null) {
            try {

            String selectedOption = (String) menuScreen.getTransferOptions().getSelectedItem();
            boolean valid = false;

            switch (Objects.requireNonNull(selectedOption)) {
                case "Transferência para conta própria":
                    valid = validateOwnTransaction();
                    break;
                case "Transferência para terceiros":
                    valid = validateThirdPartyTransaction();
                    break;
                case "Transferência para outro banco":
                    valid = validateExternalTransaction();
                    break;
                default:
                    break;
            }

            if (valid) {
                menuScreen.setVisible(false);
                if (transferScreen == null) {
                    transferScreen = TransferScreen.getInstance(BankController.this, bankModel.getAccountLoggedId(), bankModel.getAccountRecieverId(), menuScreen); // Passa o controller para a view
                }
                transferScreen.setVisible(true);
            }

            } catch (NumberFormatException e) {
                menuScreen.setError("Por favor, insira um número de transação válido.");
            }
        } else {
            menuScreen.setError("Por favor, clique no botão 'Transferir' antes de prosseguir.");
        }

    }

    public void showHistoricalScreen() {
        menuScreen.setVisible(false);
        if (historyScreen == null) {
            historyScreen = HistoryScreen.getInstance(BankController.this);
        }
        historyScreen.setVisible(true);
    }

    public void showConfirmationScreen() {
        ConfirmationScreen confirmationScreen = ConfirmationScreen.getInstance(this, menuScreen, transferScreen, bankModel.getAccountRecieverId());
        confirmationScreen.setVisible(true);
    }

    public void showPendingScreen() {
        PendingScreen pendingScreen = PendingScreen.getInstance(BankController.this, menuScreen, transferScreen, bankModel.getAccountRecieverId());
        pendingScreen.setVisible(true);
    }

    public void backToMenu() {
        transferScreen.dispose();
        menuScreen.setVisible(true);
    }

    public void backToMenuFromHistory() {
        historyScreen.dispose();
        menuScreen.setVisible(true);
    }

    public LogInScreen getLoginScreen() {
        return loginScreen;
    }

    public boolean verifyOwnAccount(int accountNumber){
        return bankModel.verifyOwnAccount(accountNumber);
    }

    public boolean verifyThirdPartyAccount(int accountNumber, int AgencyNumber){
        return bankModel.verifyThirdPartyAccount(accountNumber, AgencyNumber);
    }

    public boolean verifyExternalAccount(int accountNumber, int agencyNumber, int bankCode){
        return bankModel.verifyExternalAccount(accountNumber, agencyNumber, bankCode);
    }

    public String getClientName(int idAccount){
        return bankModel.getClientName(idAccount);
    }

    public double getClientBalance(int idAccount){
        return bankModel.getClientBalance(idAccount);
    }

    public String getRecieverName(int idRecieverAccount, String selectedTransactionType){
        return bankModel.getRecieverName(idRecieverAccount, selectedTransactionType);
    }

    public String getReceiverCpf(int idRecieverAccount, String selectedTransactionType) {
        return bankModel.getReceiverCpf(idRecieverAccount, selectedTransactionType);
    }

    public String getBankName(int idRecieverAccount, String selectedTransactionType) {
        return bankModel.getBankName(idRecieverAccount, selectedTransactionType);
    }

    public String getAccountType(int idRecieverAccount, String selectedTransactionType) {
        return bankModel.getAccountType(idRecieverAccount, selectedTransactionType);
    }

    public void doTransaction(String selectedTransactionType, String transactionValue, int accountLoggedId, int accountRecieverId) {
        bankModel.doTransaction(selectedTransactionType, transactionValue, accountLoggedId, accountRecieverId);
    }

    public List<String> getUserTransactions(int idAccount){
        return bankModel.getUserTransactions(idAccount);
    }

    public int getAccountLoggedId(){
        return bankModel.getAccountLoggedId();
    }


    private boolean validateOwnTransaction() {
        String accountNumber = menuScreen.getTextFieldAccountNumber().getText().trim();
        if (accountNumber.isEmpty()) {
            menuScreen.setError("Informações inválidas");
            return false;
        }
        if (!verifyOwnAccount(Integer.parseInt(accountNumber))) {
            menuScreen.setError("Informações inválidas");
            return false;
        }
        return true;
    }

    private boolean validateThirdPartyTransaction() {
        String accountNumber = menuScreen.getTextFieldAccountNumber().getText().trim();
        String agencyNumber = menuScreen.getTextFieldAgencyNumber().getText().trim();
        if (accountNumber.isEmpty() || agencyNumber.isEmpty()) {
            menuScreen.setError("Informações inválidas");
            return false;
        }
        if (!verifyThirdPartyAccount(Integer.parseInt(accountNumber), Integer.parseInt(agencyNumber))) {
            menuScreen.setError("Informações inválidas");
            return false;
        }
        return true;
    }

    private boolean validateExternalTransaction() {
        String accountNumber = menuScreen.getTextFieldAccountNumber().getText().trim();
        String agencyNumber = menuScreen.getTextFieldAgencyNumber().getText().trim();
        String bankNumber = menuScreen.getTextFieldBankNumber().getText().trim();
        if (accountNumber.isEmpty() || agencyNumber.isEmpty() || bankNumber.isEmpty()) {
            menuScreen.setError("Informações inválidas");
            return false;
        }
        if (!verifyExternalAccount(Integer.parseInt(accountNumber), Integer.parseInt(agencyNumber), Integer.parseInt(bankNumber))) {
            menuScreen.setError("Informações inválidas");
            return false;
        }
        return true;
    }

}

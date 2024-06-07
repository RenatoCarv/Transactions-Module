package controller;

import model.manager.BankModelManager;
import view.screens.manager.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BankControllerManager {
    private final ManagerLogInScreen managerLogInScreen;
    private final BankModelManager bankModelManager;
    private ManagerHistoryScreen managerHistoryScreen;
    private ManagerMenuScreen managerMenuScreen;
    private ManagerTransferPendingScreen managerTransferPendingScreen;
    private ManagerReviewTransferScreen managerReviewPendingScreen;
    private ManagerTransferConfirmedScreen managerTransferConfirmedScreen;
    private ManagerTransferRejectedScreen managerTransferRejectedScreen;

    public BankControllerManager() {
        this.bankModelManager = BankModelManager.getInstance();
        this.managerLogInScreen = ManagerLogInScreen.getInstance();

        configureLogInButton();
    }

    private void configureLogInButton() {
        managerLogInScreen.getBtnUserLogIn().addActionListener(_ -> login(managerLogInScreen.getTextCpf(), managerLogInScreen.getTextPassword()));

        managerLogInScreen.getBtnUserLogIn().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                managerLogInScreen.getBtnUserLogIn().setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                managerLogInScreen.getBtnUserLogIn().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    public void login(String cpf, String senha) {
        int accountLoggedId = bankModelManager.login(cpf, senha);

        if (accountLoggedId > 0) {
            if (managerMenuScreen == null) {
                managerMenuScreen = ManagerMenuScreen.getInstance(BankControllerManager.this, bankModelManager.getAccountLoggedId());
                bankModelManager.setAccountLoggedId(accountLoggedId);
                configureMenuButton();
            }
            managerLogInScreen.dispose();
        } else {
            managerLogInScreen.setError("CPF ou senha incorretos.");
        }
    }

    private void configureMenuButton() {
        managerMenuScreen.getBtnHistory().addActionListener(_ -> showHistoricalScreen());
        managerMenuScreen.getBtnReviewPending().addActionListener(_ -> showPendingTransactionScreen());
    }

    public void showHistoricalScreen() {
        managerMenuScreen.setVisible(false);
        if (managerHistoryScreen == null) {
            managerHistoryScreen = ManagerHistoryScreen.getInstance(BankControllerManager.this);
        }
        managerHistoryScreen.setVisible(true);
    }

    public void showPendingTransactionScreen() {
        managerMenuScreen.setVisible(false);
        if (managerTransferPendingScreen == null) {
            managerTransferPendingScreen = ManagerTransferPendingScreen.getInstance(BankControllerManager.this);
        }
        managerTransferPendingScreen.setVisible(true);
    }

    public void showReviewTransactionScreen() {
        managerTransferPendingScreen.setVisible(false);
        if (managerReviewPendingScreen == null) {
            managerReviewPendingScreen = ManagerReviewTransferScreen.getInstance(BankControllerManager.this, managerTransferPendingScreen);
        }
        managerReviewPendingScreen.setVisible(true);
    }

    public void showTransactionConfirmedScreen() {
        managerReviewPendingScreen.setVisible(false);
        if (managerTransferConfirmedScreen == null) {
            managerTransferConfirmedScreen = ManagerTransferConfirmedScreen.getInstance(BankControllerManager.this);
        }
        managerTransferConfirmedScreen.setVisible(true);
    }

    public void showTransactionRejectedScreen() {
        managerReviewPendingScreen.setVisible(false);
        if (managerTransferRejectedScreen == null) {
            managerTransferRejectedScreen = ManagerTransferRejectedScreen.getInstance(BankControllerManager.this);
        }
        managerTransferRejectedScreen.setVisible(true);
    }

    public void backToMenuFromHistory() {
        managerHistoryScreen.dispose();
        managerMenuScreen.setVisible(true);
    }

    public void backToPendingTransactionMenu() {
        managerTransferPendingScreen.dispose();
        managerMenuScreen.setVisible(true);
    }

    public void confirmTransaction(int registryId){
        bankModelManager.confirmTransaction(registryId);
    }

    public void rejectTransaction(int registryId){
        bankModelManager.rejectTransaction(registryId);
    }

    public void getDetailsTransaction(int transactionNumber) {
        bankModelManager.getDetailsTransaction(transactionNumber);
    }

    public String getIssuerName() {
        return bankModelManager.getIssuerName();
    }

    public String getIssuerCpf() {
        return bankModelManager.getIssuerCpf();    }

    public String getRecieverName() {
        return bankModelManager.getRecieverName();
    }

    public String getRecieverCpf() {
        return bankModelManager.getRecieverCpf();
    }

    public String getBankName() {
        return bankModelManager.getBankName();
    }

    public String getAccountType() {
        return bankModelManager.getAccountType();
    }

    public double getTransactionValue() {
        return bankModelManager.getTransactionValue();
    }

    public String getConfirmationDate() {
        return bankModelManager.getConfirmationDate();
    }

    public String getManagerName(int idConta){
        return bankModelManager.getManagerName(idConta);
    }

    public ManagerLogInScreen getLogInScreenManager() {
        return managerLogInScreen;
    }

    public List<String> getBankTransactions(){
        return bankModelManager.getBankTransactions();
    }

    public List<String> getBankPendingTransactions(){
        return bankModelManager.getBankPendingTransactions();
    }
}


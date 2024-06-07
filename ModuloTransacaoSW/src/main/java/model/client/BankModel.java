package model.client;

import java.util.List;

public class BankModel {
    private static BankModel instance;
    private final BankTransaction bankTransaction;
    private final BankAccount bankAccount;

    private BankModel() {
        this.bankTransaction = BankTransaction.getInstance();
        this.bankAccount = BankAccount.getInstance();
    }

    public static synchronized BankModel getInstance() {
        if (instance == null) {
            instance = new BankModel();
        }
        return instance;
    }

    public void doTransaction(String selectedTransactionType, String transactionValue, int accountLoggedId, int accountRecieverId) {
        bankTransaction.doTransaction(selectedTransactionType, transactionValue, accountLoggedId, accountRecieverId);
    }

    public int getAccountLoggedId(){
        return bankAccount.getAccountLoggedId();
    }

    public int getAccountRecieverId() {
        return bankAccount.getAccountRecieverId();
    }

    public void setAccountLoggedId(int accountLoggedId) {
        bankAccount.setAccountLoggedId(accountLoggedId);
    }

    public int logInUser(String cpf, String password){
        return bankAccount.logInUser(cpf, password);
    }

    public boolean verifyOwnAccount(int accountNumber){
        return bankAccount.verifyOwnAccount(accountNumber);
    }

    public boolean verifyThirdPartyAccount(int accountNumber, int agencyNumber){
        return bankAccount.verifyThirdPartyAccount(accountNumber, agencyNumber);
    }

    public boolean verifyExternalAccount(int accountNumber, int agencyNumber, int bankCode){
        return bankAccount.verifyExternalAccount(accountNumber, agencyNumber, bankCode);
    }

    public String getClientName(int accountId){
        return bankAccount.getClientName(accountId);
    }

    public double getClientBalance(int accountId){
        return bankAccount.getClientBalance(accountId);
    }

    public String getRecieverName(int accountReceiverId, String selectedTransactionType){
        return bankAccount.getRecieverName(accountReceiverId, selectedTransactionType);
    }

    public String getReceiverCpf(int accountReceiverId, String selectedTransactionType) {
        return bankAccount.getReceiverCpf(accountReceiverId, selectedTransactionType);
    }

    public String getBankName(int accountReceiverId, String selectedTransactionType) {
        return bankAccount.getBankName(accountReceiverId, selectedTransactionType);
    }

    public String getAccountType(int accountReceiverId, String selectedTransactionType) {
        return bankAccount.getAccountType(accountReceiverId, selectedTransactionType);
    }

    public List<String> getUserTransactions(int accountId){
        return bankTransaction.getUserTransactions(accountId);
    }
}
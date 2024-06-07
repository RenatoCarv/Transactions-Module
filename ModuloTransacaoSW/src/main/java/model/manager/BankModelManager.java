package model.manager;

import java.util.List;

public class BankModelManager {
    private static BankModelManager instance;
    private final BankManager bankManager;
    private final ManageTransactions manageTransactions;
    private TransactionPending transactionPending;

    private BankModelManager() {
        this.bankManager = BankManager.getInstance();
        this.manageTransactions = ManageTransactions.getInstance();
    }

    public static synchronized BankModelManager getInstance() {
        if (instance == null) {
            instance = new BankModelManager();
        }
        return instance;
    }

    public int login(String cpf, String password){
        return bankManager.login(cpf, password);
    }

    public int getAccountLoggedId() {
        return bankManager.getAccountLoggedId();
    }

    public void setAccountLoggedId(int idContaLogada){
        bankManager.setAccountLoggedId(idContaLogada);
    }

    public String getManagerName(int idConta){
        return bankManager.getManagerName(idConta);
    }

    public List<String> getBankTransactions(){
        return manageTransactions.getBankTransactions();
    }

    public List<String> getBankPendingTransactions(){
        return manageTransactions.getBankPendingTransactions();
    }

    public void getDetailsTransaction(int numeroTransacao) {
        this.transactionPending = manageTransactions.getDetailsPendingTransactions(numeroTransacao);
    }

    public String getIssuerName() {
        return transactionPending != null ? transactionPending.issuerName() : null;
    }

    public String getIssuerCpf() {
        return transactionPending != null ? transactionPending.issuerCpf() : null;
    }

    public String getRecieverName() {
        return transactionPending != null ? transactionPending.recieverName() : null;
    }

    public String getRecieverCpf() {
        return transactionPending != null ? transactionPending.recieverCpf() : null;
    }

    public String getBankName() {
        return transactionPending != null ? transactionPending.bankName() : null;
    }

    public String getAccountType() {
        return transactionPending != null ? transactionPending.accountType() : null;
    }

    public double getTransactionValue() {
        return transactionPending != null ? transactionPending.transactionValue() : 0;
    }

    public String getConfirmationDate() {
        return transactionPending != null ? transactionPending.confirmationDate() : null;
    }

    public void confirmTransaction(int idRegistro){
        manageTransactions.confirmTransaction(idRegistro);
    }

    public void rejectTransaction(int idRegistro){
        manageTransactions.rejectTransaction(idRegistro);
    }
}
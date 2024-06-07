package model.manager;

public record TransactionPending(String issuerName, String issuerCpf, String recieverName, String recieverCpf,
                                 String bankName, String accountType, double transactionValue,
                                 String confirmationDate) {
}
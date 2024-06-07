package model.client;

import conexao.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankAccount {

    private int accountLoggedId;
    private int accountRecieverId;
    private static BankAccount instance;

    private BankAccount() {
    }

    public static synchronized BankAccount getInstance() {
        if (instance == null) {
            instance = new BankAccount();
        }
        return instance;
    }

    public int logInUser(String cpf, String senha) {
        String sql = "SELECT id_conta FROM Conta WHERE cpf = ? AND senha = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, cpf);
            statement.setString(2, senha);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                accountLoggedId = resultSet.getInt("id_conta");
                return accountLoggedId;
            } else {
                return -1;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao verificar as credenciais: " + e.getMessage());
            return -1;
        }
    }


    public int getAccountLoggedId() {
        return accountLoggedId;
    }

    public void setAccountLoggedId(int accountLoggedId) {
        this.accountLoggedId = accountLoggedId;
    }

    public int getAccountRecieverId() {
        return accountRecieverId;
    }

    public void setAccountRecieverId(int accountRecieverId) {
        this.accountRecieverId = accountRecieverId;
    }


    public String getClientName(int idConta) {
        String sql = "SELECT nome FROM Conta WHERE id_conta = ?";
        String clientName = null;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, idConta);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                clientName = resultSet.getString("nome");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter o nome do cliente: " + e.getMessage());
        }

        return clientName;
    }

    public String getAccountLoggedCpf(int idContaLogada) {
        String sql = "SELECT cpf FROM Conta WHERE id_conta = ?";
        String accountLoggedCpf = null;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, idContaLogada);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                accountLoggedCpf = resultSet.getString("cpf");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o CPF da conta logada: " + e.getMessage());
        }

        return accountLoggedCpf;
    }

    public int getAccountNumberLogged(int idContaLogada) {
        String sql = "SELECT numero_conta FROM Conta WHERE id_conta = ?";
        int accountNumberLogged = -1;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, idContaLogada);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                accountNumberLogged = resultSet.getInt("numero_conta");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o número da conta logada: " + e.getMessage());
        }

        return accountNumberLogged;
    }

    public double getClientBalance(int accountId) {
        String sql = "SELECT saldo FROM Conta WHERE id_conta = ?";
        double accountBalance = 0.0;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, accountId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                accountBalance = resultSet.getDouble("saldo");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter o saldo da conta: " + e.getMessage());
        }

        return accountBalance;
    }

    public String getRecieverName(int idContaDestinatario, String selectedTransactionType) {
        String table = selectedTransactionType.equals("Transferência para terceiros") || selectedTransactionType.equals("Transferência para conta própria") ? "Conta" : "ContaExterna";
        String accountId = selectedTransactionType.equals("Transferência para terceiros") || selectedTransactionType.equals("Transferência para conta própria") ? "id_conta" : "id_contaexterna";
        String sql = "SELECT nome FROM " + table + " WHERE " + accountId + " = ?";
        String clientName = null;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, idContaDestinatario);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                clientName = resultSet.getString("nome");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter o nome do cliente: " + e.getMessage());
        }

        return clientName;
    }

    public String getReceiverCpf(int accountRecieverId, String selectedTransactionType) {
        String table = selectedTransactionType.equals("Transferência para terceiros") || selectedTransactionType.equals("Transferência para conta própria") ? "Conta" : "ContaExterna";
        String accountId = selectedTransactionType.equals("Transferência para terceiros") || selectedTransactionType.equals("Transferência para conta própria") ? "id_conta" : "id_contaexterna";
        String sql = "SELECT cpf FROM " + table + " WHERE " + accountId + " = ?";
        String accountCpf = null;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, accountRecieverId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                accountCpf = resultSet.getString("cpf");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter o nome do cliente: " + e.getMessage());
        }

        return accountCpf;
    }

    public String getBankName(int accountRecieverId, String selectedTransactionType) {
        if (selectedTransactionType.equals("Transferência para terceiros") || selectedTransactionType.equals("Transferência para conta própria")) {
            String sql = "SELECT nome_banco FROM Conta WHERE id_conta = ?";
            return getInternalBankName(accountRecieverId, sql);
        } else {
            return getExternalBankName(accountRecieverId);
        }
    }

    public String getAccountType(int accountRecieverId, String selectedTransactionType) {
        String table = selectedTransactionType.equals("Transferência para terceiros") || selectedTransactionType.equals("Transferência para conta própria") ? "Conta" : "ContaExterna";
        String accountId = selectedTransactionType.equals("Transferência para terceiros") || selectedTransactionType.equals("Transferência para conta própria") ? "id_conta" : "id_contaexterna";
        String sql = "SELECT tipo_conta FROM " + table + " WHERE " + accountId + " = ?";
        String accountType = null;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, accountRecieverId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                accountType = resultSet.getString("tipo_conta");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter o nome do cliente: " + e.getMessage());
        }

        return accountType;
    }

    private String getInternalBankName(int accountRecieverId, String sql) {
        String bankName = null;
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, accountRecieverId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                bankName = resultSet.getString("nome_banco");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter o nome do banco: " + e.getMessage());
        }
        return bankName;
    }

    public String getExternalBankName(int accountRecieverId) {
        String sql = "SELECT bancoexterno_id_bancoexterno FROM ContaExterna WHERE id_contaexterna = ?";
        String externalBankName = null;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement1 = connection.prepareStatement(sql)
        ) {
            statement1.setInt(1, accountRecieverId);

            ResultSet resultSet1 = statement1.executeQuery();

            if (resultSet1.next()) {
                int externalBankId = resultSet1.getInt("bancoexterno_id_bancoexterno");
                String sql2 = "SELECT nome_banco FROM BancoExterno WHERE id_bancoexterno = ?";

                PreparedStatement statement2 = connection.prepareStatement(sql2);
                statement2.setInt(1, externalBankId);

                // Executa a consulta SQL para obter o nome do banco externo
                ResultSet resultSet2 = statement2.executeQuery();

                if (resultSet2.next()) {
                    externalBankName = resultSet2.getString("nome_banco");
                }

                statement2.close();
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter o nome do banco externo: " + e.getMessage());
        }

        return externalBankName;
    }
    public boolean verifyExternalAccount(int accountNumber, int agencyNumber, int bankNumber) {
        Integer externalBankId = searchExternalBankId(bankNumber);
        if (externalBankId == null) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM ContaExterna WHERE numero_conta = ? AND numero_agencia = ? AND bancoexterno_id_bancoexterno = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, accountNumber);
            statement.setInt(2, agencyNumber);
            statement.setInt(3, externalBankId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                int accountRecieverId = getAccountExternalTransactionsId(agencyNumber, accountNumber, bankNumber);
                setAccountRecieverId(accountRecieverId);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar a conta externa: " + e.getMessage());
        }

        return false;
    }

    public Integer searchExternalBankId(int bankNumber) {
        String sql = "SELECT id_bancoexterno FROM BancoExterno WHERE codigo_banco = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, bankNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id_bancoexterno");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar o ID do banco externo: " + e.getMessage());
        }

        return null;
    }

    public int getAccountExternalTransactionsId(int agencyNumber, int numeroConta, int bankNumber) {
        Integer externalBankId = searchExternalBankId(bankNumber);
        if (externalBankId == null) {
            return -1;
        }

        String sql = "SELECT id_contaexterna FROM ContaExterna WHERE numero_agencia = ? AND numero_conta = ? AND bancoexterno_id_bancoexterno = ?";
        int externalAccountId = -1;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, agencyNumber);
            statement.setInt(2, numeroConta);
            statement.setInt(3, externalBankId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                externalAccountId = resultSet.getInt("id_contaexterna");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter o id_conta externo a partir do número da agência, número da conta e código do banco: " + e.getMessage());
        }

        return externalAccountId;
    }


    public boolean verifyOwnAccount(int numeroConta) {
        String sql = "SELECT COUNT(*) FROM Conta WHERE numero_conta = ? AND cpf = ? AND numero_conta != ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, numeroConta);
            statement.setString(2, getAccountLoggedCpf(accountLoggedId));
            statement.setInt(3, getAccountNumberLogged(accountLoggedId));

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                int accountRecieverId = getAccountOwnTransactionId(numeroConta);
                setAccountRecieverId(accountRecieverId);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar a conta própria: " + e.getMessage());
        }

        return false;
    }


    public boolean verifyThirdPartyAccount(int accountNumber, int agencyNumber) {
        String sql = "SELECT COUNT(*) FROM Conta WHERE numero_conta = ? AND numero_agencia = ? AND cpf != ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, accountNumber);
            statement.setInt(2, agencyNumber);
            statement.setString(3, getAccountLoggedCpf(accountLoggedId));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                int accountRecieverId = getAccountTransactionsThirdParty(accountNumber, agencyNumber);
                setAccountRecieverId(accountRecieverId);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar a conta de terceiro: " + e.getMessage());
        }

        return false;
    }

    public int getAccountOwnTransactionId(int accountNumber) {
        String sql = "SELECT id_conta FROM Conta WHERE numero_conta = ?";
        int accountId = -1;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();


            if (resultSet.next()) {
                accountId = resultSet.getInt("id_conta");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter o id_conta a partir do número da conta: " + e.getMessage());
        }

        return accountId;
    }

    public int getAccountTransactionsThirdParty(int accountNumber, int agencyAccount) {
        String sql = "SELECT id_conta FROM Conta WHERE numero_conta = ? AND numero_agencia = ?";
        int accountId = -1;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, accountNumber);
            statement.setInt(2, agencyAccount);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                accountId = resultSet.getInt("id_conta");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter o id_conta a partir do número da agência e número da conta: " + e.getMessage());
        }

        return accountId;
    }
}

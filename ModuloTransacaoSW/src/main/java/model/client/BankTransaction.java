package model.client;

import conexao.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BankTransaction {

    private final BankAccount bankAccount;

    private static BankTransaction instance;

    private BankTransaction() {
        this.bankAccount =  BankAccount.getInstance();
    }

    public static synchronized BankTransaction getInstance() {
        if (instance == null) {
            instance = new BankTransaction();
        }
        return instance;
    }

    public void doTransaction(String selectedTransactionType, String valorTransacao, int accountLoggedId, int accountRecieverId) {
        double value = Double.parseDouble(valorTransacao);
        boolean isPending = value >= 50000;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql;
            int transactionId;

            if (selectedTransactionType.equals("Transferência para terceiros") || selectedTransactionType.equals("Transferência para conta própria")) {
                sql = "INSERT INTO TransacaoInterna (tipo_transacao, valor_transacao, data_confirmacao, conta_id_conta_origem, conta_id_conta_destino) VALUES (?, ?, ?, ?, ?)";
                statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, selectedTransactionType);
                statement.setDouble(2, value);

                LocalDate confirmationDate = LocalDate.now();
                Date sqlDate = Date.valueOf(confirmationDate);
                statement.setDate(3, sqlDate);
                statement.setInt(4, accountLoggedId);
                statement.setInt(5, accountRecieverId);

                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    transactionId = generatedKeys.getInt(1);
                    registerInternalTransaction(transactionId, isPending ? "Pendente" : "Concluida", connection);
                }

                if (!isPending) {
                    double sourceAccountBalance  = bankAccount.getClientBalance(accountLoggedId);
                    double newSourceAccountBalance  = sourceAccountBalance  - value;
                    updateAccountBalance(connection, accountLoggedId, newSourceAccountBalance);

                    double destinyAccountBalance = bankAccount.getClientBalance(accountRecieverId);
                    double newDestinyAccountBalance = destinyAccountBalance + value;
                    updateExternalAccountBalance(connection, accountRecieverId, newDestinyAccountBalance);
                }
            } else {
                sql = "INSERT INTO TransacaoExterna (tipo_transacao, valor_transacao, data_confirmacao, conta_id_conta_origem, contaexterna_id_contaexterna) VALUES (?, ?, ?, ?, ?)";
                statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, selectedTransactionType);
                statement.setDouble(2, value);

                LocalDate confirmationData = LocalDate.now();
                Date dataSql = Date.valueOf(confirmationData);
                statement.setDate(3, dataSql);
                statement.setInt(4, accountLoggedId);
                statement.setInt(5, accountRecieverId);

                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    transactionId = generatedKeys.getInt(1);
                    registerExternalTransaction(transactionId, isPending ? "Pendente" : "Concluida", connection);
                }

                if (!isPending) {
                    double sourceAccountBalance  = bankAccount.getClientBalance(accountLoggedId);
                    double newSourceAccountBalance  = sourceAccountBalance  - value;
                    updateAccountBalance(connection, accountLoggedId, newSourceAccountBalance );

                    double destinyAccountBalance = getExternalAccountBalance(accountRecieverId);
                    double newDestinyAccountBalance = destinyAccountBalance + value;
                    updateExternalAccountBalance(connection, accountRecieverId, newDestinyAccountBalance);
                }
            }

            System.out.println("Transação realizada com sucesso.");

        } catch (SQLException e) {
            System.out.println("Erro ao realizar a transação: " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    private void registerExternalTransaction(int externalTransactionId, String status, Connection connection) throws SQLException {
        String sql = "INSERT INTO RegistroTransacao (status_transacao, transacaointerna_id_transacao, transacaoexterna_id_transacao) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, createInternalTransactionPlaceholder(connection));
            statement.setInt(3, externalTransactionId);
            statement.executeUpdate();
        }
    }

    private void registerInternalTransaction(int internalTransactionId, String status, Connection connection) throws SQLException {
        String sql = "INSERT INTO RegistroTransacao (status_transacao, transacaointerna_id_transacao, transacaoexterna_id_transacao) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, internalTransactionId);
            statement.setInt(3, createExternalTransactionPlaceholder(connection));
            statement.executeUpdate();
        }
    }

    private int createExternalTransactionPlaceholder(Connection connection) throws SQLException {
        String sql = "INSERT INTO TransacaoExterna (tipo_transacao, valor_transacao, data_confirmacao, conta_id_conta_origem, contaexterna_id_contaexterna) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, "placeholder");
            statement.setDouble(2, 0.0);
            statement.setDate(3, null);
            statement.setInt(4, getAccountPlaceholderId(connection));
            statement.setInt(5, getExternalAccountPlaceholderId(connection));

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Falha ao criar placeholder para TransacaoExterna, nenhum ID obtido.");
            }
        }
    }

    private int createInternalTransactionPlaceholder(Connection connection) throws SQLException {
        String sql = "INSERT INTO TransacaoInterna (tipo_transacao, valor_transacao, data_confirmacao, conta_id_conta_origem, conta_id_conta_destino) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, "placeholder"); // Tipo de transação placeholder
            statement.setDouble(2, 0.0); // Valor da transação zero
            statement.setDate(3, null); // Data de confirmação nula
            statement.setInt(4, getAccountPlaceholderId(connection)); // Conta origem válida
            statement.setInt(5, getAccountPlaceholderId(connection)); // Conta destino válida

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Retorna o ID da transação gerado
            } else {
                throw new SQLException("Falha ao criar placeholder para TransacaoInterna, nenhum ID obtido.");
            }
        }
    }

    private int getAccountPlaceholderId(Connection connection) throws SQLException {
        String sql = "SELECT id_conta FROM Conta WHERE tipo_conta = 'placeholder' LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                sql = "INSERT INTO Conta (tipo_conta, saldo) VALUES ('placeholder', 0.0)";
                try (PreparedStatement insertStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    insertStatement.executeUpdate();
                    ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Falha ao criar conta de placeholder, nenhum ID obtido.");
                    }
                }
            }
        }
    }

    private int getExternalAccountPlaceholderId(Connection connection) throws SQLException {
        String sql = "SELECT id_contaexterna FROM ContaExterna WHERE tipo_conta = 'placeholder' LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                sql = "INSERT INTO ContaExterna (tipo_conta, saldo, bancoexterno_id_bancoexterno) VALUES ('placeholder', 0.0, 1)";
                try (PreparedStatement insertStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    insertStatement.executeUpdate();
                    ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Falha ao criar conta externa de placeholder, nenhum ID obtido.");
                    }
                }
            }
        }
    }

    private void updateAccountBalance(Connection connection, int idConta, double novoSaldo) throws SQLException {
        String sql = "UPDATE Conta SET saldo = ? WHERE id_conta = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setDouble(1, novoSaldo);
        statement.setInt(2, idConta);
        statement.executeUpdate();
        statement.close();
    }

    private void updateExternalAccountBalance(Connection connection, int idConta, double novoSaldo) throws SQLException {
        String sql = "UPDATE ContaExterna SET saldo = ? WHERE id_contaexterna = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setDouble(1, novoSaldo);
        statement.setInt(2, idConta);
        statement.executeUpdate();
        statement.close();
    }


    public double getExternalAccountBalance(int idContaExterna) {
        double balance = 0.0;
        String sql = "SELECT saldo FROM ContaExterna WHERE id_contaexterna = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idContaExterna);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                balance = resultSet.getDouble("saldo");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter saldo da conta externa: " + e.getMessage());
        }

        return balance;
    }

    public List<String> getUserTransactions(int accountId) {
        List<String> transactions = new ArrayList<>();
        String sql = "SELECT RI.status_transacao AS status_interna, RE.status_transacao AS status_externa, " +
                "TI.tipo_transacao AS tipo_interna, TE.tipo_transacao AS tipo_externa, " +
                "TI.valor_transacao AS valor_interna, TE.valor_transacao AS valor_externa, " +
                "TI.data_confirmacao AS data_interna, TE.data_confirmacao AS data_externa " +
                "FROM RegistroTransacao RT " +
                "LEFT JOIN TransacaoInterna TI ON RT.transacaointerna_id_transacao = TI.id_transacao " +
                "LEFT JOIN TransacaoExterna TE ON RT.transacaoexterna_id_transacao = TE.id_transacao " +
                "LEFT JOIN RegistroTransacao RI ON TI.id_transacao = RI.transacaointerna_id_transacao " +
                "LEFT JOIN RegistroTransacao RE ON TE.id_transacao = RE.transacaoexterna_id_transacao " +
                "WHERE (TI.conta_id_conta_origem = ? OR TE.conta_id_conta_origem = ?) " +
                "AND (TI.tipo_transacao <> 'placeholder' OR TE.tipo_transacao <> 'placeholder')";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            statement.setInt(2, accountId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String internalStatus = resultSet.getString("status_interna");
                String externalStatus = resultSet.getString("status_externa");
                String internalType = resultSet.getString("tipo_interna");
                String externalType = resultSet.getString("tipo_externa");
                double internalValue = resultSet.getDouble("valor_interna");
                double externalValue = resultSet.getDouble("valor_externa");
                String internalDate = resultSet.getString("data_interna");
                String externalDate = resultSet.getString("data_externa");

                if (!internalType.equals("placeholder")) {
                    transactions.add("Status: " + internalStatus + "\nTipo: " + internalType + "\nValor: R$ " + internalValue + "\nData: " + internalDate);
                }
                if (!externalType.equals("placeholder")) {
                    transactions.add("Status: " + externalStatus + "\nTipo: " + externalType + "\nValor: R$ " + externalValue + "\nData: " + externalDate);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter transações do usuário: " + e.getMessage());
        }

        return transactions;
    }
}

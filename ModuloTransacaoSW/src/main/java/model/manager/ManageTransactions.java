package model.manager;

import conexao.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManageTransactions {

    private static ManageTransactions instance;

    private ManageTransactions() {
    }

    public static synchronized ManageTransactions getInstance() {
        if (instance == null) {
            instance = new ManageTransactions();
        }
        return instance;
    }

    public List<String> getBankTransactions() {
        List<String> transacoes = new ArrayList<>();
        String sql = "SELECT RI.status_transacao AS status_interna, RE.status_transacao AS status_externa, " +
                "TI.tipo_transacao AS tipo_interna, TE.tipo_transacao AS tipo_externa, " +
                "TI.valor_transacao AS valor_interna, TE.valor_transacao AS valor_externa, " +
                "TI.data_confirmacao AS data_interna, TE.data_confirmacao AS data_externa, " +
                "CI.nome AS nome_emitente_interna, CE.nome AS nome_emitente_externa " +
                "FROM RegistroTransacao RT " +
                "LEFT JOIN TransacaoInterna TI ON RT.transacaointerna_id_transacao = TI.id_transacao " +
                "LEFT JOIN TransacaoExterna TE ON RT.transacaoexterna_id_transacao = TE.id_transacao " +
                "LEFT JOIN RegistroTransacao RI ON TI.id_transacao = RI.transacaointerna_id_transacao " +
                "LEFT JOIN RegistroTransacao RE ON TE.id_transacao = RE.transacaoexterna_id_transacao " +
                "LEFT JOIN Conta CI ON TI.conta_id_conta_origem = CI.id_conta " +
                "LEFT JOIN Conta CE ON TE.conta_id_conta_origem = CE.id_conta " +
                "AND (TI.tipo_transacao <> 'placeholder' OR TE.tipo_transacao <> 'placeholder')";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

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
                String internalIssuerName = resultSet.getString("nome_emitente_interna");
                String externalIssuerName = resultSet.getString("nome_emitente_externa");

                if (!internalType.equals("placeholder")) {
                    transacoes.add("Emitente: " + internalIssuerName + "\nValor: R$ " + internalValue + "\nTipo: " + internalType + "\nStatus: " + internalStatus + "\nData: " + internalDate);
                }
                if (!externalType.equals("placeholder")) {
                    transacoes.add("Emitente: " + externalIssuerName + "\nValor: R$ " + externalValue + "\nTipo: " + externalType + "\nStatus: " + externalStatus + "\nData: " + externalDate);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter transações do usuário: " + e.getMessage());
        }

        return transacoes;
    }

    public List<String> getBankPendingTransactions() {
        List<String> transacoes = new ArrayList<>();
        String sql = "SELECT RT.id_registro, RI.status_transacao AS status_interna, RE.status_transacao AS status_externa, " +
                "TI.tipo_transacao AS tipo_interna, TE.tipo_transacao AS tipo_externa, " +
                "TI.valor_transacao AS valor_interna, TE.valor_transacao AS valor_externa, " +
                "TI.data_confirmacao AS data_interna, TE.data_confirmacao AS data_externa, " +
                "CI.nome AS nome_emitente_interna, CE.nome AS nome_emitente_externa " +
                "FROM RegistroTransacao RT " +
                "LEFT JOIN TransacaoInterna TI ON RT.transacaointerna_id_transacao = TI.id_transacao " +
                "LEFT JOIN TransacaoExterna TE ON RT.transacaoexterna_id_transacao = TE.id_transacao " +
                "LEFT JOIN RegistroTransacao RI ON TI.id_transacao = RI.transacaointerna_id_transacao " +
                "LEFT JOIN RegistroTransacao RE ON TE.id_transacao = RE.transacaoexterna_id_transacao " +
                "LEFT JOIN Conta CI ON TI.conta_id_conta_origem = CI.id_conta " +
                "LEFT JOIN Conta CE ON TE.conta_id_conta_origem = CE.id_conta " +
                "WHERE RT.status_transacao = 'Pendente' " +
                "AND (TI.tipo_transacao <> 'placeholder' OR TE.tipo_transacao <> 'placeholder')";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String registerId = resultSet.getString("id_registro");
                String internalStatus = resultSet.getString("status_interna");
                String externalStatus = resultSet.getString("status_externa");
                String internalType = resultSet.getString("tipo_interna");
                String externalType = resultSet.getString("tipo_externa");
                double internalValue = resultSet.getDouble("valor_interna");
                double externalValue = resultSet.getDouble("valor_externa");
                String internalDate = resultSet.getString("data_interna");
                String externalDate = resultSet.getString("data_externa");
                String internalIssuerName = resultSet.getString("nome_emitente_interna");
                String externalIssuerName = resultSet.getString("nome_emitente_externa");

                if (!internalType.equals("placeholder")) {
                    transacoes.add("ID: " + registerId + "\nEmitente: " + internalIssuerName + "\nValor: R$ " + internalValue + "\nTipo: " + internalType + "\nStatus: " + internalStatus + "\nData: " + internalDate);
                }
                if (!externalType.equals("placeholder")) {
                    transacoes.add("ID: " + registerId + "\nEmitente: " + externalIssuerName + "\nValor: R$ " + externalValue + "\nTipo: " + externalType + "\nStatus: " + externalStatus + "\nData: " + externalDate);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter transações do usuário: " + e.getMessage());
        }

        return transacoes;
    }

    public TransactionPending getDetailsPendingTransactions(int transactionNumber) {
        String sql = "SELECT RT.id_registro, TI.id_transacao AS id_transacao_interna, TE.id_transacao AS id_transacao_externa, " +
                "CI.nome AS nome_emitente_interna, CE.nome AS nome_emitente_externa, " +
                "CI.cpf AS cpf_emitente_interna, CE.cpf AS cpf_emitente_externa, " +
                "TI.valor_transacao AS valor_interna, TE.valor_transacao AS valor_externa, " +
                "TI.data_confirmacao AS data_interna, TE.data_confirmacao AS data_externa, " +
                "CD.nome AS nome_destinatario_interna, CD.cpf AS cpf_destinatario_interna, " +
                "CD.nome_banco AS nome_banco_interna, CD.tipo_conta AS tipo_conta_interna, " +
                "CEE.nome AS nome_destinatario_externa, CEE.cpf AS cpf_destinatario_externa, " +
                "CEB.nome_banco AS nome_banco_externa, CEE.tipo_conta AS tipo_conta_externa " +
                "FROM RegistroTransacao RT " +
                "LEFT JOIN TransacaoInterna TI ON RT.transacaointerna_id_transacao = TI.id_transacao " +
                "LEFT JOIN TransacaoExterna TE ON RT.transacaoexterna_id_transacao = TE.id_transacao " +
                "LEFT JOIN Conta CI ON TI.conta_id_conta_origem = CI.id_conta " +
                "LEFT JOIN Conta CE ON TE.conta_id_conta_origem = CE.id_conta " +
                "LEFT JOIN Conta CD ON TI.conta_id_conta_destino = CD.id_conta " +
                "LEFT JOIN ContaExterna CEE ON TE.contaexterna_id_contaexterna = CEE.id_contaexterna " +
                "LEFT JOIN BancoExterno CEB ON CEE.bancoexterno_id_bancoexterno = CEB.id_bancoexterno " +
                "WHERE RT.id_registro = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, transactionNumber);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String issuerName = resultSet.getString("nome_emitente_interna") != null ?
                        resultSet.getString("nome_emitente_interna") : resultSet.getString("nome_emitente_externa");
                String issuerCpf = resultSet.getString("cpf_emitente_interna") != null ?
                        resultSet.getString("cpf_emitente_interna") : resultSet.getString("cpf_emitente_externa");
                String recieverName = resultSet.getString("nome_destinatario_interna") != null ?
                        resultSet.getString("nome_destinatario_interna") : resultSet.getString("nome_destinatario_externa");
                String recieverCpf = resultSet.getString("cpf_destinatario_interna") != null ?
                        resultSet.getString("cpf_destinatario_interna") : resultSet.getString("cpf_destinatario_externa");
                String bankName = resultSet.getString("nome_banco_interna") != null ?
                        resultSet.getString("nome_banco_interna") : resultSet.getString("nome_banco_externa");
                String accountType = resultSet.getString("tipo_conta_interna") != null ?
                        resultSet.getString("tipo_conta_interna") : resultSet.getString("tipo_conta_externa");
                double transactionValue = resultSet.getDouble("valor_interna") != 0 ?
                        resultSet.getDouble("valor_interna") : resultSet.getDouble("valor_externa");
                String confirmationDate = resultSet.getString("data_interna") != null ?
                        resultSet.getString("data_interna") : resultSet.getString("data_externa");

                return new TransactionPending(issuerName, issuerCpf, recieverName, recieverCpf, bankName, accountType, transactionValue, confirmationDate);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter detalhes da transação pendente: " + e.getMessage());
        }

        return null;
    }

    public void confirmTransaction(int idRegistro) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();

            String sql = "SELECT RT.id_registro, RT.status_transacao, " +
                    "TI.valor_transacao AS valor_interna, TE.valor_transacao AS valor_externa, " +
                    "TI.conta_id_conta_origem AS id_conta_origem_interna, TE.conta_id_conta_origem AS id_conta_origem_externa, " +
                    "TI.conta_id_conta_destino AS id_conta_destino_interna, TE.contaexterna_id_contaexterna AS id_conta_destino_externa " +
                    "FROM RegistroTransacao RT " +
                    "LEFT JOIN TransacaoInterna TI ON RT.transacaointerna_id_transacao = TI.id_transacao " +
                    "LEFT JOIN TransacaoExterna TE ON RT.transacaoexterna_id_transacao = TE.id_transacao " +
                    "WHERE RT.id_registro = ? AND RT.status_transacao = 'Pendente'";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, idRegistro);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double internalTransactionValue = resultSet.getDouble("valor_interna");
                double externalTransactionValue = resultSet.getDouble("valor_externa");
                int accountSourceInternalId = resultSet.getInt("id_conta_origem_interna");
                int accountSourceExternalId = resultSet.getInt("id_conta_origem_externa");
                int accountDestinyInternalId = resultSet.getInt("id_conta_destino_interna");
                int accountDestinyExternalId = resultSet.getInt("id_conta_destino_externa");

                String updateStatusSql = "UPDATE RegistroTransacao SET status_transacao = 'Concluida' WHERE id_registro = ?";
                try (PreparedStatement updateStatusStmt = connection.prepareStatement(updateStatusSql)) {
                    updateStatusStmt.setInt(1, idRegistro);
                    updateStatusStmt.executeUpdate();
                }

                if (internalTransactionValue != 0.0) {
                    updateAccountBalance(connection, accountSourceInternalId, -internalTransactionValue);
                    updateAccountBalance(connection, accountDestinyInternalId, internalTransactionValue);
                } else {
                    updateAccountBalance(connection, accountSourceExternalId, -externalTransactionValue);
                    updateExternalAccountBalance(connection, accountDestinyExternalId, externalTransactionValue);
                }

                System.out.println("Transação confirmada com sucesso.");
            } else {
                System.out.println("Transação não encontrada ou já foi concluída.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao confirmar a transação: " + e.getMessage());
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

    private void updateAccountBalance(Connection connection, int accountId, double value) throws SQLException {
        String sql = "UPDATE Conta SET saldo = saldo + ? WHERE id_conta = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, value);
            statement.setInt(2, accountId);
            statement.executeUpdate();
        }
    }

    private void updateExternalAccountBalance(Connection connection, int accountExternalId, double value) throws SQLException {
        String sql = "UPDATE ContaExterna SET saldo = saldo + ? WHERE id_contaexterna = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, value);
            statement.setInt(2, accountExternalId);
            statement.executeUpdate();
        }
    }

    public void rejectTransaction(int registryId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();

            String sql = "SELECT RT.id_registro, RT.status_transacao " +
                    "FROM RegistroTransacao RT " +
                    "WHERE RT.id_registro = ? AND RT.status_transacao = 'Pendente'";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, registryId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String updateStatusSql = "UPDATE RegistroTransacao SET status_transacao = 'Rejeitada' WHERE id_registro = ?";
                try (PreparedStatement updateStatusStmt = connection.prepareStatement(updateStatusSql)) {
                    updateStatusStmt.setInt(1, registryId);
                    updateStatusStmt.executeUpdate();
                }

                System.out.println("Transação rejeitada com sucesso.");
            } else {
                System.out.println("Transação não encontrada ou já foi processada.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao rejeitar a transação: " + e.getMessage());
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



}

package model.manager;

import conexao.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankManager {

    private int accountLoggedId;
    private static BankManager instance;

    private BankManager() {
    }

    public static synchronized BankManager getInstance() {
        if (instance == null) {
            instance = new BankManager();
        }
        return instance;
    }

    public int login(String cpf, String senha) {
        String sql = "SELECT id_gerente FROM Gerente WHERE cpf = ? AND senha = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, cpf);
            statement.setString(2, senha);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                accountLoggedId = resultSet.getInt("id_gerente");
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

    public String getManagerName(int accountId) {
        String sql = "SELECT nome FROM Gerente WHERE id_gerente = ?";
        String name = null;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, accountId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                name = resultSet.getString("nome");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter o nome do cliente: " + e.getMessage());
        }
        return name;
    }

}

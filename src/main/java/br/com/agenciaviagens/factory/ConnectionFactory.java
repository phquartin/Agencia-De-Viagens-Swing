package br.com.agenciaviagens.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // URL de conexão com o banco de dados MySQL
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/agencia_viagens?useTimezone=true&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    /**
     * Cria e retorna uma conexão com o banco de dados.
     * @return Uma conexão com o banco de dados.
     * @throws SQLException Se ocorrer um erro ao tentar se conectar.
     */
    public static Connection createConnectionToMySQL() throws SQLException {
        // O Class.forName é necessário em algumas configurações para carregar o driver do MySQL
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Lidar com o erro de driver não encontrado
            throw new SQLException("Driver do MySQL não encontrado", e);
        }

        // Cria a conexão com o banco de dados

        return DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
    }

    // Método principal apenas para testar a conexão
    public static void main(String[] args) {
        try {
            Connection con = createConnectionToMySQL();
            if (con != null) {
                System.out.println("Conexão obtida com sucesso!");
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("Falha ao obter a conexão: " + e.getMessage());
        }
    }
}
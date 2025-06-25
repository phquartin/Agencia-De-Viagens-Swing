package br.com.agenciaviagens.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // URL de conexão para o SERVIDOR MySQL
    private static final String ROOT_DATABASE_URL = "jdbc:mysql://localhost:3306/?useTimezone=true&serverTimezone=UTC&allowMultiQueries=true";

    // URL de conexão para o BANCO DE DADOS
    private static final String APP_DATABASE_URL = "jdbc:mysql://localhost:3306/agencia_viagens?useTimezone=true&serverTimezone=UTC&allowMultiQueries=true";

    private static final String USERNAME = "root";
    private static final String PASSWORD = "root"; // todo: !!!!!!!! SUA SENHA ROOT !!!!!!!!!!
    /**
     * Cria uma conexão com o servidor MySQL (sem banco de dados específico).
     * Usado apenas para criar o schema inicial.
     * @return Uma conexão com o servidor MySQL.
     */
    public static Connection createRootConnection() throws SQLException {
        return DriverManager.getConnection(ROOT_DATABASE_URL, USERNAME, PASSWORD);
    }

    /**
     * Cria uma conexão com o banco da aplicação.
     * Usado para todas as operações da aplicação após o banco ser criado.
     * @return Uma conexão com o banco 'agencia_viagens'.
     */
    public static Connection createConnectionToMySQL() throws SQLException {
        return DriverManager.getConnection(APP_DATABASE_URL, USERNAME, PASSWORD);
    }
}
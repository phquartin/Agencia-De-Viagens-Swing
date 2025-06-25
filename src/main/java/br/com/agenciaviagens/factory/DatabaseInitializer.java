package br.com.agenciaviagens.factory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = ConnectionFactory.createConnectionToMySQL()) {
            System.out.println("Conexão estabelecida. Iniciando a execução dos scripts SQL...");

            // Executa o script de criação do schema
            System.out.println("Executando schema.sql...");
            runScript(conn, "schema.sql");
            System.out.println("schema.sql executado com sucesso.");

            // Executa o script de povoamento
            System.out.println("Executando povoamento.sql...");
            runScript(conn, "povoamento.sql");
            System.out.println("povoamento.sql executado com sucesso.");

            System.out.println("Banco de dados inicializado e populado!");

        } catch (Exception e) {
            System.err.println("Ocorreu um erro ao inicializar o banco de dados.");
            e.printStackTrace();
        }
    }

    private static void runScript(Connection conn, String resourceName) throws Exception {
        // Usando ClassLoader para encontrar o arquivo no classpath (dentro de src/main/resources)
        InputStream inputStream = DatabaseInitializer.class.getClassLoader().getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new Exception("Não foi possível encontrar o script: " + resourceName);
        }

        // Lendo o conteúdo do arquivo SQL
        String scriptContent = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        // Separando os comandos SQL pelo ponto e vírgula
        String[] sqlCommands = scriptContent.split(";\\s*");

        try (Statement stmt = conn.createStatement()) {
            for (String command : sqlCommands) {
                // Ignora comandos vazios ou comentários
                if (command.trim().isEmpty() || command.trim().startsWith("--")) {
                    continue;
                }
                stmt.execute(command);
            }
        }
    }
}
package br.com.agenciaviagens.factory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {

    // Método público que orquestra toda a inicialização.
    public static void initialize() {
        System.out.println("Iniciando processo de inicialização do banco de dados...");

        try {
            // Passo 1: Garantir que o schema exista.
            try (Connection conn = ConnectionFactory.createRootConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute("DROP SCHEMA IF EXISTS `agencia_viagens`");
                stmt.execute("CREATE SCHEMA `agencia_viagens` DEFAULT CHARACTER SET utf8mb4");
                System.out.println("Schema 'agencia_viagens' criado com sucesso.");
            }

            // Passo 2: Conectar ao banco recém-criado e CRIAR AS TABELAS explicitamente.
            try (Connection conn = ConnectionFactory.createConnectionToMySQL();
                 Statement stmt = conn.createStatement()) {

                System.out.println("Conexão com 'agencia_viagens' estabelecida. Criando tabelas...");
                createTables(stmt); // Chamando o método auxiliar para criar tabelas
                System.out.println("Todas as tabelas foram criadas com sucesso.");
            }

            // Passo 3: Popular o banco de dados usando o script de povoamento.
            try (Connection conn = ConnectionFactory.createConnectionToMySQL()) {
                System.out.println("Populando o banco de dados...");
                runPovoamentoScript(conn);
                System.out.println("Banco de dados populado com sucesso.");
            }

        } catch (Exception e) {
            System.err.println("### OCORREU UM ERRO GERAL DURANTE A INICIALIZAÇÃO ###");
            e.printStackTrace();
            throw new RuntimeException("Falha ao inicializar o banco de dados.", e);
        }
    }

    // Método privado para conter os comandos de criação de tabela.
    private static void createTables(Statement stmt) throws Exception {
        String sqlCreateTableClientes = "CREATE TABLE `clientes` (`id_cliente` INT NOT NULL AUTO_INCREMENT, `nome` VARCHAR(255) NOT NULL, `email` VARCHAR(255) NOT NULL UNIQUE, `telefone` VARCHAR(20), `endereco` VARCHAR(255), `tipo_cliente` ENUM('NACIONAL', 'ESTRANGEIRO') NOT NULL, `cpf` VARCHAR(14) UNIQUE, `passaporte` VARCHAR(20) UNIQUE, PRIMARY KEY (`id_cliente`))";
        String sqlCreateTablePacotes = "CREATE TABLE `pacotes` (`id_pacote` INT NOT NULL AUTO_INCREMENT, `nome_pacote` VARCHAR(255) NOT NULL, `destino` VARCHAR(255) NOT NULL, `data_partida` DATE, `data_retorno` DATE, `preco` DECIMAL(10, 2) NOT NULL, PRIMARY KEY (`id_pacote`))";
        String sqlCreateTableServicos = "CREATE TABLE `servicos_adicionais` (`id_servico` INT NOT NULL AUTO_INCREMENT, `nome_servico` VARCHAR(255) NOT NULL, `descricao` TEXT, `preco` DECIMAL(10, 2) NOT NULL, PRIMARY KEY (`id_servico`))";
        String sqlCreateTableContratacoes = "CREATE TABLE `contratacoes` (`id_contratacao` INT NOT NULL AUTO_INCREMENT, `id_cliente` INT NOT NULL, `id_pacote` INT NOT NULL, `data_contratacao` DATETIME NOT NULL, PRIMARY KEY (`id_contratacao`), FOREIGN KEY (`id_cliente`) REFERENCES `clientes`(`id_cliente`), FOREIGN KEY (`id_pacote`) REFERENCES `pacotes`(`id_pacote`) ON DELETE RESTRICT)";
        String sqlCreateTableContratacaoServicos = "CREATE TABLE `contratacao_servicos` (`id_contratacao` INT NOT NULL, `id_servico` INT NOT NULL, PRIMARY KEY (`id_contratacao`, `id_servico`), FOREIGN KEY (`id_contratacao`) REFERENCES `contratacoes`(`id_contratacao`) ON DELETE CASCADE, FOREIGN KEY (`id_servico`) REFERENCES `servicos_adicionais`(`id_servico`) ON DELETE CASCADE)";

        stmt.execute(sqlCreateTableClientes);
        stmt.execute(sqlCreateTablePacotes);
        stmt.execute(sqlCreateTableServicos);
        stmt.execute(sqlCreateTableContratacoes);
        stmt.execute(sqlCreateTableContratacaoServicos);
    }

    // Método auxiliar para rodar apenas o script de povoamento.
    private static void runPovoamentoScript(Connection conn) throws Exception {
        InputStream inputStream = DatabaseInitializer.class.getClassLoader().getResourceAsStream("povoamento.sql");
        if (inputStream == null) {
            throw new Exception("Não foi possível encontrar o script: povoamento.sql");
        }

        String scriptContent = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        String[] sqlCommands = scriptContent.split(";\\s*");

        try (Statement stmt = conn.createStatement()) {
            for (String command : sqlCommands) {
                if (command.trim().isEmpty() || command.trim().startsWith("--")) {
                    continue;
                }
                stmt.execute(command);
            }
        }
    }
}
package br.com.agenciaviagens.factory;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseInitializer {

    /**
     * Método público que orquestra toda a inicialização do banco de dados.
     */
    public static void initialize() {
        System.out.println("Iniciando processo de inicialização do banco de dados...");

        try {
            // Passo 1: Garantir que o schema (banco de dados) exista.
            try (Connection conn = ConnectionFactory.createRootConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute("DROP SCHEMA IF EXISTS `agencia_viagens`");
                stmt.execute("CREATE SCHEMA `agencia_viagens` DEFAULT CHARACTER SET utf8mb4");
                System.out.println("Schema 'agencia_viagens' criado com sucesso.");
            }

            // Passo 2 e 3: Conectar ao banco recém-criado para criar tabelas e popular dados.
            // Usamos um único bloco try-with-resources para a conexão, que será usada para ambos os passos.
            try (Connection conn = ConnectionFactory.createConnectionToMySQL();
                 Statement stmt = conn.createStatement()) {

                System.out.println("Conexão com 'agencia_viagens' estabelecida.");

                // Passo 2: Criar as tabelas.
                System.out.println("Criando tabelas...");
                createTables(stmt);
                System.out.println("Todas as tabelas foram criadas com sucesso.");

                // Passo 3: Popular o banco de dados.
                System.out.println("Populando o banco de dados...");
                populateDatabase(stmt);
                System.out.println("Banco de dados populado com sucesso.");
            }

        } catch (Exception e) {
            System.err.println("### OCORREU UM ERRO GERAL DURANTE A INICIALIZAÇÃO ###");
            e.printStackTrace();
            // Lança uma exceção para parar a aplicação se o banco não puder ser inicializado.
            throw new RuntimeException("Falha ao inicializar o banco de dados.", e);
        }
    }

    /**
     * Contém os comandos SQL para criar todas as tabelas da aplicação.
     * @param stmt O Statement conectado ao banco 'agencia_viagens'.
     */
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

    /**
     * Contém os comandos SQL para limpar e popular o banco com dados de teste.
     * @param stmt O Statement conectado ao banco 'agencia_viagens'.
     */
    private static void populateDatabase(Statement stmt) throws Exception {
        List<String> commands = new ArrayList<>();

        commands.add("DELETE FROM `contratacao_servicos`");
        commands.add("DELETE FROM `contratacoes`");
        commands.add("DELETE FROM `servicos_adicionais`");
        commands.add("DELETE FROM `pacotes`");
        commands.add("DELETE FROM `clientes`");

        commands.add("INSERT INTO `clientes` (nome, email, telefone, endereco, tipo_cliente, cpf, passaporte) VALUES ('Joao Silva', 'joao.silva@email.com', '(11) 98765-4321', 'Rua das Flores, 123', 'NACIONAL', '111.222.333-44', NULL)");
        commands.add("INSERT INTO `clientes` (nome, email, telefone, endereco, tipo_cliente, cpf, passaporte) VALUES ('Maria Oliveira', 'maria.oliveira@email.com', '(21) 91234-5678', 'Avenida Principal, 456', 'NACIONAL', '555.666.777-88', NULL)");
        commands.add("INSERT INTO `clientes` (nome, email, telefone, endereco, tipo_cliente, cpf, passaporte) VALUES ('John Doe', 'john.doe@email.com', '(00) 90123-0192', '123 Flower Street', 'ESTRANGEIRO', NULL, 'A1B2C3D4')");

        commands.add("INSERT INTO `pacotes` (nome_pacote, destino, data_partida, data_retorno, preco) VALUES ('Praias do Nordeste', 'Salvador, BA', '2025-10-20', '2025-10-30', 2500.00)");
        commands.add("INSERT INTO `pacotes` (nome_pacote, destino, data_partida, data_retorno, preco) VALUES ('Serra Gaúcha', 'Gramado, RS', '2025-07-15', '2025-07-22', 1800.50)");
        commands.add("INSERT INTO `pacotes` (nome_pacote, destino, data_partida, data_retorno, preco) VALUES ('Tour Europeu', 'Paris, Roma, Lisboa', '2025-09-01', '2025-09-15', 7500.75)");

        commands.add("INSERT INTO `servicos_adicionais` (nome_servico, descricao, preco) VALUES ('Seguro Viagem Completo', 'Cobertura total para despesas médicas e bagagem.', 150.00)");
        commands.add("INSERT INTO `servicos_adicionais` (nome_servico, descricao, preco) VALUES ('Aluguel de Carro', 'Carro econômico com ar condicionado.', 450.00)");
        commands.add("INSERT INTO `servicos_adicionais` (nome_servico, descricao, preco) VALUES ('Passeio de Barco', 'Passeio turístico pelas principais ilhas.', 200.00)");

        commands.add("INSERT INTO `contratacoes` (id_cliente, id_pacote, data_contratacao) VALUES ((SELECT id_cliente FROM clientes WHERE email = 'joao.silva@email.com'), (SELECT id_pacote FROM pacotes WHERE nome_pacote = 'Praias do Nordeste'), NOW())");
        commands.add("INSERT INTO `contratacoes` (id_cliente, id_pacote, data_contratacao) VALUES ((SELECT id_cliente FROM clientes WHERE email = 'john.doe@email.com'), (SELECT id_pacote FROM pacotes WHERE nome_pacote = 'Tour Europeu'), NOW())");

        commands.add("INSERT INTO `contratacao_servicos` (id_contratacao, id_servico) VALUES ((SELECT id_contratacao FROM contratacoes WHERE id_cliente = (SELECT id_cliente FROM clientes WHERE email = 'joao.silva@email.com')), (SELECT id_servico FROM servicos_adicionais WHERE nome_servico = 'Seguro Viagem Completo'))");

        for (String command : commands) {
            stmt.execute(command);
        }
    }
}
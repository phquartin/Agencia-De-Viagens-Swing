
-- Tabela: clientes
-- Armazena os dados dos clientes da agência.
CREATE TABLE `clientes` (
                            `id_cliente` INT NOT NULL AUTO_INCREMENT,
                            `nome` VARCHAR(255) NOT NULL,
                            `email` VARCHAR(255) NOT NULL UNIQUE,
                            `telefone` VARCHAR(20) ,
                            `endereco` VARCHAR(255),
                            `tipo_cliente` ENUM('NACIONAL', 'ESTRANGEIRO') NOT NULL,
                            `cpf` VARCHAR(14) UNIQUE, -- Formato XXX.XXX.XXX-XX
                            `passaporte` VARCHAR(20) UNIQUE,
                            PRIMARY KEY (`id_cliente`)
);

-- Tabela: pacotes
-- Armazena os pacotes de viagem disponíveis.
CREATE TABLE `pacotes` (
                           `id_pacote` INT NOT NULL AUTO_INCREMENT,
                           `nome_pacote` VARCHAR(255) NOT NULL,
                           `destino` VARCHAR(255) NOT NULL,
                           `data_partida` DATE,
                           `data_retorno` DATE,
                           `preco` DECIMAL(10, 2) NOT NULL,
                           PRIMARY KEY (`id_pacote`)
);

-- Tabela: servicos_adicionais
-- Armazena serviços extras que podem ser adicionados a uma contratação.
CREATE TABLE `servicos_adicionais` (
                                       `id_servico` INT NOT NULL AUTO_INCREMENT,
                                       `nome_servico` VARCHAR(255) NOT NULL,
                                       `descricao` TEXT,
                                       `preco` DECIMAL(10, 2) NOT NULL,
                                       PRIMARY KEY (`id_servico`)
);

-- Tabela Associativa: contratacoes
-- Liga clientes e pacotes, representando a contratação de um pacote por um cliente.
CREATE TABLE `contratacoes` (
                                `id_contratacao` INT NOT NULL AUTO_INCREMENT,
                                `id_cliente` INT NOT NULL,
                                `id_pacote` INT NOT NULL,
                                `data_contratacao` DATETIME NOT NULL,
                                PRIMARY KEY (`id_contratacao`),
                                FOREIGN KEY (`id_cliente`) REFERENCES `clientes`(`id_cliente`),
    -- A cláusula ON DELETE RESTRICT impede que um pacote seja deletado
    -- se houver clientes associados a ele, cumprindo uma regra de negócio.
                                FOREIGN KEY (`id_pacote`) REFERENCES `pacotes`(`id_pacote`) ON DELETE RESTRICT
);

-- Tabela Associativa: contratacao_servicos
-- Liga uma contratação específica aos serviços adicionais contratados.
CREATE TABLE `contratacao_servicos` (
                                        `id_contratacao` INT NOT NULL,
                                        `id_servico` INT NOT NULL,
                                        PRIMARY KEY (`id_contratacao`, `id_servico`),
                                        FOREIGN KEY (`id_contratacao`) REFERENCES `contratacoes`(`id_contratacao`) ON DELETE CASCADE,
                                        FOREIGN KEY (`id_servico`) REFERENCES `servicos_adicionais`(`id_servico`) ON DELETE CASCADE
);
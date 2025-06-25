-- Usa o banco de dados 'agencia_viagens'
USE `agencia_viagens`;

-- Limpa as tabelas antes de inserir novos dados para evitar duplicatas em re-execuções.
-- A ordem é importante para respeitar as chaves estrangeiras.
DELETE FROM `contratacao_servicos`;
DELETE FROM `contratacoes`;
DELETE FROM `servicos_adicionais`;
DELETE FROM `pacotes`;
DELETE FROM `clientes`;

-- Inserindo Clientes de Exemplo
INSERT INTO `clientes` (nome, email, telefone, endereco, tipo_cliente, cpf, passaporte) VALUES
                                                                                            ('João Silva', 'joao.silva@email.com', '11987654321', 'Rua das Flores, 123', 'NACIONAL', '111.222.333-44', NULL),
                                                                                            ('Maria Oliveira', 'maria.oliveira@email.com', '21912345678', 'Avenida Principal, 456', 'NACIONAL', '555.666.777-88', NULL),
                                                                                            ('John Doe', 'john.doe@email.com', '+11234567890', '123 Flower Street', 'ESTRANGEIRO', NULL, 'A1B2C3D4');

-- Inserindo Pacotes de Viagem de Exemplo
INSERT INTO `pacotes` (nome_pacote, destino, data_partida, data_retorno, preco) VALUES
                                                                                    ('Praias do Nordeste', 'Salvador, BA', '2025-10-20', '2025-10-30', 2500.00),
                                                                                    ('Serra Gaúcha', 'Gramado, RS', '2025-07-15', '2025-07-22', 1800.50),
                                                                                    ('Tour Europeu', 'Paris, Roma, Lisboa', '2025-09-01', '2025-09-15', 7500.75);

-- Inserindo Serviços Adicionais de Exemplo
INSERT INTO `servicos_adicionais` (nome_servico, descricao, preco) VALUES
                                                                       ('Seguro Viagem Completo', 'Cobertura total para despesas médicas e bagagem.', 150.00),
                                                                       ('Aluguel de Carro', 'Carro econômico com ar condicionado.', 450.00),
                                                                       ('Passeio de Barco', 'Passeio turístico pelas principais ilhas.', 200.00);

-- Inserindo Contratações de Exemplo
-- Cliente 'João Silva' contrata o pacote 'Praias do Nordeste'
INSERT INTO `contratacoes` (id_cliente, id_pacote, data_contratacao) VALUES
    ((SELECT id_cliente FROM clientes WHERE email = 'joao.silva@email.com'), (SELECT id_pacote FROM pacotes WHERE nome_pacote = 'Praias do Nordeste'), NOW());

-- Cliente 'John Doe' contrata o 'Tour Europeu'
INSERT INTO `contratacoes` (id_cliente, id_pacote, data_contratacao) VALUES
    ((SELECT id_cliente FROM clientes WHERE email = 'john.doe@email.com'), (SELECT id_pacote FROM pacotes WHERE nome_pacote = 'Tour Europeu'), NOW());

-- Inserindo serviços em contratações
-- Adiciona 'Seguro Viagem' à contratação do João Silva
INSERT INTO `contratacao_servicos` (id_contratacao, id_servico) VALUES
    ((SELECT id_contratacao FROM contratacoes WHERE id_cliente = (SELECT id_cliente FROM clientes WHERE email = 'joao.silva@email.com')), (SELECT id_servico FROM servicos_adicionais WHERE nome_servico = 'Seguro Viagem Completo'));
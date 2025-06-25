# Sistema de Gerenciamento para Agência de Viagens

![Status](https://img.shields.io/badge/status-concluído-brightgreen)

Um sistema de desktop robusto para o gerenciamento de uma agência de turismo, desenvolvido em Java com interface gráfica Swing. O projeto abrange desde o cadastro de entidades até a criação de relacionamentos e consultas complexas.

---

## Tabela de Conteúdos

1.  [Sobre o Projeto](#sobre-o-projeto)
2.  [Funcionalidades](#funcionalidades)
3.  [Arquitetura do Projeto](#arquitetura-do-projeto)
4.  [Estrutura de Pacotes](#estrutura-de-pacotes)
5.  [Tecnologias Utilizadas](#tecnologias-utilizadas)
6.  [Como Executar](#como-executar)
7.  [Modelo do Banco de Dados](#modelo-do-banco-de-dados)

---

## Sobre o Projeto

Este sistema foi construído como um projeto acadêmico para demonstrar a aplicação de conceitos de engenharia de software, incluindo arquitetura de camadas, programação orientada a objetos, manipulação de banco de dados com JDBC e desenvolvimento de interface gráfica com Java Swing. A aplicação gerencia as operações diárias de uma agência de viagens, focando na integridade dos dados e em uma experiência de usuário clara e funcional.

## Funcionalidades

### Gerenciamento (CRUD)
-   ✔️ **Clientes:** Cadastro, listagem, edição e exclusão de clientes (nacionais e estrangeiros).
-   ✔️ **Pacotes de Viagem:** Cadastro, listagem, edição e exclusão de pacotes.
-   ✔️ **Serviços Adicionais:** Cadastro, listagem, edição e exclusão de serviços.

### Operações e Consultas
-   ✔️ **Nova Contratação:** Permite vincular um cliente a um pacote de viagem, registrando uma nova contratação.
-   ✔️ **Adição de Serviços:** Permite adicionar múltiplos serviços opcionais a uma contratação.
-   ✔️ **Consulta de Pacotes por Cliente:** Exibe todos os pacotes que um cliente específico contratou.
-   ✔️ **Consulta de Clientes por Pacote:** Exibe todos os clientes que contrataram um pacote específico.

### Recursos da Interface
-   ✔️ **Validação em Tempo Real:** Filtros e máscaras nos formulários que guiam o usuário e previnem a entrada de dados inválidos (CPF, telefone, formato de nome, etc.).
-   ✔️ **Feedback ao Usuário:** Caixas de diálogo informativas para mensagens de sucesso, aviso e erro.
-   ✔️ **Design Moderno:** Utiliza o Look & Feel FlatLaf para uma aparência limpa e moderna, com um sistema de design padronizado para cores e fontes.

---

## Arquitetura do Projeto

O sistema foi projetado com base na **Arquitetura de Camadas**, visando o desacoplamento e a organização do código:

-   **Camada de Apresentação (UI):** Responsável por toda a interação com o usuário. Construída com Java Swing, contém as janelas, painéis, tabelas e componentes visuais. Não possui lógica de negócio.
-   **Camada de Serviço (Service):** O "cérebro" da aplicação. Orquestra as operações, aplica as regras de negócio e validações (ex: um cliente não pode ser excluído se tiver contratações ativas). Atua como intermediária entre a UI e a camada de persistência.
-   **Camada de Persistência (DAO):** A única camada que se comunica diretamente com o banco de dados. Contém os objetos de acesso a dados (DAO - Data Access Object), responsáveis por executar as queries SQL (CRUD - `INSERT`, `SELECT`, `UPDATE`, `DELETE`).
-   **Camada de Modelo (Model):** Contém as classes de domínio (POJOs - Plain Old Java Objects) que representam as entidades do negócio, como `Cliente`, `Pacote`, etc.

---

## Estrutura de Pacotes

O código-fonte está organizado da seguinte forma para refletir a arquitetura:

```
/src/main/java/br/com/agenciaviagens
|
|-- /dao ............ Camada de Persistência (Data Access Objects)
|-- /exception ...... Exceções customizadas (ex: ValidationException)
|-- /factory ........ Fábrica de conexões com o banco e o inicializador do sistema
|-- /main ........... Classe principal para iniciar a aplicação
|-- /model .......... Classes de domínio (POJOs: Cliente, Pacote, etc.)
|-- /service ........ Camada de serviço e regras de negócio
`-- /ui ............. Camada de apresentação (telas e componentes Swing)
    |-- /tablemodel . Modelos de tabela customizados para JTable
    `-- /util ....... Classes utilitárias da UI (Estilo, Filtros de Documento)
```

---

## Tecnologias Utilizadas

-   **Linguagem:** Java 11+
-   **Banco de Dados:** MySQL
-   **Interface Gráfica:** Java Swing
-   **Gerenciador de Dependências:** Apache Maven
-   **Biblioteca de UI:** [FlatLaf](https://www.formdev.com/flatlaf/) (Look & Feel)

---

## Como Executar

Siga os passos abaixo para configurar e executar o projeto em seu ambiente local.

### Pré-requisitos
-   **JDK 11** ou superior instalado e configurado.
-   **Apache Maven** instalado e configurado.
-   Um **servidor MySQL** ativo e acessível.

### Instalação e Execução

1.  **Clone o Repositório**
    ```bash
    git clone https://github.com/phquartin/Agencia-De-Viagens-Swing.git
    ```

2.  **Configure a Conexão com o Banco**
    -   Abra o projeto em sua IDE Java (IntelliJ, Eclipse, etc.).
    -   Navegue até o arquivo: `src/main/java/br/com/agenciaviagens/factory/ConnectionFactory.java`.
    -   Altere as constantes `USERNAME` e `PASSWORD` para corresponder às suas credenciais de acesso ao MySQL.

3.  **Execute a Aplicação**
    -   Localize a classe `Main.java` em `src/main/java/br/com/agenciaviagens/main/`.
    -   Execute o método `main`.

    > **Nota:** Na primeira execução, o sistema irá automaticamente se conectar ao seu servidor MySQL, criar o banco de dados `agencia_viagens`, criar todas as tabelas necessárias e populá-las com dados de teste para demonstração.

---

## Modelo do Banco de Dados

Abaixo estão os comandos SQL para a criação da estrutura de tabelas do banco de dados.

```sql
CREATE TABLE `clientes` (`id_cliente` INT NOT NULL AUTO_INCREMENT, `nome` VARCHAR(255) NOT NULL, `email` VARCHAR(255) NOT NULL UNIQUE, `telefone` VARCHAR(20), `endereco` VARCHAR(255), `tipo_cliente` ENUM('NACIONAL', 'ESTRANGEIRO') NOT NULL, `cpf` VARCHAR(14) UNIQUE, `passaporte` VARCHAR(20) UNIQUE, PRIMARY KEY (`id_cliente`));

CREATE TABLE `pacotes` (`id_pacote` INT NOT NULL AUTO_INCREMENT, `nome_pacote` VARCHAR(255) NOT NULL, `destino` VARCHAR(255) NOT NULL, `data_partida` DATE, `data_retorno` DATE, `preco` DECIMAL(10, 2) NOT NULL, PRIMARY KEY (`id_pacote`));

CREATE TABLE `servicos_adicionais` (`id_servico` INT NOT NULL AUTO_INCREMENT, `nome_servico` VARCHAR(255) NOT NULL, `descricao` TEXT, `preco` DECIMAL(10, 2) NOT NULL, PRIMARY KEY (`id_servico`));

CREATE TABLE `contratacoes` (`id_contratacao` INT NOT NULL AUTO_INCREMENT, `id_cliente` INT NOT NULL, `id_pacote` INT NOT NULL, `data_contratacao` DATETIME NOT NULL, PRIMARY KEY (`id_contratacao`), FOREIGN KEY (`id_cliente`) REFERENCES `clientes`(`id_cliente`), FOREIGN KEY (`id_pacote`) REFERENCES `pacotes`(`id_pacote`) ON DELETE RESTRICT);

CREATE TABLE `contratacao_servicos` (`id_contratacao` INT NOT NULL, `id_servico` INT NOT NULL, PRIMARY KEY (`id_contratacao`, `id_servico`), FOREIGN KEY (`id_contratacao`) REFERENCES `contratacoes`(`id_contratacao`) ON DELETE CASCADE, FOREIGN KEY (`id_servico`) REFERENCES `servicos_adicionais`(`id_servico`) ON DELETE CASCADE);
```
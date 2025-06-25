package br.com.agenciaviagens.dao;

import br.com.agenciaviagens.factory.ConnectionFactory;
import br.com.agenciaviagens.model.Cliente;
import br.com.agenciaviagens.model.Contratacao;
import br.com.agenciaviagens.model.Pacote;
import br.com.agenciaviagens.model.ServicoAdicional;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContratacaoDAO {
    public void save(Contratacao contratacao) throws SQLException {
        String sqlContratacao = "INSERT INTO contratacoes (id_cliente, id_pacote, data_contratacao) VALUES (?, ?, ?)";
        String sqlServicos = "INSERT INTO contratacao_servicos (id_contratacao, id_servico) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement pstmContratacao = null;
        PreparedStatement pstmServicos = null;

        try {
            conn = ConnectionFactory.createConnectionToMySQL();
            // Inicia a transação
            conn.setAutoCommit(false);

            // Salva a contratação principal e obtém o ID gerado
            pstmContratacao = conn.prepareStatement(sqlContratacao, Statement.RETURN_GENERATED_KEYS);
            pstmContratacao.setInt(1, contratacao.getCliente().getId());
            pstmContratacao.setInt(2, contratacao.getPacote().getId());
            pstmContratacao.setTimestamp(3, new Timestamp(contratacao.getDataContratacao().getTime()));
            pstmContratacao.executeUpdate();

            // Pega o ID da contratação que acabamos de salvar
            ResultSet generatedKeys = pstmContratacao.getGeneratedKeys();
            int idContratacao = -1;
            if (generatedKeys.next()) {
                idContratacao = generatedKeys.getInt(1);
            }

            // Salva os serviços adicionais associados, se houver
            if (idContratacao > 0 && contratacao.getServicosAdicionais() != null && !contratacao.getServicosAdicionais().isEmpty()) {
                pstmServicos = conn.prepareStatement(sqlServicos);
                for (ServicoAdicional servico : contratacao.getServicosAdicionais()) {
                    pstmServicos.setInt(1, idContratacao);
                    pstmServicos.setInt(2, servico.getId());
                    pstmServicos.addBatch(); // Adiciona a operação em lote
                }
                pstmServicos.executeBatch(); // Executa todas as inserções de serviços de uma vez
            }

            // Se tudo deu certo, efetiva a transação
            conn.commit();

        } catch (SQLException e) {
            // Se algo deu errado, desfaz todas as operações
            if (conn != null) {
                conn.rollback();
            }
            throw e; // Lança a exceção para a camada de serviço tratar
        } finally {
            // Fecha todos os recursos
            if (pstmContratacao != null) pstmContratacao.close();
            if (pstmServicos != null) pstmServicos.close();
            if (conn != null) {
                conn.setAutoCommit(true); // Restaura o comportamento padrão
                conn.close();
            }
        }
    }
    public List<Contratacao> findByClienteId(int clienteId) throws SQLException {
        // SQL com JOIN para buscar dados da contratação e do pacote vinculado
        String sql = "SELECT c.id_contratacao, c.data_contratacao, p.id_pacote, p.nome_pacote, p.destino, p.preco " +
                "FROM contratacoes c " +
                "JOIN pacotes p ON c.id_pacote = p.id_pacote " +
                "WHERE c.id_cliente = ?";

        List<Contratacao> contratacoes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, clienteId);
            try (ResultSet rset = pstm.executeQuery()) {
                while (rset.next()) {
                    Contratacao contratacao = new Contratacao();
                    contratacao.setId(rset.getInt("id_contratacao"));
                    contratacao.setDataContratacao(rset.getTimestamp("data_contratacao"));

                    Pacote pacote = new Pacote();
                    pacote.setId(rset.getInt("id_pacote"));
                    pacote.setNomePacote(rset.getString("nome_pacote"));
                    pacote.setDestino(rset.getString("destino"));
                    pacote.setPreco(rset.getDouble("preco"));

                    contratacao.setPacote(pacote);

                    contratacoes.add(contratacao);
                }
            }
        }
        return contratacoes;
    }
    public List<Contratacao> findByPacoteId(int pacoteId) throws SQLException {
        String sql = "SELECT c.id_contratacao, c.data_contratacao, cl.id_cliente, cl.nome, cl.email " +
                "FROM contratacoes c " +
                "JOIN clientes cl ON c.id_cliente = cl.id_cliente " +
                "WHERE c.id_pacote = ?";

        List<Contratacao> contratacoes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, pacoteId);
            try (ResultSet rset = pstm.executeQuery()) {
                while (rset.next()) {
                    Contratacao contratacao = new Contratacao();
                    contratacao.setId(rset.getInt("id_contratacao"));
                    contratacao.setDataContratacao(rset.getTimestamp("data_contratacao"));

                    Cliente cliente = new Cliente();
                    cliente.setId(rset.getInt("id_cliente"));
                    cliente.setNome(rset.getString("nome"));
                    cliente.setEmail(rset.getString("email"));

                    contratacao.setCliente(cliente);

                    contratacoes.add(contratacao);
                }
            }
        }
        return contratacoes;
    }
}
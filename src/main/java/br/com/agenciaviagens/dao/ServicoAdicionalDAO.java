package br.com.agenciaviagens.dao;

import br.com.agenciaviagens.factory.ConnectionFactory;
import br.com.agenciaviagens.model.Pacote;
import br.com.agenciaviagens.model.ServicoAdicional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicoAdicionalDAO {

    // Método para salvar um serviço adicional
    public void save(ServicoAdicional servico) {
        String sql = "INSERT INTO servicos_adicionais (nome_servico, descricao, preco) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConnectionFactory.createConnectionToMySQL();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, servico.getNomeServico());
            pstm.setString(2, servico.getDescricao());
            pstm.setDouble(3, servico.getPreco());

            pstm.execute();
            System.out.println("Serviço adicional salvo com sucesso!");

        } catch (SQLException e) {
            System.err.println("Falha ao salvar um serviço:" + e.getMessage());
        } finally {
            // Fecha as conexões
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Falha ao fechar conexões: "+ e.getMessage());
            }
        }
    }

    // Método para ler todos os serviços adicionais
    public List<ServicoAdicional> findAll() {
        String sql = "SELECT * FROM servicos_adicionais";
        List<ServicoAdicional> servicos = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.createConnectionToMySQL();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                ServicoAdicional servico = new ServicoAdicional();

                servico.setId(rset.getInt("id_servico"));
                servico.setNomeServico(rset.getString("nome_servico"));
                servico.setDescricao(rset.getString("descricao"));
                servico.setPreco(rset.getDouble("preco"));

                servicos.add(servico);
            }
        } catch (SQLException e) {
            System.err.println("Falha ao obter a lista serviços: "+ e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Falha ao fechar conexões: "+ e.getMessage());
            }
        }
        return servicos;
    }

    // Método para buscar um servico pelo seu ID
    public ServicoAdicional findById(int id) {
        String sql = "SELECT * FROM servicos_adicionais WHERE id_servico = ?";
        ServicoAdicional servico = null;

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.createConnectionToMySQL();
            pstm = conn.prepareStatement(sql);

            // Seta o ID que estamos buscando
            pstm.setInt(1, id);

            rset = pstm.executeQuery();

            // Se o ResultSet tiver um resultado, significa que um objeto foi encontrado
            if (rset.next()) {
                servico = new ServicoAdicional();
                servico.setId(rset.getInt("id_servico"));
                servico.setNomeServico(rset.getString("nome_servico"));
                servico.setPreco(rset.getDouble("preco"));
                servico.setDescricao(rset.getString("descricao"));
            }
        } catch (SQLException e) {
            System.err.println("Falha ao obter o Servico: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Falha ao fechar as conexões: " + e.getMessage());
            }
        }

        // Retorna o objeto encontrado ou null se não existir
        return servico;
    }

    // Método para deletar um servico pelo seu ID
    public void deleteById(int id) {
        String sql = "DELETE FROM servicos_adicionais WHERE id_servico = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConnectionFactory.createConnectionToMySQL();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);

            pstm.execute();
            System.out.println("Servico deletado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao deletar servico. Pode haver contratações associadas." + e.getMessage());
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Falha ao fechar as conexões: " + e.getMessage());
            }
        }
    }

}
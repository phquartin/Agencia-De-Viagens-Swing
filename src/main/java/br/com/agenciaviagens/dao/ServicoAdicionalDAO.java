package br.com.agenciaviagens.dao;

import br.com.agenciaviagens.factory.ConnectionFactory;
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
}
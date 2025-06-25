package br.com.agenciaviagens.dao;

import br.com.agenciaviagens.factory.ConnectionFactory;
import br.com.agenciaviagens.model.Contratacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ContratacaoDAO {
    public void save(Contratacao contratacao) throws SQLException {
        String sql = "INSERT INTO contratacoes (id_cliente, id_pacote, data_contratacao) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, contratacao.getCliente().getId());
            pstm.setInt(2, contratacao.getPacote().getId());
            pstm.setTimestamp(3, new Timestamp(contratacao.getDataContratacao().getTime()));
            pstm.execute();
        }
    }
}
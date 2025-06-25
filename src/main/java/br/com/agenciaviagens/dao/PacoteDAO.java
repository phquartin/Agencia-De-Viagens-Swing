package br.com.agenciaviagens.dao;

import br.com.agenciaviagens.factory.ConnectionFactory;
import br.com.agenciaviagens.model.Pacote;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacoteDAO {


    // Método para salvar um pacote no banco de dados
    public void save(Pacote pacote) {
        String sql = "INSERT INTO pacotes (nome_pacote, destino, preco, data_partida, data_retorno) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            // 1. Criar uma conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            // 2. Criar um PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            // 3. Adicionar os valores que são esperados pela query
            pstm.setString(1, pacote.getNomePacote());
            pstm.setString(2, pacote.getDestino());
            pstm.setDouble(3, pacote.getPreco());
            pstm.setDate(4, new java.sql.Date(pacote.getDataPartida().getTime()));
            pstm.setDate(5, new java.sql.Date(pacote.getDataRetorno().getTime()));

            // 4. Executar a query
            pstm.execute();

            System.out.println("Pacote salvo com sucesso!");

        } catch (SQLException e) {

            System.err.println("Falha ao gravar a Pacote: " + e.getMessage());
        } finally {
            // 5. Fechar as conexões
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Falha ao fechar as conexões: " + e.getMessage());
            }
        }
    }

    // Método para ler todos os pacotes do banco de dados
    public List<Pacote> findAll() {
        String sql = "SELECT * FROM pacotes";

        List<Pacote> pacotes = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstm = null;
        // Objeto que vai recuperar os dados do banco.
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.createConnectionToMySQL();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            // Enquanto existirem dados no banco, faça:
            while (rset.next()) {
                Pacote pacote = new Pacote();

                // Recupera os dados do ResultSet e atribui ao objeto Pacote
                pacote.setId(rset.getInt("id_pacote"));
                pacote.setNomePacote(rset.getString("nome_pacote"));
                pacote.setDestino(rset.getString("destino"));
                pacote.setPreco(rset.getDouble("preco"));
                pacote.setDataPartida(rset.getDate("data_partida"));
                pacote.setDataRetorno(rset.getDate("data_retorno"));

                // Adiciona o pacote recuperado à lista de pacotes
                pacotes.add(pacote);
            }
        } catch (SQLException e) {
            System.err.println("Falha ao obter a lista de pacotes: " + e.getMessage());
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Falha ao fechar as conexões: " + e.getMessage());
            }
        }

        return pacotes;
    }

    // Método para buscar um pacote pelo seu ID
    public Pacote findById(int id) {
        String sql = "SELECT * FROM pacotes WHERE id_pacote = ?";
        Pacote pacote = null; // Pode ser null

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
                pacote = new Pacote();
                pacote.setId(rset.getInt("id_pacote"));
                pacote.setNomePacote(rset.getString("nome_pacote"));
                pacote.setDestino(rset.getString("destino"));
                pacote.setPreco(rset.getDouble("preco"));
                pacote.setDataPartida(rset.getDate("data_partida"));
                pacote.setDataRetorno(rset.getDate("data_retorno"));
            }
        } catch (SQLException e) {
            System.err.println("Falha ao obter a Pacote: " + e.getMessage());
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
        return pacote;
    }

    // Método para deletar um pacote pelo seu ID
    public void deleteById(int id) {
        String sql = "DELETE FROM pacotes WHERE id_pacote = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConnectionFactory.createConnectionToMySQL();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);

            pstm.execute();
            System.out.println("Pacote deletado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao deletar pacote. Pode haver contratações associadas." + e.getMessage());
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
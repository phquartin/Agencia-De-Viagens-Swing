package br.com.agenciaviagens.dao;

import br.com.agenciaviagens.factory.ConnectionFactory;
import br.com.agenciaviagens.model.Cliente;
import br.com.agenciaviagens.model.ServicoAdicional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void save(Cliente cliente) throws SQLException{
        String sql = "INSERT INTO clientes (nome, email, telefone, endereco, tipo_cliente, cpf, passaporte) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConnectionFactory.createConnectionToMySQL();
            pstm = conn.prepareStatement(sql);

            // Seta os valores comuns
            pstm.setString(1, cliente.getNome());
            pstm.setString(2, cliente.getEmail());
            pstm.setString(3, cliente.getTelefone());
            pstm.setString(4, cliente.getEndereco());
            pstm.setString(5, cliente.getTipo().name()); // .name() converte o enum para String ("NACIONAL" ou "ESTRANGEIRO")

            // Lógica para CPF e Passaporte
            if (cliente.getTipo() == Cliente.TipoCliente.NACIONAL) {
                pstm.setString(6, cliente.getCpf());
                pstm.setNull(7, Types.VARCHAR); // O campo passaporte será nulo
            } else {
                pstm.setNull(6, Types.VARCHAR); // O campo cpf será nulo
                pstm.setString(7, cliente.getPassaporte());
            }

            pstm.execute();
            System.out.println("Cliente salvo com sucesso!");

        } catch (SQLException e) {
            System.err.println("Falha ao salvar Cliente:" + e.getMessage());
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Falha ao fechar conexões:" + e.getMessage());
            }
        }
    }

    public List<Cliente> findAll() {
        String sql = "SELECT * FROM clientes";
        List<Cliente> clientes = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.createConnectionToMySQL();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                Cliente cliente = new Cliente();

                cliente.setId(rset.getInt("id_cliente"));
                cliente.setNome(rset.getString("nome"));
                cliente.setEmail(rset.getString("email"));
                cliente.setTelefone(rset.getString("telefone"));
                cliente.setEndereco(rset.getString("endereco"));
                // Converte a String do banco de volta para o enum
                cliente.setTipo(Cliente.TipoCliente.valueOf(rset.getString("tipo_cliente")));
                cliente.setCpf(rset.getString("cpf"));
                cliente.setPassaporte(rset.getString("passaporte"));

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Falha ao listar todos os Clientes:" + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Falha ao fechar conexões:" + e.getMessage());
            }
        }
        return clientes;
    }

    // Método para buscar um cliente pelo seu ID
    public Cliente findById(int id) {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        Cliente cliente = null;

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
                cliente = new Cliente();

                cliente.setId(rset.getInt("id_cliente"));
                cliente.setNome(rset.getString("nome"));
                cliente.setEmail(rset.getString("email"));
                cliente.setTelefone(rset.getString("telefone"));
                cliente.setEndereco(rset.getString("endereco"));
                // Converte a String do banco de volta para o enum
                cliente.setTipo(Cliente.TipoCliente.valueOf(rset.getString("tipo_cliente")));
                cliente.setCpf(rset.getString("cpf"));
                cliente.setPassaporte(rset.getString("passaporte"));
            }
        } catch (SQLException e) {
            System.err.println("Falha ao obter o Cliente: " + e.getMessage());
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
        return cliente;
    }

    // Método para deletar um cliente pelo seu ID
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConnectionFactory.createConnectionToMySQL();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);

            pstm.execute();
            System.out.println("Cliente deletado com sucesso!");

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
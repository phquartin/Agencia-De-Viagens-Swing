package br.com.agenciaviagens.dao;

import br.com.agenciaviagens.factory.ConnectionFactory;
import br.com.agenciaviagens.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Adicionado "throws SQLException" para propagar o erro para a camada de serviço
    public void save(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nome, email, telefone, endereco, tipo_cliente, cpf, passaporte) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, cliente.getNome());
            pstm.setString(2, cliente.getEmail());
            pstm.setString(3, cliente.getTelefone());
            pstm.setString(4, cliente.getEndereco());
            pstm.setString(5, cliente.getTipo().name());

            if (cliente.getTipo() == Cliente.TipoCliente.NACIONAL) {
                pstm.setString(6, cliente.getCpf());
                pstm.setNull(7, Types.VARCHAR);
            } else {
                pstm.setNull(6, Types.VARCHAR);
                pstm.setString(7, cliente.getPassaporte());
            }

            pstm.execute();
        }
    }

    // Adicionado "throws SQLException"
    public void update(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nome = ?, email = ?, telefone = ?, endereco = ?, tipo_cliente = ?, cpf = ?, passaporte = ? WHERE id_cliente = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, cliente.getNome());
            pstm.setString(2, cliente.getEmail());
            pstm.setString(3, cliente.getTelefone());
            pstm.setString(4, cliente.getEndereco());
            pstm.setString(5, cliente.getTipo().name());

            if (cliente.getTipo() == Cliente.TipoCliente.NACIONAL) {
                pstm.setString(6, cliente.getCpf());
                pstm.setNull(7, Types.VARCHAR);
            } else {
                pstm.setNull(6, Types.VARCHAR);
                pstm.setString(7, cliente.getPassaporte());
            }

            pstm.setInt(8, cliente.getId());
            pstm.execute();
        }
    }

    // Adicionado "throws SQLException"
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            pstm.execute();
        }
    }

    public List<Cliente> findAll() {
        String sql = "SELECT * FROM clientes";
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {

            while (rset.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rset.getInt("id_cliente"));
                cliente.setNome(rset.getString("nome"));
                cliente.setEmail(rset.getString("email"));
                cliente.setTelefone(rset.getString("telefone"));
                cliente.setEndereco(rset.getString("endereco"));
                cliente.setTipo(Cliente.TipoCliente.valueOf(rset.getString("tipo_cliente")));
                cliente.setCpf(rset.getString("cpf"));
                cliente.setPassaporte(rset.getString("passaporte"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public Cliente findById(int id) {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        Cliente cliente = null;
        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    cliente = new Cliente();

                    cliente.setId(rset.getInt("id_cliente"));
                    cliente.setNome(rset.getString("nome"));
                    cliente.setEmail(rset.getString("email"));
                    cliente.setTelefone(rset.getString("telefone"));
                    cliente.setEndereco(rset.getString("endereco"));
                    cliente.setTipo(Cliente.TipoCliente.valueOf(rset.getString("tipo_cliente")));
                    cliente.setCpf(rset.getString("cpf"));
                    cliente.setPassaporte(rset.getString("passaporte"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar cliente por ID: " + e.getMessage());
        }
        return cliente;
    }
}
package br.com.agenciaviagens.ui.tablemodel;

import br.com.agenciaviagens.model.Cliente;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ClienteTableModel extends AbstractTableModel {

    private final String[] colunas = {"ID", "Nome", "E-mail", "Tipo", "Documento", "Telefone"};
    private List<Cliente> clientes;

    public ClienteTableModel() {
        this.clientes = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return clientes.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Cliente cliente = clientes.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return cliente.getId();
            case 1:
                return cliente.getNome();
            case 2:
                return cliente.getEmail();
            case 3:
                return cliente.getTipo().name(); // Retorna "NACIONAL" ou "ESTRANGEIRO"
            case 4:
                // Lógica para mostrar o documento correto
                if (cliente.getTipo() == Cliente.TipoCliente.NACIONAL) {
                    return cliente.getCpf();
                } else {
                    return cliente.getPassaporte();
                }
            case 5:
                return cliente.getTelefone();
            default:
                return null;
        }
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
        fireTableDataChanged();
    }

    public Cliente getClienteAt(int rowIndex) {
        return clientes.get(rowIndex);
    }
}
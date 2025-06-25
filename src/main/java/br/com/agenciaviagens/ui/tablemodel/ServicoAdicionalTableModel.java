package br.com.agenciaviagens.ui.tablemodel;

import br.com.agenciaviagens.model.ServicoAdicional;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ServicoAdicionalTableModel extends AbstractTableModel {

    private final String[] colunas = {"ID", "Nome do Serviço", "Preço (R$)"};
    private List<ServicoAdicional> servicos;

    public ServicoAdicionalTableModel() {
        this.servicos = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return servicos.size();
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
        ServicoAdicional servico = servicos.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> servico.getId();
            case 1 -> servico.getNomeServico();
            case 2 -> String.format("%.2f", servico.getPreco());
            default -> null;
        };
    }

    public void setServicos(List<ServicoAdicional> servicos) {
        this.servicos = servicos;
        fireTableDataChanged();
    }

    public ServicoAdicional getServicoAt(int rowIndex) {
        return servicos.get(rowIndex);
    }
}
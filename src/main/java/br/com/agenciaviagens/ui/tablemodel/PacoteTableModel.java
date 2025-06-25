package br.com.agenciaviagens.ui.tablemodel;

import br.com.agenciaviagens.model.Pacote;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PacoteTableModel extends AbstractTableModel {

    private final String[] colunas = {"ID", "Nome do Pacote", "Destino", "Preço (R$)"};
    private List<Pacote> pacotes;

    public PacoteTableModel() {
        this.pacotes = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return pacotes.size();
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
        Pacote pacote = pacotes.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> pacote.getId();
            case 1 -> pacote.getNomePacote();
            case 2 -> pacote.getDestino();
            case 3 ->
                // Formatando o preço para exibição
                    String.format("%.2f", pacote.getPreco());
            default -> null;
        };
    }

    public void setPacotes(List<Pacote> pacotes) {
        this.pacotes = pacotes;
        // Notifica a JTable que os dados mudaram, para que ela se atualize
        fireTableDataChanged();
    }

    public Pacote getPacoteAt(int rowIndex) {
        return pacotes.get(rowIndex);
    }
}
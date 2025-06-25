package br.com.agenciaviagens.ui.tablemodel;

import br.com.agenciaviagens.model.Contratacao;
import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ContratacaoTableModel extends AbstractTableModel {

    private final String[] colunas = {"ID Contrato", "Pacote Contratado", "Destino", "Data da Contratação"};
    private List<Contratacao> contratacoes;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public ContratacaoTableModel() {
        this.contratacoes = new ArrayList<>();
    }

    @Override
    public int getRowCount() { return contratacoes.size(); }
    @Override
    public int getColumnCount() { return colunas.length; }
    @Override
    public String getColumnName(int column) { return colunas[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Contratacao contratacao = contratacoes.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> contratacao.getId();
            case 1 -> contratacao.getPacote().getNomePacote();
            case 2 -> contratacao.getPacote().getDestino();
            case 3 -> dateFormat.format(contratacao.getDataContratacao());
            default -> null;
        };
    }

    public void setContratacoes(List<Contratacao> contratacoes) {
        this.contratacoes = contratacoes;
        fireTableDataChanged();
    }
}
package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.Contratacao;
import br.com.agenciaviagens.model.Pacote;
import br.com.agenciaviagens.service.ContratacaoService;
import br.com.agenciaviagens.service.PacoteService;
import br.com.agenciaviagens.ui.tablemodel.ConsultaPacoteTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class PainelConsultaPacote extends JPanel {

    private JComboBox<Pacote> comboPacotes;
    private JTable tabelaResultados;
    private ConsultaPacoteTableModel tableModel;
    private ContratacaoService contratacaoService;

    public PainelConsultaPacote() {
        this.contratacaoService = new ContratacaoService();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFiltro.add(new JLabel("Consultar clientes que contrataram o pacote:"));
        comboPacotes = new JComboBox<>();
        comboPacotes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Pacote) setText(((Pacote) value).getNomePacote());
                return this;
            }
        });
        painelFiltro.add(comboPacotes);
        add(painelFiltro, BorderLayout.NORTH);

        tableModel = new ConsultaPacoteTableModel();
        tabelaResultados = new JTable(tableModel);
        add(new JScrollPane(tabelaResultados), BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                System.out.println("Painel de Consulta por Pacote visível. Atualizando lista de pacotes...");
                carregarPacotes();
            }
        });

        comboPacotes.addActionListener(e -> buscarContratacoes());
    }

    private void carregarPacotes() {
        Pacote pacoteSelecionado = (Pacote) comboPacotes.getSelectedItem();
        PacoteService pacoteService = new PacoteService();
        comboPacotes.removeAllItems();
        pacoteService.listarTodos().forEach(comboPacotes::addItem);

        if (pacoteSelecionado != null) {
            comboPacotes.setSelectedItem(pacoteSelecionado);
        }

        if (comboPacotes.getSelectedItem() != pacoteSelecionado) {
            buscarContratacoes();
        }
    }

    private void buscarContratacoes() {
        Pacote pacoteSelecionado = (Pacote) comboPacotes.getSelectedItem();
        if (pacoteSelecionado == null) {
            tableModel.setContratacoes(List.of());
            return;
        }
        try {
            List<Contratacao> contratacoes = contratacaoService.buscarContratacoesPorPacote(pacoteSelecionado.getId());
            tableModel.setContratacoes(contratacoes);
        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
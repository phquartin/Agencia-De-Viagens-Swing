package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.Cliente;
import br.com.agenciaviagens.model.Contratacao;
import br.com.agenciaviagens.service.ClienteService;
import br.com.agenciaviagens.service.ContratacaoService;
import br.com.agenciaviagens.ui.tablemodel.ContratacaoTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class PainelConsultaCliente extends JPanel {

    private JComboBox<Cliente> comboClientes;
    private JTable tabelaResultados;
    private ContratacaoTableModel tableModel;
    private ContratacaoService contratacaoService;

    public PainelConsultaCliente() {
        this.contratacaoService = new ContratacaoService();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFiltro.add(new JLabel("Consultar pacotes contratados pelo cliente:"));
        comboClientes = new JComboBox<>();
        comboClientes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente) setText(((Cliente) value).getNome());
                return this;
            }
        });
        painelFiltro.add(comboClientes);
        add(painelFiltro, BorderLayout.NORTH);

        tableModel = new ContratacaoTableModel();
        tabelaResultados = new JTable(tableModel);
        add(new JScrollPane(tabelaResultados), BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                System.out.println("Painel de Consulta por Cliente visível. Atualizando lista de clientes...");
                carregarClientes();
            }
        });

        comboClientes.addActionListener(e -> buscarContratacoes());
    }

    private void carregarClientes() {
        Cliente clienteSelecionado = (Cliente) comboClientes.getSelectedItem();
        ClienteService clienteService = new ClienteService();
        comboClientes.removeAllItems();
        clienteService.listarTodos().forEach(comboClientes::addItem);

        // Tenta manter o cliente que já estava selecionado
        if (clienteSelecionado != null) {
            comboClientes.setSelectedItem(clienteSelecionado);
        }

        // Se a seleção mudou (ou era nula), atualiza a busca.
        if (comboClientes.getSelectedItem() != clienteSelecionado) {
            buscarContratacoes();
        }
    }

    private void buscarContratacoes() {
        Cliente clienteSelecionado = (Cliente) comboClientes.getSelectedItem();
        if (clienteSelecionado == null) {
            tableModel.setContratacoes(List.of());
            return;
        }
        try {
            List<Contratacao> contratacoes = contratacaoService.buscarContratacoesPorCliente(clienteSelecionado.getId());
            tableModel.setContratacoes(contratacoes);
        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro na Consulta", JOptionPane.ERROR_MESSAGE);
        }
    }
}
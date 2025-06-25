package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.Cliente;
import br.com.agenciaviagens.model.Contratacao;
import br.com.agenciaviagens.service.ClienteService;
import br.com.agenciaviagens.service.ContratacaoService;
import br.com.agenciaviagens.ui.tablemodel.ContratacaoTableModel;
import br.com.agenciaviagens.ui.util.Estilo;

import javax.swing.*;
import java.awt.*;
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

        // Painel Superior com o seletor de cliente
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

        // Tabela de Resultados
        tableModel = new ContratacaoTableModel();
        tabelaResultados = new JTable(tableModel);
        add(new JScrollPane(tabelaResultados), BorderLayout.CENTER);

        carregarClientes();

        // Ação para quando um cliente for selecionado
        comboClientes.addActionListener(e -> buscarContratacoes());
    }

    private void carregarClientes() {
        ClienteService clienteService = new ClienteService();
        comboClientes.removeAllItems();
        clienteService.listarTodos().forEach(comboClientes::addItem);
        // Garante que a busca seja acionada para o primeiro item da lista
        if (comboClientes.getItemCount() > 0) {
            comboClientes.setSelectedIndex(0);
        }
    }

    private void buscarContratacoes() {
        Cliente clienteSelecionado = (Cliente) comboClientes.getSelectedItem();
        if (clienteSelecionado == null) {
            tableModel.setContratacoes(List.of()); // Limpa a tabela se nada for selecionado
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
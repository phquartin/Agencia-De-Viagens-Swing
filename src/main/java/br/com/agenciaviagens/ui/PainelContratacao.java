package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.Cliente;
import br.com.agenciaviagens.model.Contratacao;
import br.com.agenciaviagens.model.Pacote;
import br.com.agenciaviagens.model.ServicoAdicional;
import br.com.agenciaviagens.service.ClienteService;
import br.com.agenciaviagens.service.ContratacaoService;
import br.com.agenciaviagens.service.PacoteService;
import br.com.agenciaviagens.service.ServicoAdicionalService;
import br.com.agenciaviagens.ui.util.Estilo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Date;
import java.util.List;

public class PainelContratacao extends JPanel {

    private JComboBox<Cliente> comboClientes;
    private JComboBox<Pacote> comboPacotes;
    private JList<ServicoAdicional> listaServicos;
    private ContratacaoService contratacaoService;

    public PainelContratacao() {
        this.contratacaoService = new ContratacaoService();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelTitulo = new JLabel("Nova Contratação de Pacote", SwingConstants.LEFT);
        labelTitulo.setFont(Estilo.FONTE_TITULO);
        add(labelTitulo, BorderLayout.NORTH);

        // Painel de Seleção
        JPanel painelSelecao = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboClientes = new JComboBox<>();
        comboPacotes = new JComboBox<>();
        listaServicos = new JList<>();

        // Renderizadores
        comboClientes.setRenderer(createClienteRenderer());
        comboPacotes.setRenderer(createPacoteRenderer());
        listaServicos.setCellRenderer(createServicoRenderer());

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; painelSelecao.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST; painelSelecao.add(comboClientes, gbc);
        gbc.gridy++; gbc.gridx = 0; gbc.weightx = 0.0; painelSelecao.add(new JLabel("Pacote:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; painelSelecao.add(comboPacotes, gbc);
        gbc.gridy++; gbc.gridx = 0; gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.NORTHEAST; painelSelecao.add(new JLabel("Serviços (Opcional):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0; painelSelecao.add(new JScrollPane(listaServicos), gbc);

        add(painelSelecao, BorderLayout.CENTER);

        JButton btnContratar = new JButton("Realizar Contratação");
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotao.add(btnContratar);
        add(painelBotao, BorderLayout.SOUTH);

        // Adiciona um listener que é acionado quando o painel fica visível.
        // Usamos ComponentAdapter para não precisar implementar todos os métodos da interface.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Toda vez que o painel for mostrado, atualiza os dados.
                System.out.println("Painel de Contratação visível. Atualizando dados...");
                carregarDados();
            }
        });

        // O carregamento inicial de dados não é mais necessário no construtor,
        // pois o componentShown será chamado na primeira vez que a tela aparecer.

        btnContratar.addActionListener(e -> contratar());
    }

    private void carregarDados() {
        // Carrega clientes
        Cliente clienteSelecionado = (Cliente) comboClientes.getSelectedItem();
        comboClientes.removeAllItems();
        new ClienteService().listarTodos().forEach(comboClientes::addItem);
        if (clienteSelecionado != null) comboClientes.setSelectedItem(clienteSelecionado);

        // Carrega pacotes
        Pacote pacoteSelecionado = (Pacote) comboPacotes.getSelectedItem();
        comboPacotes.removeAllItems();
        new PacoteService().listarTodos().forEach(comboPacotes::addItem);
        if (pacoteSelecionado != null) comboPacotes.setSelectedItem(pacoteSelecionado);

        // Carrega serviços
        List<ServicoAdicional> servicos = new ServicoAdicionalService().listarTodos();
        listaServicos.setListData(servicos.toArray(new ServicoAdicional[0]));
    }

    private void contratar() {
        try {
            Contratacao novaContratacao = new Contratacao();
            novaContratacao.setCliente((Cliente) comboClientes.getSelectedItem());
            novaContratacao.setPacote((Pacote) comboPacotes.getSelectedItem());
            novaContratacao.setDataContratacao(new Date());

            List<ServicoAdicional> servicosSelecionados = listaServicos.getSelectedValuesList();
            novaContratacao.setServicosAdicionais(servicosSelecionados);

            contratacaoService.salvar(novaContratacao);

            JOptionPane.showMessageDialog(this, "Contratação realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            listaServicos.clearSelection();
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Métodos Renderizadores
    private ListCellRenderer<Object> createClienteRenderer() { return new DefaultListCellRenderer() { @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) { super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); if (value instanceof Cliente) setText(((Cliente) value).getNome()); return this; } }; }
    private ListCellRenderer<Object> createPacoteRenderer() { return new DefaultListCellRenderer() { @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) { super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); if (value instanceof Pacote) setText(((Pacote) value).getNomePacote()); return this; } }; }
    private ListCellRenderer<Object> createServicoRenderer() { return new DefaultListCellRenderer() { @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) { super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); if (value instanceof ServicoAdicional) { ServicoAdicional s = (ServicoAdicional) value; setText(String.format("%s (R$ %.2f)", s.getNomeServico(), s.getPreco())); } return this; } }; }
}
package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.Cliente;
import br.com.agenciaviagens.model.Contratacao;
import br.com.agenciaviagens.model.Pacote;
import br.com.agenciaviagens.service.ClienteService;
import br.com.agenciaviagens.service.ContratacaoService;
import br.com.agenciaviagens.service.PacoteService;
import br.com.agenciaviagens.ui.util.Estilo;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class PainelContratacao extends JPanel {

    private JComboBox<Cliente> comboClientes;
    private JComboBox<Pacote> comboPacotes;
    private ContratacaoService contratacaoService;

    public PainelContratacao() {
        this.contratacaoService = new ContratacaoService();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
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

        // Renderizadores para mostrar o nome em vez do objeto
        comboClientes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente) {
                    setText(((Cliente) value).getNome());
                }
                return this;
            }
        });

        comboPacotes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Pacote) {
                    setText(((Pacote) value).getNomePacote());
                }
                return this;
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; painelSelecao.add(new JLabel("Selecione o Cliente:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; painelSelecao.add(comboClientes, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; painelSelecao.add(new JLabel("Selecione o Pacote:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; painelSelecao.add(comboPacotes, gbc);

        add(painelSelecao, BorderLayout.CENTER);

        // Botão de Ação
        JButton btnContratar = new JButton("Realizar Contratação");
        btnContratar.setBackground(Estilo.COR_PRIMARIA);
        btnContratar.setForeground(Color.WHITE);
        btnContratar.setFont(Estilo.FONTE_BOTAO);
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotao.add(btnContratar);
        add(painelBotao, BorderLayout.SOUTH);

        carregarCombos();

        btnContratar.addActionListener(e -> contratar());
    }

    private void carregarCombos() {
        // Carrega clientes
        ClienteService clienteService = new ClienteService();
        comboClientes.removeAllItems();
        clienteService.listarTodos().forEach(comboClientes::addItem);

        // Carrega pacotes
        PacoteService pacoteService = new PacoteService();
        comboPacotes.removeAllItems();
        pacoteService.listarTodos().forEach(comboPacotes::addItem);
    }

    private void contratar() {
        try {
            Contratacao novaContratacao = new Contratacao();
            novaContratacao.setCliente((Cliente) comboClientes.getSelectedItem());
            novaContratacao.setPacote((Pacote) comboPacotes.getSelectedItem());
            novaContratacao.setDataContratacao(new Date()); // Data atual

            contratacaoService.salvar(novaContratacao);

            JOptionPane.showMessageDialog(this, "Contratação realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            // Limpa a seleção ou pode navegar para outra tela
            carregarCombos(); // Recarrega para o caso de algo mudar
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
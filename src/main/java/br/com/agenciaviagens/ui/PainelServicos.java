package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.ServicoAdicional;
import br.com.agenciaviagens.service.ServicoAdicionalService;
import br.com.agenciaviagens.ui.tablemodel.ServicoAdicionalTableModel;
import br.com.agenciaviagens.ui.util.Estilo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelServicos extends JPanel {

    private final ServicoAdicionalService servicoService;
    private final JTable tabelaServicos;
    private final ServicoAdicionalTableModel tableModel;

    public PainelServicos() {
        this.servicoService = new ServicoAdicionalService();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(new JLabel("Gerenciamento de Serviços Adicionais", SwingConstants.LEFT), BorderLayout.NORTH);

        tableModel = new ServicoAdicionalTableModel();
        tabelaServicos = new JTable(tableModel);
        tabelaServicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabelaServicos), BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNovo = new JButton("Novo Serviço");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        // Estilos... (código dos estilos dos botões permanece o mesmo)
        btnNovo.setBackground(Estilo.COR_PRIMARIA);
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setFont(Estilo.FONTE_BOTAO);

        btnEditar.setBackground(Estilo.COR_SEGUNDARIA);
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(Estilo.FONTE_BOTAO);

        btnExcluir.setBackground(Estilo.COR_DESTAQUE);
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(Estilo.FONTE_BOTAO);

        painelBotoes.add(btnNovo);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        add(painelBotoes, BorderLayout.SOUTH);

        carregarDadosTabela();

        btnNovo.addActionListener(e -> {
            DialogoServico dialogo = new DialogoServico((Frame) SwingUtilities.getWindowAncestor(this), servicoService, null, this::carregarDadosTabela);
            dialogo.setVisible(true);
        });

        btnEditar.addActionListener(e -> {
            int linha = tabelaServicos.getSelectedRow();
            if (linha != -1) {
                ServicoAdicional servico = tableModel.getServicoAt(linha);
                DialogoServico dialogo = new DialogoServico((Frame) SwingUtilities.getWindowAncestor(this), servicoService, servico, this::carregarDadosTabela);
                dialogo.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um serviço para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnExcluir.addActionListener(e -> {
            int linha = tabelaServicos.getSelectedRow();
            if (linha != -1) {
                if (JOptionPane.showConfirmDialog(this, "Tem certeza?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    try {
                        servicoService.excluir(tableModel.getServicoAt(linha).getId());
                        carregarDadosTabela();
                    } catch (ValidationException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um serviço para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void carregarDadosTabela() {
        List<ServicoAdicional> servicos = servicoService.listarTodos();
        tableModel.setServicos(servicos);
    }
}
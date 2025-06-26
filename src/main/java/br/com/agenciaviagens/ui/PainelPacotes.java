package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.Pacote;
import br.com.agenciaviagens.service.PacoteService;
import br.com.agenciaviagens.ui.tablemodel.PacoteTableModel;
import br.com.agenciaviagens.ui.util.Estilo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelPacotes extends JPanel {

    private final PacoteService pacoteService;
    private final JTable tabelaPacotes;
    private final PacoteTableModel tableModel;

    public PainelPacotes() {
        this.pacoteService = new PacoteService();

        setLayout(new BorderLayout(10, 10));
        setBackground(Estilo.COR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel labelTitulo = new JLabel("Gerenciamento de Pacotes");
        labelTitulo.setFont(Estilo.FONTE_TITULO);
        labelTitulo.setForeground(Estilo.COR_TEXTO);
        add(labelTitulo, BorderLayout.NORTH);

        // Tabela
        tableModel = new PacoteTableModel();
        tabelaPacotes = new JTable(tableModel);
        tabelaPacotes.setFont(Estilo.FONTE_CORPO);
        tabelaPacotes.getTableHeader().setFont(Estilo.FONTE_SUBTITULO);
        tabelaPacotes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabelaPacotes);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBackground(Estilo.COR_BACKGROUND);

        JButton btnNovo = new JButton("Novo Pacote");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

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

        // --- AÇÕES DOS BOTÕES ---

        btnExcluir.addActionListener(e -> {
            int linhaSelecionada = tabelaPacotes.getSelectedRow();

            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um pacote para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Pede confirmação ao usuário
            int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este pacote?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    Pacote pacoteSelecionado = tableModel.getPacoteAt(linhaSelecionada);
                    pacoteService.excluir(pacoteSelecionado.getId());
                    JOptionPane.showMessageDialog(this, "Pacote excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarDadosTabela(); // Atualiza a tabela
                } catch (ValidationException ex) {
                    // Mostra a mensagem de erro amigável
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnNovo.addActionListener(e -> {
            DialogoPacote dialogo = new DialogoPacote((Frame) SwingUtilities.getWindowAncestor(this), pacoteService, null, this::carregarDadosTabela);
            dialogo.setVisible(true);
        });

        btnEditar.addActionListener(e -> {
            int linhaSelecionada = tabelaPacotes.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um pacote para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Pacote pacoteSelecionado = tableModel.getPacoteAt(linhaSelecionada);
            DialogoPacote dialogo = new DialogoPacote((Frame) SwingUtilities.getWindowAncestor(this), pacoteService, pacoteSelecionado, this::carregarDadosTabela);
            dialogo.setVisible(true);
        });
    }

    private void carregarDadosTabela() {
        List<Pacote> pacotes = pacoteService.listarTodos();
        tableModel.setPacotes(pacotes);
    }
}
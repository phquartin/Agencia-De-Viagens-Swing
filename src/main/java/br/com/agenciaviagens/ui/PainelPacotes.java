package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.model.Pacote;
import br.com.agenciaviagens.service.PacoteService;
import br.com.agenciaviagens.ui.tablemodel.PacoteTableModel;
import br.com.agenciaviagens.ui.util.Estilo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelPacotes extends JPanel {

    private PacoteService pacoteService;
    private JTable tabelaPacotes;
    private PacoteTableModel tableModel;

    public PainelPacotes() {
        this.pacoteService = new PacoteService();

        setLayout(new BorderLayout(10, 10));
        setBackground(Estilo.COR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título do Painel
        JLabel labelTitulo = new JLabel("Gerenciamento de Pacotes");
        labelTitulo.setFont(Estilo.FONTE_TITULO);
        labelTitulo.setForeground(Estilo.COR_TEXTO);
        add(labelTitulo, BorderLayout.NORTH);

        // Tabela de Pacotes
        tableModel = new PacoteTableModel();
        tabelaPacotes = new JTable(tableModel);
        tabelaPacotes.setFont(Estilo.FONTE_CORPO);
        tabelaPacotes.getTableHeader().setFont(Estilo.FONTE_SUBTITULO);
        JScrollPane scrollPane = new JScrollPane(tabelaPacotes);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBackground(Estilo.COR_BACKGROUND);

        JButton btnNovo = new JButton("Novo Pacote");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        // Estilizando os botões
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

        // Carrega os dados do banco de dados na tabela
        carregarDadosTabela();

        // As ações dos botões serão implementadas no próximo passo
    }

    private void carregarDadosTabela() {
        List<Pacote> pacotes = pacoteService.listarTodos();
        tableModel.setPacotes(pacotes);
    }
}
package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.model.Cliente;
import br.com.agenciaviagens.service.ClienteService;
import br.com.agenciaviagens.ui.tablemodel.ClienteTableModel;
import br.com.agenciaviagens.ui.util.Estilo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelClientes extends JPanel {

    private final ClienteService clienteService;
    private final JTable tabelaClientes;
    private final ClienteTableModel tableModel;

    public PainelClientes() {
        this.clienteService = new ClienteService();

        setLayout(new BorderLayout(10, 10));
        setBackground(Estilo.COR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel labelTitulo = new JLabel("Gerenciamento de Clientes");
        labelTitulo.setFont(Estilo.FONTE_TITULO);
        labelTitulo.setForeground(Estilo.COR_TEXTO);
        add(labelTitulo, BorderLayout.NORTH);

        // Tabela
        tableModel = new ClienteTableModel();
        tabelaClientes = new JTable(tableModel);
        tabelaClientes.setFont(Estilo.FONTE_CORPO);
        tabelaClientes.getTableHeader().setFont(Estilo.FONTE_SUBTITULO);
        tabelaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBackground(Estilo.COR_BACKGROUND);

        JButton btnNovo = new JButton("Novo Cliente");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        // Estilos dos botões
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

        // Ações dos botões serão implementadas no próximo passo
    }

    private void carregarDadosTabela() {
        List<Cliente> clientes = clienteService.listarTodos();
        tableModel.setClientes(clientes);
    }
}
package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.ui.util.Estilo;
import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel painelPrincipal;

    public TelaPrincipal() {
        super("Sistema de Agência de Viagens");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // --- Barra de Menu ---
        JMenuBar menuBar = new JMenuBar();
        JMenu menuCadastro = new JMenu("Cadastros");

        JMenuItem itemPacotes = new JMenuItem("Pacotes");
        JMenuItem itemClientes = new JMenuItem("Clientes");
        JMenuItem itemServicos = new JMenuItem("Serviços Adicionais");

        menuCadastro.add(itemPacotes);
        menuCadastro.add(itemClientes);
        menuCadastro.add(itemServicos);
        menuBar.add(menuCadastro);
        setJMenuBar(menuBar);

        // --- Painel Principal com CardLayout ---
        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);
        painelPrincipal.setBackground(Estilo.COR_BACKGROUND);

        JPanel painelBoasVindas = new JPanel(new GridBagLayout());
        painelBoasVindas.setBackground(Estilo.COR_BACKGROUND);
        JLabel labelBoasVindas = new JLabel("Bem-vindo ao Sistema!");
        labelBoasVindas.setFont(Estilo.FONTE_TITULO);
        labelBoasVindas.setForeground(Estilo.COR_TEXTO);
        painelBoasVindas.add(labelBoasVindas);

        // Adiciona os painéis (telas) ao CardLayout
        painelPrincipal.add(painelBoasVindas, "BOAS_VINDAS");
        painelPrincipal.add(new PainelPacotes(), "PACOTES");
        painelPrincipal.add(new PainelClientes(), "CLIENTES");

        add(painelPrincipal, BorderLayout.CENTER);

        // --- Ações do Menu ---
        itemPacotes.addActionListener(e -> cardLayout.show(painelPrincipal, "PACOTES"));
        itemClientes.addActionListener(e -> cardLayout.show(painelPrincipal, "CLIENTES"));

        // Mostra o painel inicial
        cardLayout.show(painelPrincipal, "BOAS_VINDAS");
    }
}
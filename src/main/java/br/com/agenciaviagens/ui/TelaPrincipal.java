package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.ui.util.Estilo;
import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel painelPrincipal;

    public TelaPrincipal() {
        super("Sistema de Agência de Viagens"); // Título da janela

        // Configurações básicas da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // --- Barra de Menu (será adicionada depois) ---

        JMenuBar menuBar = new JMenuBar();
        // ... (código do menu virá aqui)
        setJMenuBar(menuBar);

        // --- Painel Principal com CardLayout ---
        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);
        painelPrincipal.setBackground(Estilo.COR_BACKGROUND);

        // Adicionando um painel de boas-vindas inicial
        JPanel painelBoasVindas = new JPanel(new GridBagLayout());
        painelBoasVindas.setBackground(Estilo.COR_BACKGROUND);
        JLabel labelBoasVindas = new JLabel("Bem-vindo ao Sistema!");
        labelBoasVindas.setFont(Estilo.FONTE_TITULO);
        labelBoasVindas.setForeground(Estilo.COR_TEXTO);
        painelBoasVindas.add(labelBoasVindas);

        // Adiciona os painéis (telas) ao CardLayout com um nome
        painelPrincipal.add(painelBoasVindas, "BOAS_VINDAS");
        // Futuramente: painelPrincipal.add(new PainelPacotes(), "PACOTES");
        // Futuramente: painelPrincipal.add(new PainelClientes(), "CLIENTES");

        // Adiciona o painel principal à janela
        add(painelPrincipal, BorderLayout.CENTER);

        // Mostra o painel inicial
        cardLayout.show(painelPrincipal, "BOAS_VINDAS");
    }
}
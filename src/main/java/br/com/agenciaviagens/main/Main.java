package br.com.agenciaviagens.main;

import br.com.agenciaviagens.factory.DatabaseInitializer;
import br.com.agenciaviagens.ui.TelaPrincipal;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando aplicação...");

        // 1. Inicializa o banco de dados (como antes)
        DatabaseInitializer.initialize();

        // 2. Configura o Look and Feel moderno ANTES de criar qualquer componente Swing.
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Falha ao carregar o Look and Feel.");
        }

        // 3. Inicia a interface gráfica na Thread de Eventos do Swing (EDT)
        // Isso é crucial para garantir que a UI seja segura para threads.
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}
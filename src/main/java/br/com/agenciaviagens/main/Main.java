package br.com.agenciaviagens.main;

import br.com.agenciaviagens.factory.DatabaseInitializer;
import br.com.agenciaviagens.ui.TelaPrincipal;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando aplicação...");

        DatabaseInitializer.initialize();

        // 2. Configura o Look and Feel moderno ANTES de criar qualquer componente Swing.
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Falha ao carregar o Look and Feel.");
        }

        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}
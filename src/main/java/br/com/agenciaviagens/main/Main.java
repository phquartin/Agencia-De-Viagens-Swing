package br.com.agenciaviagens.main;

import br.com.agenciaviagens.factory.DatabaseInitializer;

public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando aplicação...");

        // Passo 1: Inicializar o banco de dados.
        DatabaseInitializer.initialize();

        System.out.println("=====================================================");
        System.out.println("Backend pronto. Aguardando a implementação da interface gráfica.");
        System.out.println("=====================================================");

        // Passo 2: Futuramente, aqui chamaremos a tela principal
        // Ex: SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
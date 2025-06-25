package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.Pacote;
import br.com.agenciaviagens.service.PacoteService;
import br.com.agenciaviagens.ui.util.Estilo;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogoPacote extends JDialog {

    private final PacoteService pacoteService;
    private final Runnable callbackSucesso;
    private Pacote pacoteAtual;

    private JTextField txtNome;
    private JTextField txtDestino;
    private JTextField txtPreco;
    private JTextField txtDataPartida;
    private JTextField txtDataRetorno;

    // O formato de data que vamos esperar do usuário
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public DialogoPacote(Frame owner, PacoteService pacoteService, Pacote pacote, Runnable callbackSucesso) {
        super(owner, true); // true para ser modal
        this.pacoteService = pacoteService;
        this.pacoteAtual = pacote;
        this.callbackSucesso = callbackSucesso;

        configurarLayout();
        preencherDados();

        setTitle(pacoteAtual == null ? "Novo Pacote" : "Editar Pacote");
        pack(); // Ajusta o tamanho da janela aos componentes
        setLocationRelativeTo(owner);
    }

    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels
        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Nome do Pacote:"), gbc);
        gbc.gridy++; painelCampos.add(new JLabel("Destino:"), gbc);
        gbc.gridy++; painelCampos.add(new JLabel("Preço (R$):"), gbc);
        gbc.gridy++; painelCampos.add(new JLabel("Data Partida (dd/mm/aaaa):"), gbc);
        gbc.gridy++; painelCampos.add(new JLabel("Data Retorno (dd/mm/aaaa):"), gbc);

        // Campos de Texto
        txtNome = new JTextField(20);
        txtDestino = new JTextField(20);
        txtPreco = new JTextField(20);
        txtDataPartida = new JTextField(20);
        txtDataRetorno = new JTextField(20);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; painelCampos.add(txtNome, gbc);
        gbc.gridy++; painelCampos.add(txtDestino, gbc);
        gbc.gridy++; painelCampos.add(txtPreco, gbc);
        gbc.gridy++; painelCampos.add(txtDataPartida, gbc);
        gbc.gridy++; painelCampos.add(txtDataRetorno, gbc);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.setBackground(Estilo.COR_PRIMARIA);
        btnSalvar.setForeground(Color.WHITE);
        btnCancelar.setBackground(Estilo.COR_SEGUNDARIA);
        btnCancelar.setForeground(Color.WHITE);

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões do diálogo
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose()); // dispose() fecha a janela
    }

    private void preencherDados() {
        if (pacoteAtual != null) {
            txtNome.setText(pacoteAtual.getNomePacote());
            txtDestino.setText(pacoteAtual.getDestino());
            txtPreco.setText(String.format("%.2f", pacoteAtual.getPreco()).replace(",", "."));
            txtDataPartida.setText(dateFormat.format(pacoteAtual.getDataPartida()));
            txtDataRetorno.setText(dateFormat.format(pacoteAtual.getDataRetorno()));
        }
    }

    private void salvar() {
        if (pacoteAtual == null) {
            pacoteAtual = new Pacote();
        }

        try {
            // Coletar e converter os dados dos campos
            pacoteAtual.setNomePacote(txtNome.getText());
            pacoteAtual.setDestino(txtDestino.getText());
            pacoteAtual.setPreco(Double.parseDouble(txtPreco.getText().replace(",", ".")));

            Date dataPartida = dateFormat.parse(txtDataPartida.getText());
            Date dataRetorno = dateFormat.parse(txtDataRetorno.getText());
            pacoteAtual.setDataPartida(dataPartida);
            pacoteAtual.setDataRetorno(dataRetorno);

            // Chamar a camada de serviço
            pacoteService.salvar(pacoteAtual);

            JOptionPane.showMessageDialog(this, "Pacote salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            callbackSucesso.run(); // Executa o método de atualização da tabela na tela principal
            dispose(); // Fecha o diálogo

        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/mm/aaaa.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O preço deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}
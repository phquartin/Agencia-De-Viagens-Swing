package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.ServicoAdicional;
import br.com.agenciaviagens.service.ServicoAdicionalService;
import br.com.agenciaviagens.ui.util.Estilo;

import javax.swing.*;
import java.awt.*;

public class DialogoServico extends JDialog {

    private final ServicoAdicionalService servicoService;
    private final Runnable callbackSucesso;
    private ServicoAdicional servicoAtual;

    private JTextField txtNome, txtPreco;
    private JTextArea areaDescricao;

    public DialogoServico(Frame owner, ServicoAdicionalService servicoService, ServicoAdicional servico, Runnable callbackSucesso) {
        super(owner, true);
        this.servicoService = servicoService;
        this.servicoAtual = servico;
        this.callbackSucesso = callbackSucesso;

        configurarLayout();
        preencherDados();

        setTitle(servicoAtual == null ? "Novo Serviço" : "Editar Serviço");
        pack();
        setLocationRelativeTo(owner);
    }

    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Nome do Serviço:"), gbc);
        gbc.gridy++; gbc.anchor = GridBagConstraints.NORTHWEST; painelCampos.add(new JLabel("Descrição:"), gbc);
        gbc.gridy++; gbc.anchor = GridBagConstraints.CENTER; painelCampos.add(new JLabel("Preço (R$):"), gbc);

        txtNome = new JTextField(20);
        txtPreco = new JTextField(20);
        areaDescricao = new JTextArea(4, 20);
        areaDescricao.setLineWrap(true);
        areaDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(areaDescricao);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; painelCampos.add(txtNome, gbc);
        gbc.gridy++; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0; painelCampos.add(scrollDescricao, gbc);
        gbc.gridy++; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0.0; painelCampos.add(txtPreco, gbc);

        add(painelCampos, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void preencherDados() {
        if (servicoAtual != null) {
            txtNome.setText(servicoAtual.getNomeServico());
            areaDescricao.setText(servicoAtual.getDescricao());
            txtPreco.setText(String.format("%.2f", servicoAtual.getPreco()).replace(",", "."));
        }
    }

    private void salvar() {
        if (servicoAtual == null) {
            servicoAtual = new ServicoAdicional();
        }
        try {
            servicoAtual.setNomeServico(txtNome.getText());
            servicoAtual.setDescricao(areaDescricao.getText());
            servicoAtual.setPreco(Double.parseDouble(txtPreco.getText().replace(",", ".")));

            servicoService.salvar(servicoAtual);
            JOptionPane.showMessageDialog(this, "Serviço salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            callbackSucesso.run();
            dispose();
        } catch (ValidationException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }
}
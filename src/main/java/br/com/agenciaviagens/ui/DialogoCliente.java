package br.com.agenciaviagens.ui;

import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.Cliente;
import br.com.agenciaviagens.service.ClienteService;
import br.com.agenciaviagens.ui.util.Estilo;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

public class DialogoCliente extends JDialog {

    private final ClienteService clienteService;
    private final Runnable callbackSucesso;
    private Cliente clienteAtual;

    // Campos de Texto
    private JTextField txtNome, txtEmail, txtEndereco, txtPassaporte;
    private JFormattedTextField txtCpf, txtTelefone; // <-- TROCAMOS PARA JFormattedTextField
    private JComboBox<Cliente.TipoCliente> comboTipoCliente;

    public DialogoCliente(Frame owner, ClienteService clienteService, Cliente cliente, Runnable callbackSucesso) {
        super(owner, true);
        this.clienteService = clienteService;
        this.clienteAtual = cliente;
        this.callbackSucesso = callbackSucesso;

        configurarLayout();
        configurarListeners();
        preencherDados();

        setTitle(clienteAtual == null ? "Novo Cliente" : "Editar Cliente");
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

        // Labels
        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Nome:"), gbc);
        gbc.gridy++; painelCampos.add(new JLabel("E-mail:"), gbc);
        gbc.gridy++; painelCampos.add(new JLabel("Telefone:"), gbc);
        gbc.gridy++; painelCampos.add(new JLabel("Endereço:"), gbc);
        gbc.gridy++; painelCampos.add(new JLabel("Tipo:"), gbc);
        gbc.gridy++; painelCampos.add(new JLabel("CPF:"), gbc);
        gbc.gridy++; painelCampos.add(new JLabel("Passaporte:"), gbc);

        // Campos de entrada
        txtNome = new JTextField(25);
        txtEmail = new JTextField(25);
        txtEndereco = new JTextField(25);
        comboTipoCliente = new JComboBox<>(Cliente.TipoCliente.values());
        txtPassaporte = new JTextField(25);

        // --- INÍCIO DA MUDANÇA ---
        // Criação dos campos formatados
        try {
            MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
            mascaraCpf.setPlaceholderCharacter('_');
            txtCpf = new JFormattedTextField(mascaraCpf);

            MaskFormatter mascaraTelefone = new MaskFormatter("(##) #####-####");
            mascaraTelefone.setPlaceholderCharacter('_');
            txtTelefone = new JFormattedTextField(mascaraTelefone);

        } catch (ParseException e) {
            // Em caso de erro na máscara, usa campos normais como fallback
            e.printStackTrace();
            txtCpf = new JFormattedTextField();
            txtTelefone = new JFormattedTextField();
        }
        // --- FIM DA MUDANÇA ---

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; painelCampos.add(txtNome, gbc);
        gbc.gridy++; painelCampos.add(txtEmail, gbc);
        gbc.gridy++; painelCampos.add(txtTelefone, gbc); // <-- Agora é o campo formatado
        gbc.gridy++; painelCampos.add(txtEndereco, gbc);
        gbc.gridy++; painelCampos.add(comboTipoCliente, gbc);
        gbc.gridy++; painelCampos.add(txtCpf, gbc); // <-- Agora é o campo formatado
        gbc.gridy++; painelCampos.add(txtPassaporte, gbc);

        // (O restante do layout de botões permanece o mesmo)
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        btnSalvar.setBackground(Estilo.COR_PRIMARIA); btnSalvar.setForeground(Color.WHITE);
        btnCancelar.setBackground(Estilo.COR_SEGUNDARIA); btnCancelar.setForeground(Color.WHITE);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void configurarListeners() {
        comboTipoCliente.addActionListener(e -> atualizarCamposDocumento());
    }

    private void preencherDados() {
        if (clienteAtual != null) {
            txtNome.setText(clienteAtual.getNome());
            txtEmail.setText(clienteAtual.getEmail());
            txtTelefone.setText(clienteAtual.getTelefone()); // O JFormattedTextField vai tentar encaixar o valor na máscara
            txtEndereco.setText(clienteAtual.getEndereco());
            comboTipoCliente.setSelectedItem(clienteAtual.getTipo());
            txtCpf.setText(clienteAtual.getCpf()); // O JFormattedTextField vai tentar encaixar o valor na máscara
            txtPassaporte.setText(clienteAtual.getPassaporte());
        }
        atualizarCamposDocumento();
    }

    // Método para salvar os dados (agora pega os dados dos campos formatados)
    private void salvar() {
        if (clienteAtual == null) {
            clienteAtual = new Cliente();
        }

        try {
            clienteAtual.setNome(txtNome.getText());
            clienteAtual.setEmail(txtEmail.getText());
            clienteAtual.setTelefone(txtTelefone.getText()); // Pega o texto formatado
            clienteAtual.setEndereco(txtEndereco.getText());
            clienteAtual.setTipo((Cliente.TipoCliente) comboTipoCliente.getSelectedItem());
            clienteAtual.setCpf(txtCpf.getText()); // Pega o texto formatado
            clienteAtual.setPassaporte(txtPassaporte.getText());

            clienteService.salvar(clienteAtual);

            JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            callbackSucesso.run();
            dispose();

        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    // O método atualizarCamposDocumento permanece o mesmo
    private void atualizarCamposDocumento() {
        Cliente.TipoCliente tipoSelecionado = (Cliente.TipoCliente) comboTipoCliente.getSelectedItem();
        if (tipoSelecionado == Cliente.TipoCliente.NACIONAL) {
            txtCpf.setEnabled(true);
            txtPassaporte.setEnabled(false);
            if (clienteAtual == null || clienteAtual.getTipo() != Cliente.TipoCliente.NACIONAL) {
                txtPassaporte.setText("");
            }
        } else {
            txtCpf.setEnabled(false);
            if (clienteAtual == null || clienteAtual.getTipo() != Cliente.TipoCliente.ESTRANGEIRO) {
                txtCpf.setText("");
            }
            txtPassaporte.setEnabled(true);
        }
    }
}
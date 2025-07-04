package br.com.agenciaviagens.service;

import br.com.agenciaviagens.dao.ClienteDAO;
import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.Cliente;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class ClienteService {

    private final ClienteDAO clienteDAO;

    // REGEX para validar o formato de e-mail.
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
    }

    public void salvar(Cliente cliente) throws ValidationException {
        // 1. Executar todas as validações
        validarCliente(cliente);

        try {
            clienteDAO.save(cliente);
        } catch (SQLException e) {
            // O código '1062' é o erro padrão do MySQL para 'Duplicate entry'
            if (e.getErrorCode() == 1062) {
                throw new ValidationException("Já existe um cliente com o mesmo E-mail ou CPF/Passaporte.");
            }
            throw new ValidationException("Ocorreu um erro técnico ao salvar o cliente. Detalhes: " + e.getMessage());
        }
    }

    // Em ClienteService.java

    private void validarCliente(Cliente cliente) throws ValidationException {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new ValidationException("O nome do cliente é obrigatório.");
        }

        if (!cliente.getNome().matches("[a-zA-Z\\s]+")) {
            throw new ValidationException("O nome deve conter apenas letras e espaços.");
        }

        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new ValidationException("O e-mail do cliente é obrigatório.");
        }
        if (!EMAIL_PATTERN.matcher(cliente.getEmail()).matches()) {
            throw new ValidationException("O formato do e-mail é inválido.");
        }
        if (cliente.getTipo() == null) {
            throw new ValidationException("O tipo de cliente (Nacional/Estrangeiro) é obrigatório.");
        }

        if (cliente.getTipo() == Cliente.TipoCliente.NACIONAL) {
            if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty() || cliente.getCpf().contains("_")) {
                throw new ValidationException("O CPF é obrigatório e deve ser preenchido completamente para clientes nacionais.");
            }
            cliente.setPassaporte(null);
        } else { // Se for ESTRANGEIRO
            if (cliente.getPassaporte() == null || cliente.getPassaporte().trim().isEmpty()) {
                throw new ValidationException("O Passaporte é obrigatório para clientes estrangeiros.");
            }
            // NOVA VALIDAÇÃO: Passaporte com regex
            if (!cliente.getPassaporte().matches("^[A-Z0-9]{6,9}$")) {
                throw new ValidationException("O passaporte deve conter de 6 a 9 caracteres, apenas letras maiúsculas e números.");
            }
            cliente.setCpf(null);
        }
    }

    public void excluir(int id) throws ValidationException {
        if (clienteDAO.findById(id) == null) {
            throw new ValidationException("Cliente não encontrado para exclusão.");
        }
        try {
            clienteDAO.deleteById(id);
        } catch (SQLException e) {
            throw new ValidationException("Não é possível excluir um cliente que já possui pacotes contratados.");
        }
    }

    public List<Cliente> listarTodos() {
        return clienteDAO.findAll();
    }

    public Cliente buscarPorId(int id) {
        return clienteDAO.findById(id);
    }
}
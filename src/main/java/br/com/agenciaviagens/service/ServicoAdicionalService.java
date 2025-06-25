package br.com.agenciaviagens.service;

import br.com.agenciaviagens.dao.ServicoAdicionalDAO;
import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.ServicoAdicional;

import java.sql.SQLException;
import java.util.List;

public class ServicoAdicionalService {

    private final ServicoAdicionalDAO servicoDAO;

    public ServicoAdicionalService() {
        this.servicoDAO = new ServicoAdicionalDAO();
    }

    public void salvar(ServicoAdicional servico) throws ValidationException {
        if (servico.getNomeServico() == null || servico.getNomeServico().trim().isEmpty()) {
            throw new ValidationException("O nome do serviço é obrigatório.");
        }
        if (servico.getPreco() <= 0) {
            throw new ValidationException("O preço do serviço deve ser um valor positivo.");
        }

        try {
                servicoDAO.save(servico);
        } catch (SQLException e) {
            throw new ValidationException("Erro técnico ao salvar o serviço: " + e.getMessage());
        }
    }

    public void excluir(int id) throws ValidationException {
        if (servicoDAO.findById(id) == null) {
            throw new ValidationException("Serviço não encontrado para exclusão.");
        }
        try {
            servicoDAO.deleteById(id);
        } catch (SQLException e) {
            throw new ValidationException("Não é possível excluir um serviço que já está associado a uma contratação.");
        }
    }

    public List<ServicoAdicional> listarTodos() {
        return servicoDAO.findAll();
    }

    public ServicoAdicional buscarPorId(int id) {
        return servicoDAO.findById(id);
    }
}
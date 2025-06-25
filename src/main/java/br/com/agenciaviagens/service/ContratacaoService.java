package br.com.agenciaviagens.service;

import br.com.agenciaviagens.dao.ContratacaoDAO;
import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.Contratacao;
import java.sql.SQLException;
import java.util.List;

public class ContratacaoService {
    private final ContratacaoDAO contratacaoDAO;

    public ContratacaoService() {
        this.contratacaoDAO = new ContratacaoDAO();
    }

    public void salvar(Contratacao contratacao) throws ValidationException {
        if (contratacao.getCliente() == null) {
            throw new ValidationException("Um cliente deve ser selecionado.");
        }
        if (contratacao.getPacote() == null) {
            throw new ValidationException("Um pacote deve ser selecionado.");
        }
        try {
            contratacaoDAO.save(contratacao);
        } catch (SQLException e) {
            throw new ValidationException("Erro técnico ao salvar a contratação: " + e.getMessage());
        }
    }
    public List<Contratacao> buscarContratacoesPorCliente(int clienteId) throws ValidationException {
        try {
            return contratacaoDAO.findByClienteId(clienteId);
        } catch (SQLException e) {
            throw new ValidationException("Erro técnico ao consultar as contratações: " + e.getMessage());
        }
    }
    public List<Contratacao> buscarContratacoesPorPacote(int pacoteId) throws ValidationException {
        try {
            return contratacaoDAO.findByPacoteId(pacoteId);
        } catch (SQLException e) {
            throw new ValidationException("Erro técnico ao consultar as contratações: " + e.getMessage());
        }
    }
}
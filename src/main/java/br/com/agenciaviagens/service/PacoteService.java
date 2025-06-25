package br.com.agenciaviagens.service;

import br.com.agenciaviagens.dao.PacoteDAO;
import br.com.agenciaviagens.exception.ValidationException;
import br.com.agenciaviagens.model.Pacote;

import java.sql.SQLException;
import java.util.List;

public class PacoteService {

    private final PacoteDAO pacoteDAO;

    public PacoteService() {
        this.pacoteDAO = new PacoteDAO();
    }

    /**
     * Salva um pacote, aplicando todas as validações de negócio.
     *
     * @param pacote O objeto Pacote a ser salvo
     * @throws ValidationException Se alguma regra de negócio for violada.
     */
    public void salvar(Pacote pacote) throws ValidationException {
        // Validações de Negócio
        if (pacote.getNomePacote() == null || pacote.getNomePacote().trim().isEmpty()) {
            throw new ValidationException("O nome do pacote é obrigatório.");
        }
        if (pacote.getDestino() == null || pacote.getDestino().trim().isEmpty()) {
            throw new ValidationException("O destino do pacote é obrigatório.");
        }
        if (pacote.getPreco() <= 0) {
            throw new ValidationException("O preço do pacote deve ser um valor positivo.");
        }
        if (pacote.getDataPartida() == null) {
            throw new ValidationException("A data de partida é obrigatória.");
        }
        if (pacote.getDataRetorno() == null) {
            throw new ValidationException("A data de retorno é obrigatória.");
        }
        if (pacote.getDataRetorno().before(pacote.getDataPartida())) {
            throw new ValidationException("A data de retorno não pode ser anterior à data de partida.");
        }
        try {
            pacoteDAO.save(pacote);
        } catch (SQLException e) {
            throw new ValidationException("Erro ao Salvar pacote.");
        }
    }

    /**
     * Exclui um pacote, tratando o erro de integridade de dados.
     *
     * @param id O ID do pacote a ser excluído.
     * @throws ValidationException Se o pacote não puder ser excluído.
     */
    public void excluir(int id) throws ValidationException {
        // Primeiro, verificamos se o pacote existe antes de tentar excluir.
        if (pacoteDAO.findById(id) == null) {
            throw new ValidationException("Pacote não encontrado para exclusão.");
        }
        try {
            pacoteDAO.deleteById(id);
        } catch (SQLException e) {
            // Se o DAO lançar SQLException na exclusão, transformamos em uma mensagem amigável.
            throw new ValidationException("Não é possível excluir um pacote que já está associado a uma contratação de cliente.");
        }
    }

    /**
     * Lista todos os pacotes.
     * @return uma lista de todos os pacotes.
     */
    public List<Pacote> listarTodos() {
        return pacoteDAO.findAll();
    }

    /**
     * Busca um pacote específico pelo seu ID.
     * @param id O ID do pacote.
     * @return O pacote encontrado, ou null se não existir.
     */
    public Pacote buscarPorId(int id) {
        return pacoteDAO.findById(id);
    }
}
package br.com.agenciaviagens.model;

import java.util.Date;
import java.util.List;

public class Contratacao {
    private int id;
    private Cliente cliente;
    private Pacote pacote;
    private Date dataContratacao;
    private List<ServicoAdicional> servicosAdicionais;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Pacote getPacote() { return pacote; }
    public void setPacote(Pacote pacote) { this.pacote = pacote; }
    public Date getDataContratacao() { return dataContratacao; }
    public void setDataContratacao(Date dataContratacao) { this.dataContratacao = dataContratacao; }
    public List<ServicoAdicional> getServicosAdicionais() { return servicosAdicionais; }
    public void setServicosAdicionais(List<ServicoAdicional> servicosAdicionais) { this.servicosAdicionais = servicosAdicionais; }
}
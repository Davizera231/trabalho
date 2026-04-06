package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import state.EstadoProposta;
import state.EstadoRascunho;

public class Proposta {

    private int id;
    private String codigo;
    private String titulo;
    private String descricao;
    private double valor;
    private String status;
    private String etapaAtual;
    private Date dataCriacao;
    private Date dataAtualizacao;
    private String observacoes;
    private Cliente cliente;
    private List<Documento> documentos;

    // Campo do State Pattern — não persiste no banco
    private EstadoProposta estado;

    public static final String STATUS_RASCUNHO  = "RASCUNHO";
    public static final String STATUS_ANALISE   = "ANALISE";
    public static final String STATUS_APROVADA  = "APROVADA";
    public static final String STATUS_REPROVADA = "REPROVADA";

    private Proposta() {}

    // ── Getters ──────────────────────────────────────
    public int getId()                    { return id; }
    public String getCodigo()             { return codigo; }
    public String getTitulo()             { return titulo; }
    public String getDescricao()          { return descricao; }
    public double getValor()              { return valor; }
    public String getStatus()             { return status; }
    public String getEtapaAtual()         { return etapaAtual; }
    public Date getDataCriacao()          { return dataCriacao; }
    public Date getDataAtualizacao()      { return dataAtualizacao; }
    public String getObservacoes()        { return observacoes; }
    public Cliente getCliente()           { return cliente; }
    public List<Documento> getDocumentos(){ return documentos; }
    public EstadoProposta getEstado()     { return estado; }

    // ── Setters ──────────────────────────────────────
    public void setId(int id)                          { this.id = id; }
    public void setCodigo(String codigo)               { this.codigo = codigo; }
    public void setTitulo(String titulo)               { this.titulo = titulo; }
    public void setDescricao(String descricao)         { this.descricao = descricao; }
    public void setValor(double valor)                 { this.valor = valor; }
    public void setStatus(String status)               { this.status = status; }
    public void setEtapaAtual(String etapaAtual)       { this.etapaAtual = etapaAtual; }
    public void setDataCriacao(Date dataCriacao)       { this.dataCriacao = dataCriacao; }
    public void setDataAtualizacao(Date d)             { this.dataAtualizacao = d; }
    public void setObservacoes(String observacoes)     { this.observacoes = observacoes; }
    public void setCliente(Cliente cliente)            { this.cliente = cliente; }
    public void setDocumentos(List<Documento> docs)    { this.documentos = docs; }

    // ── State Pattern ─────────────────────────────────
    public void setEstado(EstadoProposta estado) {
        this.estado     = estado;
        this.status     = estado.getNome();
        this.etapaAtual = estado.getEtapa();
    }

    public void avancar() {
        if (estado == null) estado = new EstadoRascunho();
        estado.avancar(this);
        this.dataAtualizacao = new Date();
    }

    public void reprovar() {
        if (estado == null) throw new IllegalStateException("Estado não definido.");
        estado.reprovar(this);
        this.dataAtualizacao = new Date();
    }

    public void reabrir() {
        if (estado == null) throw new IllegalStateException("Estado não definido.");
        estado.reabrir(this);
        this.dataAtualizacao = new Date();
    }

    // ── Auxiliar ──────────────────────────────────────
    public void adicionarDocumento(Documento documento) {
        if (this.documentos == null) this.documentos = new ArrayList<>();
        this.documentos.add(documento);
    }

    @Override
    public String toString() {
        return "Proposta{id=" + id + ", codigo='" + codigo +
               "', titulo='" + titulo + "', status='" + status + "'}";
    }

    // ── BUILDER ───────────────────────────────────────
    public static class Builder {
        private final Proposta proposta;

        public Builder() {
            this.proposta = new Proposta();
            this.proposta.status          = STATUS_RASCUNHO;
            this.proposta.etapaAtual      = "CADASTRO";
            this.proposta.dataCriacao     = new Date();
            this.proposta.dataAtualizacao = new Date();
            this.proposta.documentos      = new ArrayList<>();
            this.proposta.estado          = new EstadoRascunho();
        }

        public Builder id(int id)                    { proposta.id = id; return this; }
        public Builder codigo(String codigo)         { proposta.codigo = codigo; return this; }
        public Builder titulo(String titulo)         { proposta.titulo = titulo; return this; }
        public Builder descricao(String descricao)   { proposta.descricao = descricao; return this; }
        public Builder valor(double valor)           { proposta.valor = valor; return this; }
        public Builder status(String status)         { proposta.status = status; return this; }
        public Builder etapaAtual(String e)          { proposta.etapaAtual = e; return this; }
        public Builder dataCriacao(Date d)           { proposta.dataCriacao = d; return this; }
        public Builder dataAtualizacao(Date d)       { proposta.dataAtualizacao = d; return this; }
        public Builder observacoes(String o)         { proposta.observacoes = o; return this; }
        public Builder cliente(Cliente cliente)      { proposta.cliente = cliente; return this; }
        public Builder documentos(List<Documento> d) { proposta.documentos = d; return this; }

        public Proposta build() {
            if (proposta.titulo == null || proposta.titulo.isBlank())
                throw new IllegalStateException("Título da proposta é obrigatório.");
            if (proposta.valor <= 0)
                throw new IllegalStateException("Valor deve ser maior que zero.");
            if (proposta.cliente == null)
                throw new IllegalStateException("Proposta deve ter um cliente vinculado.");
            return proposta;
        }
    }
}
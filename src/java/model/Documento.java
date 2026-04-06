package model;

import java.util.Date;

public class Documento {
    private int id;
    private String nome;
    private String tipo;       // CONTRATO, ANEXO, LAUDO, etc.
    private String caminho;    // caminho do arquivo no servidor
    private String descricao;
    private Date dataUpload;
    private int propostaId;    // chave estrangeira para Proposta (1:N)

    private Documento() {}

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public String getCaminho() { return caminho; }
    public String getDescricao() { return descricao; }
    public Date getDataUpload() { return dataUpload; }
    public int getPropostaId() { return propostaId; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setCaminho(String caminho) { this.caminho = caminho; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setDataUpload(Date dataUpload) { this.dataUpload = dataUpload; }
    public void setPropostaId(int propostaId) { this.propostaId = propostaId; }

    @Override
    public String toString() {
        return "Documento{id=" + id + ", nome='" + nome + "', tipo='" + tipo + "'}";
    }

    // ===================== BUILDER =====================
    public static class Builder {
        private final Documento documento;

        public Builder() {
            this.documento = new Documento();
            this.documento.dataUpload = new Date(); // padrão = agora
        }

        public Builder id(int id) { documento.id = id; return this; }
        public Builder nome(String nome) { documento.nome = nome; return this; }
        public Builder tipo(String tipo) { documento.tipo = tipo; return this; }
        public Builder caminho(String caminho) { documento.caminho = caminho; return this; }
        public Builder descricao(String descricao) { documento.descricao = descricao; return this; }
        public Builder dataUpload(Date dataUpload) { documento.dataUpload = dataUpload; return this; }
        public Builder propostaId(int propostaId) { documento.propostaId = propostaId; return this; }

        public Documento build() {
            if (documento.nome == null || documento.nome.isBlank())
                throw new IllegalStateException("Nome do documento é obrigatório.");
            if (documento.propostaId <= 0)
                throw new IllegalStateException("Documento deve estar vinculado a uma proposta.");
            return documento;
        }
    }
}
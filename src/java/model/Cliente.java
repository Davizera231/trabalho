package model;

public class Cliente {
    private int id;
    private String nome;
    private String cpfCnpj;
    private String email;
    private String telefone;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private String tipo; // PESSOA_FISICA ou PESSOA_JURIDICA
    private boolean ativo;

    // Construtor privado — uso obrigatório via Builder
    private Cliente() {}

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCpfCnpj() { return cpfCnpj; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getEndereco() { return endereco; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public String getCep() { return cep; }
    public String getTipo() { return tipo; }
    public boolean isAtivo() { return ativo; }

    // Setters (necessários para o DAO popular o objeto)
    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setCep(String cep) { this.cep = cep; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nome='" + nome + "', cpfCnpj='" + cpfCnpj + "'}";
    }

    // ===================== BUILDER =====================
    public static class Builder {
        private final Cliente cliente;

        public Builder() {
            this.cliente = new Cliente();
            this.cliente.ativo = true; // padrão
        }

        public Builder id(int id) { cliente.id = id; return this; }
        public Builder nome(String nome) { cliente.nome = nome; return this; }
        public Builder cpfCnpj(String cpfCnpj) { cliente.cpfCnpj = cpfCnpj; return this; }
        public Builder email(String email) { cliente.email = email; return this; }
        public Builder telefone(String telefone) { cliente.telefone = telefone; return this; }
        public Builder endereco(String endereco) { cliente.endereco = endereco; return this; }
        public Builder cidade(String cidade) { cliente.cidade = cidade; return this; }
        public Builder estado(String estado) { cliente.estado = estado; return this; }
        public Builder cep(String cep) { cliente.cep = cep; return this; }
        public Builder tipo(String tipo) { cliente.tipo = tipo; return this; }
        public Builder ativo(boolean ativo) { cliente.ativo = ativo; return this; }

        public Cliente build() {
            if (cliente.nome == null || cliente.nome.isBlank())
                throw new IllegalStateException("Nome do cliente é obrigatório.");
            if (cliente.cpfCnpj == null || cliente.cpfCnpj.isBlank())
                throw new IllegalStateException("CPF/CNPJ é obrigatório.");
            return cliente;
        }
    }
}
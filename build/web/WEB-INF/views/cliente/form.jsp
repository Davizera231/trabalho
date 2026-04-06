<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Cliente" %>
<%@ include file="../fragments/header.jsp" %>

<% Cliente c = (Cliente) request.getAttribute("cliente");
   boolean editando = c != null; %>

<div class="card shadow-sm" style="max-width:700px; margin:auto;">
    <div class="card-header bg-primary text-white">
        <h5 class="mb-0"><i class="bi bi-person-plus"></i> <%= editando ? "Editar Cliente" : "Novo Cliente" %></h5>
    </div>
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/cliente" method="post">
            <input type="hidden" name="acao" value="<%= editando ? "atualizar" : "inserir" %>">
            <% if (editando) { %><input type="hidden" name="id" value="<%= c.getId() %>"><% } %>

            <div class="row g-3">
                <div class="col-md-8">
                    <label class="form-label fw-bold">Nome *</label>
                    <input type="text" name="nome" class="form-control" required
                           value="<%= editando ? c.getNome() : "" %>">
                </div>
                <div class="col-md-4">
                    <label class="form-label fw-bold">Tipo *</label>
                    <select name="tipo" class="form-select" required>
                        <option value="PESSOA_FISICA"  <%= editando && "PESSOA_FISICA".equals(c.getTipo())  ? "selected" : "" %>>Pessoa Física</option>
                        <option value="PESSOA_JURIDICA" <%= editando && "PESSOA_JURIDICA".equals(c.getTipo()) ? "selected" : "" %>>Pessoa Jurídica</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label fw-bold">CPF/CNPJ *</label>
                    <input type="text" name="cpfCnpj" class="form-control" required
                           value="<%= editando ? c.getCpfCnpj() : "" %>">
                </div>
                <div class="col-md-6">
                    <label class="form-label fw-bold">E-mail</label>
                    <input type="email" name="email" class="form-control"
                           value="<%= editando && c.getEmail() != null ? c.getEmail() : "" %>">
                </div>
                <div class="col-md-6">
                    <label class="form-label fw-bold">Telefone</label>
                    <input type="text" name="telefone" class="form-control"
                           value="<%= editando && c.getTelefone() != null ? c.getTelefone() : "" %>">
                </div>
                <div class="col-md-6">
                    <label class="form-label fw-bold">CEP</label>
                    <input type="text" name="cep" class="form-control"
                           value="<%= editando && c.getCep() != null ? c.getCep() : "" %>">
                </div>
                <div class="col-12">
                    <label class="form-label fw-bold">Endereço</label>
                    <input type="text" name="endereco" class="form-control"
                           value="<%= editando && c.getEndereco() != null ? c.getEndereco() : "" %>">
                </div>
                <div class="col-md-8">
                    <label class="form-label fw-bold">Cidade</label>
                    <input type="text" name="cidade" class="form-control"
                           value="<%= editando && c.getCidade() != null ? c.getCidade() : "" %>">
                </div>
                <div class="col-md-4">
                    <label class="form-label fw-bold">Estado (UF)</label>
                    <input type="text" name="estado" maxlength="2" class="form-control"
                           value="<%= editando && c.getEstado() != null ? c.getEstado() : "" %>">
                </div>
            </div>

            <div class="d-flex gap-2 mt-4">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-lg"></i> <%= editando ? "Atualizar" : "Cadastrar" %>
                </button>
                <a href="${pageContext.request.contextPath}/cliente?acao=listar" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left"></i> Voltar
                </a>
            </div>
        </form>
    </div>
</div>

<%@ include file="../fragments/footer.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Proposta, model.Cliente, java.util.List" %>
<%@ include file="../fragments/header.jsp" %>

<%
    Proposta proposta  = (Proposta) request.getAttribute("proposta");
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    boolean editando   = proposta != null;
    String acaoForm    = editando ? "atualizar" : "inserir";
    String tituloPage  = editando ? "Editar Proposta" : "Nova Proposta";
%>

<div class="card shadow-sm" style="max-width:700px; margin:auto;">
    <div class="card-header bg-primary text-white">
        <h5 class="mb-0"><i class="bi bi-file-earmark-plus"></i> <%= tituloPage %></h5>
    </div>
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/proposta" method="post">
            <input type="hidden" name="acao" value="<%= acaoForm %>">
            <% if (editando) { %>
                <input type="hidden" name="id" value="<%= proposta.getId() %>">
            <% } %>

            <div class="mb-3">
                <label class="form-label fw-bold">Cliente *</label>
                <select name="clienteId" class="form-select" required>
                    <option value="">Selecione...</option>
                    <% for (Cliente c : clientes) {
                        boolean sel = editando && proposta.getCliente().getId() == c.getId(); %>
                        <option value="<%= c.getId() %>" <%= sel ? "selected" : "" %>>
                            <%= c.getNome() %> — <%= c.getCpfCnpj() %>
                        </option>
                    <% } %>
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label fw-bold">Título *</label>
                <input type="text" name="titulo" class="form-control" required
                       value="<%= editando ? proposta.getTitulo() : "" %>"
                       placeholder="Ex: Proposta de Desenvolvimento Web">
            </div>

            <div class="mb-3">
                <label class="form-label fw-bold">Descrição</label>
                <textarea name="descricao" class="form-control" rows="3"
                          placeholder="Descreva o escopo da proposta..."><%= editando ? proposta.getDescricao() : "" %></textarea>
            </div>

            <div class="mb-3">
                <label class="form-label fw-bold">Valor (R$) *</label>
                <input type="number" name="valor" class="form-control" step="0.01" min="0.01" required
                       value="<%= editando ? proposta.getValor() : "" %>"
                       placeholder="Ex: 15000.00">
            </div>

            <div class="mb-3">
                <label class="form-label fw-bold">Observações</label>
                <textarea name="observacoes" class="form-control" rows="2"
                          placeholder="Observações adicionais..."><%= editando ? (proposta.getObservacoes() != null ? proposta.getObservacoes() : "") : "" %></textarea>
            </div>

            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-lg"></i> <%= editando ? "Atualizar" : "Cadastrar" %>
                </button>
                <a href="${pageContext.request.contextPath}/proposta?acao=listar" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left"></i> Voltar
                </a>
            </div>
        </form>
    </div>
</div>

<%@ include file="../fragments/footer.jsp" %>
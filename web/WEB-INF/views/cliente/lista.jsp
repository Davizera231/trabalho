<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Cliente, java.util.List" %>
<%@ include file="../fragments/header.jsp" %>

<% List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes"); %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4><i class="bi bi-people"></i> Clientes</h4>
    <a href="${pageContext.request.contextPath}/cliente?acao=novo" class="btn btn-primary">
        <i class="bi bi-plus-lg"></i> Novo Cliente
    </a>
</div>

<div class="card shadow-sm">
    <div class="card-body p-0">
        <table class="table table-hover mb-0">
            <thead class="table-light">
                <tr>
                    <th>Nome</th>
                    <th>CPF/CNPJ</th>
                    <th>E-mail</th>
                    <th>Cidade/UF</th>
                    <th>Tipo</th>
                    <th>Ativo</th>
                    <th class="text-center">Ações</th>
                </tr>
            </thead>
            <tbody>
            <% if (clientes == null || clientes.isEmpty()) { %>
                <tr><td colspan="7" class="text-center text-muted py-4">Nenhum cliente cadastrado.</td></tr>
            <% } else {
                for (Cliente c : clientes) { %>
                <tr>
                    <td><strong><%= c.getNome() %></strong></td>
                    <td><%= c.getCpfCnpj() %></td>
                    <td><%= c.getEmail() != null ? c.getEmail() : "-" %></td>
                    <td><%= c.getCidade() %> / <%= c.getEstado() %></td>
                    <td><%= c.getTipo() %></td>
                    <td>
                        <span class="badge <%= c.isAtivo() ? "bg-success" : "bg-secondary" %>">
                            <%= c.isAtivo() ? "Sim" : "Não" %>
                        </span>
                    </td>
                    <td class="text-center">
                        <a href="${pageContext.request.contextPath}/cliente?acao=editar&id=<%= c.getId() %>"
                           class="btn btn-sm btn-outline-secondary"><i class="bi bi-pencil"></i></a>
                        <a href="${pageContext.request.contextPath}/cliente?acao=deletar&id=<%= c.getId() %>"
                           class="btn btn-sm btn-outline-danger"
                           onclick="return confirm('Excluir cliente?')"><i class="bi bi-trash"></i></a>
                    </td>
                </tr>
            <% } } %>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="../fragments/footer.jsp" %>
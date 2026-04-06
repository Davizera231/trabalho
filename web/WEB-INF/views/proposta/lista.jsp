<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Proposta, java.util.List" %>
<%@ include file="../fragments/header.jsp" %>

<%
    List<Proposta> propostas = (List<Proposta>) request.getAttribute("propostas");
%>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4><i class="bi bi-file-earmark-text"></i> Propostas</h4>
    <a href="${pageContext.request.contextPath}/proposta?acao=novo" class="btn btn-primary">
        <i class="bi bi-plus-lg"></i> Nova Proposta
    </a>
</div>

<div class="card shadow-sm">
    <div class="card-body p-0">
        <table class="table table-hover mb-0">
            <thead class="table-light">
                <tr>
                    <th>Código</th>
                    <th>Título</th>
                    <th>Cliente</th>
                    <th>Valor</th>
                    <th>Status</th>
                    <th>Data</th>
                    <th class="text-center">Ações</th>
                </tr>
            </thead>
            <tbody>
            <% if (propostas == null || propostas.isEmpty()) { %>
                <tr>
                    <td colspan="7" class="text-center text-muted py-4">
                        Nenhuma proposta cadastrada.
                    </td>
                </tr>
            <% } else {
                for (Proposta p : propostas) { %>
                <tr>
                    <td><strong><%= p.getCodigo() %></strong></td>
                    <td><%= p.getTitulo() %></td>
                    <td><%= p.getCliente().getNome() %></td>
                    <td>R$ <%= String.format("%,.2f", p.getValor()) %></td>
                    <td>
                        <span class="badge badge-<%= p.getStatus() %>">
                            <%= p.getStatus() %>
                        </span>
                    </td>
                    <td><%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getDataCriacao()) %></td>
                    <td class="text-center">
                        <a href="${pageContext.request.contextPath}/proposta?acao=detalhe&id=<%= p.getId() %>"
                           class="btn btn-sm btn-outline-primary" title="Detalhes">
                            <i class="bi bi-eye"></i>
                        </a>
                        <% if ("RASCUNHO".equals(p.getStatus())) { %>
                        <a href="${pageContext.request.contextPath}/proposta?acao=editar&id=<%= p.getId() %>"
                           class="btn btn-sm btn-outline-secondary" title="Editar">
                            <i class="bi bi-pencil"></i>
                        </a>
                        <a href="${pageContext.request.contextPath}/proposta?acao=deletar&id=<%= p.getId() %>"
                           class="btn btn-sm btn-outline-danger" title="Excluir"
                           onclick="return confirm('Excluir esta proposta?')">
                            <i class="bi bi-trash"></i>
                        </a>
                        <% } %>
                    </td>
                </tr>
            <% } } %>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="../fragments/footer.jsp" %>
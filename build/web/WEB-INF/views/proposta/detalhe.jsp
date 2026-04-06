<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Proposta, model.Documento, factory.FabricaComandoEsteira, java.util.List" %>
<%@ include file="../fragments/header.jsp" %>

<%
    Proposta p = (Proposta) request.getAttribute("proposta");
    String[] acoes = FabricaComandoEsteira.acoesDisponiveisPara(p.getStatus());
%>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4><i class="bi bi-file-earmark-text"></i> <%= p.getCodigo() %></h4>
    <a href="${pageContext.request.contextPath}/proposta?acao=listar" class="btn btn-outline-secondary btn-sm">
        <i class="bi bi-arrow-left"></i> Voltar
    </a>
</div>

<%-- Esteira visual --%>
<div class="card shadow-sm mb-4">
    <div class="card-header fw-bold"><i class="bi bi-diagram-3"></i> Esteira</div>
    <div class="card-body">
        <div class="row g-2 text-center">
            <%
                String[] etapas = {"CADASTRO", "ANALISE", "APROVADA"};
                String[] labels = {"1. Cadastro", "2. Análise", "3. Aprovada"};
                String etapaAtual = p.getEtapaAtual();
                String status = p.getStatus();

                for (int i = 0; i < etapas.length; i++) {
                    String cls;
                    if ("REPROVADA".equals(status) && "ANALISE".equals(etapas[i])) {
                        cls = "reprovado";
                    } else if (etapas[i].equals(etapaAtual)) {
                        cls = "ativo";
                    } else if (i < java.util.Arrays.asList(etapas).indexOf(etapaAtual)) {
                        cls = "concluido";
                    } else {
                        cls = "pendente";
                    }
            %>
            <div class="col">
                <div class="esteira-step <%= cls %> p-3 rounded">
                    <div class="fw-bold"><%= labels[i] %></div>
                </div>
            </div>
            <% if (i < etapas.length - 1) { %>
            <div class="col-auto d-flex align-items-center">
                <i class="bi bi-arrow-right fs-4 text-muted"></i>
            </div>
            <% } } %>
            <% if ("REPROVADA".equals(status)) { %>
            <div class="col-auto d-flex align-items-center">
                <i class="bi bi-arrow-right fs-4 text-muted"></i>
            </div>
            <div class="col">
                <div class="esteira-step reprovado p-3 rounded">
                    <div class="fw-bold">Reprovada</div>
                </div>
            </div>
            <% } %>
        </div>
    </div>
</div>

<div class="row g-4">
    <%-- Dados da Proposta --%>
    <div class="col-md-6">
        <div class="card shadow-sm h-100">
            <div class="card-header fw-bold"><i class="bi bi-info-circle"></i> Dados da Proposta</div>
            <div class="card-body">
                <p><strong>Título:</strong> <%= p.getTitulo() %></p>
                <p><strong>Valor:</strong> R$ <%= String.format("%,.2f", p.getValor()) %></p>
                <p><strong>Status:</strong>
                    <span class="badge badge-<%= p.getStatus() %>"><%= p.getStatus() %></span>
                </p>
                <p><strong>Descrição:</strong> <%= p.getDescricao() != null ? p.getDescricao() : "-" %></p>
                <p><strong>Observações:</strong> <%= p.getObservacoes() != null ? p.getObservacoes() : "-" %></p>
                <p><strong>Criada em:</strong>
                    <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(p.getDataCriacao()) %>
                </p>
            </div>
        </div>
    </div>

    <%-- Dados do Cliente --%>
    <div class="col-md-6">
        <div class="card shadow-sm h-100">
            <div class="card-header fw-bold"><i class="bi bi-person"></i> Cliente</div>
            <div class="card-body">
                <p><strong>Nome:</strong> <%= p.getCliente().getNome() %></p>
                <p><strong>CPF/CNPJ:</strong> <%= p.getCliente().getCpfCnpj() %></p>
                <p><strong>E-mail:</strong> <%= p.getCliente().getEmail() %></p>
                <p><strong>Telefone:</strong> <%= p.getCliente().getTelefone() %></p>
                <p><strong>Cidade/UF:</strong>
                    <%= p.getCliente().getCidade() %> / <%= p.getCliente().getEstado() %>
                </p>
                <p><strong>Tipo:</strong> <%= p.getCliente().getTipo() %></p>
            </div>
        </div>
    </div>

    <%-- Documentos 1:N --%>
    <div class="col-12">
        <div class="card shadow-sm">
            <div class="card-header fw-bold"><i class="bi bi-paperclip"></i> Documentos</div>
            <div class="card-body">
                <% List<Documento> docs = p.getDocumentos();
                   if (docs == null || docs.isEmpty()) { %>
                    <p class="text-muted">Nenhum documento vinculado.</p>
                <% } else {
                    for (Documento d : docs) { %>
                    <div class="d-flex align-items-center gap-2 mb-2">
                        <i class="bi bi-file-earmark text-primary fs-5"></i>
                        <span><strong><%= d.getNome() %></strong> — <%= d.getTipo() %></span>
                        <small class="text-muted ms-auto">
                            <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(d.getDataUpload()) %>
                        </small>
                    </div>
                <% } } %>
            </div>
        </div>
    </div>

    <%-- Ações da Esteira --%>
    <% if (acoes.length > 0) { %>
    <div class="col-12">
        <div class="card shadow-sm border-primary">
            <div class="card-header fw-bold text-primary">
                <i class="bi bi-play-circle"></i> Avançar na Esteira
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/esteira" method="post">
                    <input type="hidden" name="propostaId" value="<%= p.getId() %>">

                    <div class="mb-3">
                        <label class="form-label">Observação (opcional)</label>
                        <textarea name="observacao" class="form-control" rows="2"
                                  placeholder="Adicione um comentário sobre esta ação..."></textarea>
                    </div>

                    <div class="d-flex gap-2 flex-wrap">
                        <% for (String acao : acoes) { %>
                            <%
                                String btnClass = "btn-primary";
                                String btnIcon  = "bi-arrow-right-circle";
                                String btnLabel = acao;

                                if ("APROVAR".equals(acao)) {
                                    btnClass = "btn-success"; btnIcon = "bi-check-circle";
                                    btnLabel = "Aprovar Proposta";
                                } else if ("REPROVAR".equals(acao)) {
                                    btnClass = "btn-danger"; btnIcon = "bi-x-circle";
                                    btnLabel = "Reprovar Proposta";
                                } else if ("ENVIAR_ANALISE".equals(acao)) {
                                    btnClass = "btn-primary"; btnIcon = "bi-send";
                                    btnLabel = "Enviar para Análise";
                                } else if ("REABRIR".equals(acao)) {
                                    btnClass = "btn-warning"; btnIcon = "bi-arrow-counterclockwise";
                                    btnLabel = "Reabrir Proposta";
                                }
                            %>
                            <button type="submit" name="acao" value="<%= acao %>"
                                    class="btn <%= btnClass %>"
                                    onclick="return confirm('Confirma a ação: <%= btnLabel %>?')">
                                <i class="bi <%= btnIcon %>"></i> <%= btnLabel %>
                            </button>
                        <% } %>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <% } %>
</div>

<br>
<%@ include file="../fragments/footer.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Esteira de Propostas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .navbar-brand { font-weight: 700; letter-spacing: 1px; }
        .badge-RASCUNHO  { background-color: #6c757d; }
        .badge-ANALISE   { background-color: #0d6efd; }
        .badge-APROVADA  { background-color: #198754; }
        .badge-REPROVADA { background-color: #dc3545; }
        .esteira-step { text-align: center; padding: 10px; border-radius: 8px; }
        .esteira-step.ativo { background: #0d6efd; color: white; }
        .esteira-step.concluido { background: #198754; color: white; }
        .esteira-step.reprovado { background: #dc3545; color: white; }
        .esteira-step.pendente  { background: #e9ecef; color: #6c757d; }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/proposta">
            <i class="bi bi-diagram-3-fill"></i> Esteira de Propostas
        </a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/proposta?acao=listar">
                        <i class="bi bi-file-earmark-text"></i> Propostas
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/cliente?acao=listar">
                        <i class="bi bi-people"></i> Clientes
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <%-- Mensagens de feedback --%>
    <% String sucesso = (String) session.getAttribute("sucesso");
       String erro    = (String) session.getAttribute("erro");
       if (sucesso != null) { session.removeAttribute("sucesso"); %>
        <div class="alert alert-success alert-dismissible fade show">
            <i class="bi bi-check-circle"></i> <%= sucesso %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    <% } if (erro != null) { session.removeAttribute("erro"); %>
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="bi bi-exclamation-triangle"></i> <%= erro %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    <% } %>
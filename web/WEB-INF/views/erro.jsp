<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<div class="alert alert-danger">
    <h5><i class="bi bi-exclamation-triangle"></i> Ocorreu um erro</h5>
    <p><%= request.getAttribute("erro") %></p>
    <a href="${pageContext.request.contextPath}/proposta?acao=listar" class="btn btn-outline-danger btn-sm">
        <i class="bi bi-arrow-left"></i> Voltar ao início
    </a>
</div>
<%@ include file="fragments/footer.jsp" %>
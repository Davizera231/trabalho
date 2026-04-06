package controller;

import command.ComandoEsteira;
import dao.PropostaDAO;
import factory.FabricaComandoEsteira;
import model.Proposta;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/esteira")
public class EsteiraServlet extends HttpServlet {

    private final PropostaDAO propostaDAO = new PropostaDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String acao      = req.getParameter("acao");
        String idStr     = req.getParameter("propostaId");
        String observacao = req.getParameter("observacao");

        try {
            int id = Integer.parseInt(idStr);
            Proposta proposta = propostaDAO.buscarPorId(id);

            if (proposta == null) {
                req.getSession().setAttribute("erro", "Proposta não encontrada.");
                resp.sendRedirect(req.getContextPath() + "/proposta?acao=listar");
                return;
            }

            // Adiciona observação antes de executar se informada
            if (observacao != null && !observacao.isBlank()) {
                proposta.setObservacoes(observacao);
            }

            // Factory cria o comando correto e executa
            ComandoEsteira comando = FabricaComandoEsteira.criar(acao);
            comando.executar(proposta);

            req.getSession().setAttribute("sucesso", comando.getDescricao());

        } catch (IllegalStateException | IllegalArgumentException e) {
            req.getSession().setAttribute("erro", e.getMessage());
        } catch (SQLException e) {
            req.getSession().setAttribute("erro", "Erro no banco de dados: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/proposta?acao=listar");
    }
}
package controller;

import dao.ClienteDAO;
import dao.PropostaDAO;
import model.Cliente;
import model.Proposta;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/proposta")
public class PropostaServlet extends HttpServlet {

    private final PropostaDAO propostaDAO = new PropostaDAO();
    private final ClienteDAO  clienteDAO  = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String acao = req.getParameter("acao");
        if (acao == null) acao = "listar";

        try {
            switch (acao) {
                case "listar":   listar(req, resp);   break;
                case "novo":     novo(req, resp);      break;
                case "editar":   editar(req, resp);    break;
                case "detalhe":  detalhe(req, resp);   break;
                case "deletar":  deletar(req, resp);   break;
                default:         listar(req, resp);
            }
        } catch (SQLException e) {
            req.setAttribute("erro", "Erro no banco de dados: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String acao = req.getParameter("acao");

        try {
            if ("inserir".equals(acao))       inserir(req, resp);
            else if ("atualizar".equals(acao)) atualizar(req, resp);
        } catch (SQLException e) {
            req.setAttribute("erro", "Erro no banco de dados: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }

    // ── Listar todas as propostas ──────────────────────────────────────
    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        List<Proposta> propostas = propostaDAO.buscarTodos();
        req.setAttribute("propostas", propostas);
        req.getRequestDispatcher("/WEB-INF/views/proposta/lista.jsp").forward(req, resp);
    }

    // ── Exibir formulário novo ─────────────────────────────────────────
    private void novo(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        List<Cliente> clientes = clienteDAO.buscarTodos();
        req.setAttribute("clientes", clientes);
        req.setAttribute("proposta", null);
        req.getRequestDispatcher("/WEB-INF/views/proposta/form.jsp").forward(req, resp);
    }

    // ── Exibir formulário editar ───────────────────────────────────────
    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Proposta proposta = propostaDAO.buscarPorId(id);
        List<Cliente> clientes = clienteDAO.buscarTodos();
        req.setAttribute("proposta", proposta);
        req.setAttribute("clientes", clientes);
        req.getRequestDispatcher("/WEB-INF/views/proposta/form.jsp").forward(req, resp);
    }

    // ── Detalhe + esteira ─────────────────────────────────────────────
    private void detalhe(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Proposta proposta = propostaDAO.buscarPorId(id);
        req.setAttribute("proposta", proposta);
        req.getRequestDispatcher("/WEB-INF/views/proposta/detalhe.jsp").forward(req, resp);
    }

    // ── Deletar ───────────────────────────────────────────────────────
    private void deletar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        propostaDAO.deletar(id);
        req.getSession().setAttribute("sucesso", "Proposta excluída com sucesso.");
        resp.sendRedirect(req.getContextPath() + "/proposta?acao=listar");
    }

    // ── Inserir ───────────────────────────────────────────────────────
    private void inserir(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        Cliente cliente = clienteDAO.buscarPorId(
            Integer.parseInt(req.getParameter("clienteId"))
        );

        // Gera código automático: PROP-2026-001
        String codigo = gerarCodigo();

        Proposta proposta = new Proposta.Builder()
            .codigo(codigo)
            .titulo(req.getParameter("titulo"))
            .descricao(req.getParameter("descricao"))
            .valor(Double.parseDouble(req.getParameter("valor").replace(",", ".")))
            .observacoes(req.getParameter("observacoes"))
            .cliente(cliente)
            .build();

        propostaDAO.inserir(proposta);
        req.getSession().setAttribute("sucesso", "Proposta " + codigo + " criada com sucesso.");
        resp.sendRedirect(req.getContextPath() + "/proposta?acao=listar");
    }

    // ── Atualizar ─────────────────────────────────────────────────────
    private void atualizar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(req.getParameter("id"));
        Proposta proposta = propostaDAO.buscarPorId(id);

        // Só permite editar se ainda estiver em RASCUNHO
        if (!Proposta.STATUS_RASCUNHO.equals(proposta.getStatus())) {
            req.getSession().setAttribute("erro", "Só é possível editar propostas em RASCUNHO.");
            resp.sendRedirect(req.getContextPath() + "/proposta?acao=detalhe&id=" + id);
            return;
        }

        Cliente cliente = clienteDAO.buscarPorId(
            Integer.parseInt(req.getParameter("clienteId"))
        );

        proposta.setTitulo(req.getParameter("titulo"));
        proposta.setDescricao(req.getParameter("descricao"));
        proposta.setValor(Double.parseDouble(req.getParameter("valor").replace(",", ".")));
        proposta.setObservacoes(req.getParameter("observacoes"));
        proposta.setCliente(cliente);

        propostaDAO.atualizar(proposta);
        req.getSession().setAttribute("sucesso", "Proposta atualizada com sucesso.");
        resp.sendRedirect(req.getContextPath() + "/proposta?acao=detalhe&id=" + id);
    }

    // ── Gera código sequencial ─────────────────────────────────────────
    private String gerarCodigo() throws SQLException {
        String ano = new SimpleDateFormat("yyyy").format(new Date());
        int total  = propostaDAO.buscarTodos().size() + 1;
        return String.format("PROP-%s-%03d", ano, total);
    }
}
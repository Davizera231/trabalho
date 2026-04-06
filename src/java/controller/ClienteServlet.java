package controller;

import dao.ClienteDAO;
import model.Cliente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/cliente")
public class ClienteServlet extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String acao = req.getParameter("acao");
        if (acao == null) acao = "listar";
        try {
            switch (acao) {
                case "listar":  listar(req, resp);  break;
                case "novo":    novo(req, resp);     break;
                case "editar":  editar(req, resp);   break;
                case "deletar": deletar(req, resp);  break;
                default:        listar(req, resp);
            }
        } catch (SQLException e) {
            req.setAttribute("erro", "Erro: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String acao = req.getParameter("acao");
        try {
            if ("inserir".equals(acao))        inserir(req, resp);
            else if ("atualizar".equals(acao)) atualizar(req, resp);
        } catch (SQLException e) {
            req.setAttribute("erro", "Erro: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        List<Cliente> clientes = clienteDAO.buscarTodos();
        req.setAttribute("clientes", clientes);
        req.getRequestDispatcher("/WEB-INF/views/cliente/lista.jsp").forward(req, resp);
    }

    private void novo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("cliente", null);
        req.getRequestDispatcher("/WEB-INF/views/cliente/form.jsp").forward(req, resp);
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("cliente", clienteDAO.buscarPorId(id));
        req.getRequestDispatcher("/WEB-INF/views/cliente/form.jsp").forward(req, resp);
    }

    private void deletar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        clienteDAO.deletar(Integer.parseInt(req.getParameter("id")));
        req.getSession().setAttribute("sucesso", "Cliente excluído com sucesso.");
        resp.sendRedirect(req.getContextPath() + "/cliente?acao=listar");
    }

    private void inserir(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        Cliente cliente = new Cliente.Builder()
            .nome(req.getParameter("nome"))
            .cpfCnpj(req.getParameter("cpfCnpj"))
            .email(req.getParameter("email"))
            .telefone(req.getParameter("telefone"))
            .endereco(req.getParameter("endereco"))
            .cidade(req.getParameter("cidade"))
            .estado(req.getParameter("estado"))
            .cep(req.getParameter("cep"))
            .tipo(req.getParameter("tipo"))
            .build();
        clienteDAO.inserir(cliente);
        req.getSession().setAttribute("sucesso", "Cliente cadastrado com sucesso.");
        resp.sendRedirect(req.getContextPath() + "/cliente?acao=listar");
    }

    private void atualizar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        Cliente cliente = new Cliente.Builder()
            .id(Integer.parseInt(req.getParameter("id")))
            .nome(req.getParameter("nome"))
            .cpfCnpj(req.getParameter("cpfCnpj"))
            .email(req.getParameter("email"))
            .telefone(req.getParameter("telefone"))
            .endereco(req.getParameter("endereco"))
            .cidade(req.getParameter("cidade"))
            .estado(req.getParameter("estado"))
            .cep(req.getParameter("cep"))
            .tipo(req.getParameter("tipo"))
            .build();
        clienteDAO.atualizar(cliente);
        req.getSession().setAttribute("sucesso", "Cliente atualizado com sucesso.");
        resp.sendRedirect(req.getContextPath() + "/cliente?acao=listar");
    }
}
package dao;

import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO extends AbstractDAO<Cliente> {

    @Override
    public void inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nome, cpf_cnpj, email, telefone, endereco, cidade, estado, cep, tipo, ativo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpfCnpj());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getTelefone());
            ps.setString(5, cliente.getEndereco());
            ps.setString(6, cliente.getCidade());
            ps.setString(7, cliente.getEstado());
            ps.setString(8, cliente.getCep());
            ps.setString(9, cliente.getTipo());
            ps.setBoolean(10, cliente.isAtivo());
            ps.executeUpdate();

            // Recupera o ID gerado pelo banco e seta no objeto
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) cliente.setId(rs.getInt(1));

        } finally {
            fecharConexao(conn);
        }
    }

    @Override
    public void atualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nome=?, cpf_cnpj=?, email=?, telefone=?, endereco=?, "
                   + "cidade=?, estado=?, cep=?, tipo=?, ativo=? WHERE id=?";
        Connection conn = null;
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,  cliente.getNome());
            ps.setString(2,  cliente.getCpfCnpj());
            ps.setString(3,  cliente.getEmail());
            ps.setString(4,  cliente.getTelefone());
            ps.setString(5,  cliente.getEndereco());
            ps.setString(6,  cliente.getCidade());
            ps.setString(7,  cliente.getEstado());
            ps.setString(8,  cliente.getCep());
            ps.setString(9,  cliente.getTipo());
            ps.setBoolean(10, cliente.isAtivo());
            ps.setInt(11,    cliente.getId());
            ps.executeUpdate();
        } finally {
            fecharConexao(conn);
        }
    }

    @Override
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id=?";
        Connection conn = null;
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            fecharConexao(conn);
        }
    }

    @Override
    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE id=?";
        Connection conn = null;
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearCliente(rs);
            return null;
        } finally {
            fecharConexao(conn);
        }
    }

    @Override
    public List<Cliente> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM cliente ORDER BY nome";
        Connection conn = null;
        List<Cliente> lista = new ArrayList<>();
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearCliente(rs));
            return lista;
        } finally {
            fecharConexao(conn);
        }
    }

    // Monta um Cliente a partir do ResultSet usando o Builder
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente.Builder()
            .id(rs.getInt("id"))
            .nome(rs.getString("nome"))
            .cpfCnpj(rs.getString("cpf_cnpj"))
            .email(rs.getString("email"))
            .telefone(rs.getString("telefone"))
            .endereco(rs.getString("endereco"))
            .cidade(rs.getString("cidade"))
            .estado(rs.getString("estado"))
            .cep(rs.getString("cep"))
            .tipo(rs.getString("tipo"))
            .ativo(rs.getBoolean("ativo"))
            .build();
    }
}
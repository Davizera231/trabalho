package dao;

import model.Cliente;
import model.Documento;
import model.Proposta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropostaDAO extends AbstractDAO<Proposta> {

    private final ClienteDAO  clienteDAO  = new ClienteDAO();
    private final DocumentoDAO documentoDAO = new DocumentoDAO();

    @Override
    public void inserir(Proposta proposta) throws SQLException {
        String sql = "INSERT INTO proposta (codigo, titulo, descricao, valor, status, etapa_atual, "
                   + "data_criacao, data_atualizacao, observacoes, cliente_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,    proposta.getCodigo());
            ps.setString(2,    proposta.getTitulo());
            ps.setString(3,    proposta.getDescricao());
            ps.setDouble(4,    proposta.getValor());
            ps.setString(5,    proposta.getStatus());
            ps.setString(6,    proposta.getEtapaAtual());
            ps.setTimestamp(7, new Timestamp(proposta.getDataCriacao().getTime()));
            ps.setTimestamp(8, new Timestamp(proposta.getDataAtualizacao().getTime()));
            ps.setString(9,    proposta.getObservacoes());
            ps.setInt(10,      proposta.getCliente().getId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) proposta.setId(rs.getInt(1));
        } finally {
            fecharConexao(conn);
        }
    }

    @Override
    public void atualizar(Proposta proposta) throws SQLException {
        String sql = "UPDATE proposta SET titulo=?, descricao=?, valor=?, status=?, etapa_atual=?, "
                   + "data_atualizacao=?, observacoes=?, cliente_id=? WHERE id=?";
        Connection conn = null;
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,    proposta.getTitulo());
            ps.setString(2,    proposta.getDescricao());
            ps.setDouble(3,    proposta.getValor());
            ps.setString(4,    proposta.getStatus());
            ps.setString(5,    proposta.getEtapaAtual());
            ps.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
            ps.setString(7,    proposta.getObservacoes());
            ps.setInt(8,       proposta.getCliente().getId());
            ps.setInt(9,       proposta.getId());
            ps.executeUpdate();
        } finally {
            fecharConexao(conn);
        }
    }

    @Override
    public void deletar(int id) throws SQLException {
        // Deleta documentos vinculados antes (integridade referencial)
        String sqlDocs = "DELETE FROM documento WHERE proposta_id=?";
        String sqlProp = "DELETE FROM proposta WHERE id=?";
        Connection conn = null;
        try {
            conn = getConexao();
            PreparedStatement ps1 = conn.prepareStatement(sqlDocs);
            ps1.setInt(1, id);
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(sqlProp);
            ps2.setInt(1, id);
            ps2.executeUpdate();
        } finally {
            fecharConexao(conn);
        }
    }

    @Override
    public Proposta buscarPorId(int id) throws SQLException {
        String sql = "SELECT p.*, c.id AS c_id, c.nome AS c_nome, c.cpf_cnpj, c.email, "
                   + "c.telefone, c.endereco, c.cidade, c.estado, c.cep, c.tipo AS c_tipo, c.ativo "
                   + "FROM proposta p "
                   + "JOIN cliente c ON p.cliente_id = c.id "
                   + "WHERE p.id = ?";
        Connection conn = null;
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Proposta proposta = mapearProposta(rs);
                // Carrega os documentos vinculados (1:N)
                proposta.setDocumentos(documentoDAO.buscarPorProposta(proposta.getId()));
                return proposta;
            }
            return null;
        } finally {
            fecharConexao(conn);
        }
    }

    @Override
    public List<Proposta> buscarTodos() throws SQLException {
        String sql = "SELECT p.*, c.id AS c_id, c.nome AS c_nome, c.cpf_cnpj, c.email, "
                   + "c.telefone, c.endereco, c.cidade, c.estado, c.cep, c.tipo AS c_tipo, c.ativo "
                   + "FROM proposta p "
                   + "JOIN cliente c ON p.cliente_id = c.id "
                   + "ORDER BY p.data_criacao DESC";
        Connection conn = null;
        List<Proposta> lista = new ArrayList<>();
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearProposta(rs));
            return lista;
        } finally {
            fecharConexao(conn);
        }
    }

    // Busca propostas por status (útil para a esteira)
    public List<Proposta> buscarPorStatus(String status) throws SQLException {
        String sql = "SELECT p.*, c.id AS c_id, c.nome AS c_nome, c.cpf_cnpj, c.email, "
                   + "c.telefone, c.endereco, c.cidade, c.estado, c.cep, c.tipo AS c_tipo, c.ativo "
                   + "FROM proposta p "
                   + "JOIN cliente c ON p.cliente_id = c.id "
                   + "WHERE p.status = ? ORDER BY p.data_criacao DESC";
        Connection conn = null;
        List<Proposta> lista = new ArrayList<>();
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearProposta(rs));
            return lista;
        } finally {
            fecharConexao(conn);
        }
    }

    // Monta a Proposta com o Cliente embutido (JOIN)
    private Proposta mapearProposta(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente.Builder()
            .id(rs.getInt("c_id"))
            .nome(rs.getString("c_nome"))
            .cpfCnpj(rs.getString("cpf_cnpj"))
            .email(rs.getString("email"))
            .telefone(rs.getString("telefone"))
            .endereco(rs.getString("endereco"))
            .cidade(rs.getString("cidade"))
            .estado(rs.getString("estado"))
            .cep(rs.getString("cep"))
            .tipo(rs.getString("c_tipo"))
            .ativo(rs.getBoolean("ativo"))
            .build();

        return new Proposta.Builder()
            .id(rs.getInt("id"))
            .codigo(rs.getString("codigo"))
            .titulo(rs.getString("titulo"))
            .descricao(rs.getString("descricao"))
            .valor(rs.getDouble("valor"))
            .status(rs.getString("status"))
            .etapaAtual(rs.getString("etapa_atual"))
            .dataCriacao(rs.getTimestamp("data_criacao"))
            .dataAtualizacao(rs.getTimestamp("data_atualizacao"))
            .observacoes(rs.getString("observacoes"))
            .cliente(cliente)
            .build();
    }
}
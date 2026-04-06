package dao;

import model.Documento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentoDAO extends AbstractDAO<Documento> {

    @Override
    public void inserir(Documento doc) throws SQLException {
        String sql = "INSERT INTO documento (nome, tipo, caminho, descricao, data_upload, proposta_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, doc.getNome());
            ps.setString(2, doc.getTipo());
            ps.setString(3, doc.getCaminho());
            ps.setString(4, doc.getDescricao());
            ps.setTimestamp(5, new Timestamp(doc.getDataUpload().getTime()));
            ps.setInt(6, doc.getPropostaId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) doc.setId(rs.getInt(1));
        } finally {
            fecharConexao(conn);
        }
    }

    @Override
    public void atualizar(Documento doc) throws SQLException {
        String sql = "UPDATE documento SET nome=?, tipo=?, caminho=?, descricao=? WHERE id=?";
        Connection conn = null;
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, doc.getNome());
            ps.setString(2, doc.getTipo());
            ps.setString(3, doc.getCaminho());
            ps.setString(4, doc.getDescricao());
            ps.setInt(5,    doc.getId());
            ps.executeUpdate();
        } finally {
            fecharConexao(conn);
        }
    }

    @Override
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM documento WHERE id=?";
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
    public Documento buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM documento WHERE id=?";
        Connection conn = null;
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearDocumento(rs);
            return null;
        } finally {
            fecharConexao(conn);
        }
    }

    @Override
    public List<Documento> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM documento ORDER BY data_upload DESC";
        Connection conn = null;
        List<Documento> lista = new ArrayList<>();
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearDocumento(rs));
            return lista;
        } finally {
            fecharConexao(conn);
        }
    }

    // Busca todos os documentos de uma proposta específica (1:N)
    public List<Documento> buscarPorProposta(int propostaId) throws SQLException {
        String sql = "SELECT * FROM documento WHERE proposta_id=? ORDER BY data_upload DESC";
        Connection conn = null;
        List<Documento> lista = new ArrayList<>();
        try {
            conn = getConexao();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, propostaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearDocumento(rs));
            return lista;
        } finally {
            fecharConexao(conn);
        }
    }

    private Documento mapearDocumento(ResultSet rs) throws SQLException {
        return new Documento.Builder()
            .id(rs.getInt("id"))
            .nome(rs.getString("nome"))
            .tipo(rs.getString("tipo"))
            .caminho(rs.getString("caminho"))
            .descricao(rs.getString("descricao"))
            .dataUpload(rs.getTimestamp("data_upload"))
            .propostaId(rs.getInt("proposta_id"))
            .build();
    }
}
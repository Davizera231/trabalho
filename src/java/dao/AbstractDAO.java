package dao;

import util.ConexaoDB;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDAO<T> {

    // Métodos abstratos que cada DAO concreto deve implementar
    public abstract void inserir(T obj) throws SQLException;
    public abstract void atualizar(T obj) throws SQLException;
    public abstract void deletar(int id) throws SQLException;
    public abstract T buscarPorId(int id) throws SQLException;
    public abstract List<T> buscarTodos() throws SQLException;

    // Método utilitário compartilhado por todos os DAOs filhos
    protected Connection getConexao() throws SQLException {
        return ConexaoDB.obterConexao();
    }

    protected void fecharConexao(Connection conn) {
        ConexaoDB.fecharConexao(conn);
    }
}
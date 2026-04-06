package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/esteira_proposta"
    + "?useSSL=false"
    + "&serverTimezone=America/Sao_Paulo"
    + "&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String SENHA   = ""; 

    private ConexaoDB() {}

    public static Connection obterConexao() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL não encontrado.", e);
        }
    }

    public static void fecharConexao(Connection conn) {
        if (conn != null) {
            try { conn.close(); }
            catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
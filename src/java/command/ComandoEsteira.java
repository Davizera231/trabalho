package command;

import model.Proposta;
import java.sql.SQLException;

public interface ComandoEsteira {
    void executar(Proposta proposta) throws SQLException;
    String getDescricao();
    String getProximaEtapa();
}
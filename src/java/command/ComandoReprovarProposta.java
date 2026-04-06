package command;

import dao.PropostaDAO;
import model.Proposta;
import java.sql.SQLException;
import java.util.Date;

public class ComandoReprovarProposta implements ComandoEsteira {

    private final PropostaDAO propostaDAO = new PropostaDAO();

    @Override
    public void executar(Proposta proposta) throws SQLException {
        // Regra de negócio: só pode reprovar se estiver em ANALISE
        if (!Proposta.STATUS_ANALISE.equals(proposta.getStatus())) {
            throw new IllegalStateException(
                "Apenas propostas em ANÁLISE podem ser reprovadas."
            );
        }

        proposta.setStatus(Proposta.STATUS_REPROVADA);
        proposta.setEtapaAtual(getProximaEtapa());
        proposta.setDataAtualizacao(new Date());
        propostaDAO.atualizar(proposta);
    }

    @Override
    public String getDescricao() {
        return "Proposta reprovada.";
    }

    @Override
    public String getProximaEtapa() {
        return "REPROVADA";
    }
}
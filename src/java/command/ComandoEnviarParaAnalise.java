package command;

import dao.PropostaDAO;
import model.Proposta;
import java.sql.SQLException;
import java.util.Date;

public class ComandoEnviarParaAnalise implements ComandoEsteira {

    private final PropostaDAO propostaDAO = new PropostaDAO();

    @Override
    public void executar(Proposta proposta) throws SQLException {
        // Regra de negócio: só pode enviar se estiver em RASCUNHO
        if (!Proposta.STATUS_RASCUNHO.equals(proposta.getStatus())) {
            throw new IllegalStateException(
                "Apenas propostas em RASCUNHO podem ser enviadas para análise."
            );
        }

        proposta.setStatus(Proposta.STATUS_ANALISE);
        proposta.setEtapaAtual(getProximaEtapa());
        proposta.setDataAtualizacao(new Date());
        propostaDAO.atualizar(proposta);
    }

    @Override
    public String getDescricao() {
        return "Proposta enviada para análise.";
    }

    @Override
    public String getProximaEtapa() {
        return "ANALISE";
    }
}
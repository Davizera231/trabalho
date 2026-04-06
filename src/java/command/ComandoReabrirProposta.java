package command;

import dao.PropostaDAO;
import model.Proposta;
import java.sql.SQLException;
import java.util.Date;

public class ComandoReabrirProposta implements ComandoEsteira {

    private final PropostaDAO propostaDAO = new PropostaDAO();

    @Override
    public void executar(Proposta proposta) throws SQLException {
        if (!Proposta.STATUS_REPROVADA.equals(proposta.getStatus())) {
            throw new IllegalStateException(
                "Apenas propostas REPROVADAS podem ser reabertas."
            );
        }

        proposta.setStatus(Proposta.STATUS_RASCUNHO);
        proposta.setEtapaAtual(getProximaEtapa());
        proposta.setDataAtualizacao(new Date());
        propostaDAO.atualizar(proposta);
    }

    @Override
    public String getDescricao() {
        return "Proposta reaberta para revisão.";
    }

    @Override
    public String getProximaEtapa() {
        return "CADASTRO";
    }
}
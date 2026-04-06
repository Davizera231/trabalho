package state;

import model.Proposta;

public class EstadoAnalise implements EstadoProposta {

    @Override
    public void avancar(Proposta proposta) {
        // ANALISE → APROVADA: permitido
        proposta.setEstado(new EstadoAprovada());
    }

    @Override
    public void reprovar(Proposta proposta) {
        // ANALISE → REPROVADA: permitido
        proposta.setEstado(new EstadoReprovada());
    }

    @Override
    public void reabrir(Proposta proposta) {
        throw new IllegalStateException(
            "Só propostas REPROVADAS podem ser reabertas."
        );
    }

    @Override
    public String getNome()  { return "ANALISE"; }

    @Override
    public String getEtapa() { return "ANALISE"; }
}
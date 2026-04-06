package state;

import model.Proposta;

public class EstadoRascunho implements EstadoProposta {

    @Override
    public void avancar(Proposta proposta) {
        // RASCUNHO → ANALISE: permitido
        proposta.setEstado(new EstadoAnalise());
    }

    @Override
    public void reprovar(Proposta proposta) {
        // Não faz sentido reprovar um rascunho
        throw new IllegalStateException(
            "Não é possível reprovar uma proposta em RASCUNHO."
        );
    }

    @Override
    public void reabrir(Proposta proposta) {
        throw new IllegalStateException(
            "Proposta já está em RASCUNHO."
        );
    }

    @Override
    public String getNome()  { return "RASCUNHO"; }

    @Override
    public String getEtapa() { return "CADASTRO"; }
}
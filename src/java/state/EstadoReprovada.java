package state;

import model.Proposta;

public class EstadoReprovada implements EstadoProposta {

    @Override
    public void avancar(Proposta proposta) {
        throw new IllegalStateException(
            "Proposta REPROVADA não pode avançar. Use reabrir."
        );
    }

    @Override
    public void reprovar(Proposta proposta) {
        throw new IllegalStateException(
            "Proposta já está REPROVADA."
        );
    }

    @Override
    public void reabrir(Proposta proposta) {
        // REPROVADA → RASCUNHO: permitido
        proposta.setEstado(new EstadoRascunho());
    }

    @Override
    public String getNome()  { return "REPROVADA"; }

    @Override
    public String getEtapa() { return "REPROVADA"; }
}
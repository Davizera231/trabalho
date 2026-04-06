package state;

import model.Proposta;

public class EstadoAprovada implements EstadoProposta {

    @Override
    public void avancar(Proposta proposta) {
        // Estado final — não avança mais
        throw new IllegalStateException(
            "Proposta já foi APROVADA. Não há próxima etapa."
        );
    }

    @Override
    public void reprovar(Proposta proposta) {
        throw new IllegalStateException(
            "Não é possível reprovar uma proposta já APROVADA."
        );
    }

    @Override
    public void reabrir(Proposta proposta) {
        throw new IllegalStateException(
            "Não é possível reabrir uma proposta APROVADA."
        );
    }

    @Override
    public String getNome()  { return "APROVADA"; }

    @Override
    public String getEtapa() { return "APROVADA"; }
}
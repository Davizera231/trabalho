package state;

import model.Proposta;

public interface EstadoProposta {

    // Tenta avançar para o próximo estado
    void avancar(Proposta proposta);

    // Tenta reprovar (só faz sentido em ANALISE)
    void reprovar(Proposta proposta);

    // Tenta reabrir (só faz sentido em REPROVADA)
    void reabrir(Proposta proposta);

    // Retorna o nome do estado atual
    String getNome();

    // Retorna a etapa atual da esteira
    String getEtapa();
}
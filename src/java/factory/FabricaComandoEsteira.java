package factory;

import command.*;

public class FabricaComandoEsteira {

    // Constantes das ações disponíveis
    public static final String ACAO_ENVIAR_ANALISE = "ENVIAR_ANALISE";
    public static final String ACAO_APROVAR        = "APROVAR";
    public static final String ACAO_REPROVAR       = "REPROVAR";
    public static final String ACAO_REABRIR        = "REABRIR";

    // Construtor privado — classe utilitária
    private FabricaComandoEsteira() {}

    // Factory Method: recebe a ação e retorna o comando correto
    public static ComandoEsteira criar(String acao) {
        if (acao == null) throw new IllegalArgumentException("Ação não pode ser nula.");

        switch (acao.toUpperCase()) {
            case ACAO_ENVIAR_ANALISE: return new ComandoEnviarParaAnalise();
            case ACAO_APROVAR:        return new ComandoAprovarProposta();
            case ACAO_REPROVAR:       return new ComandoReprovarProposta();
            case ACAO_REABRIR:        return new ComandoReabrirProposta();
            default:
                throw new IllegalArgumentException("Ação desconhecida: " + acao);
        }
    }

    // Retorna as ações disponíveis para um determinado status
    // Útil para montar os botões dinamicamente no JSP
    public static String[] acoesDisponiveisPara(String status) {
        switch (status) {
            case "RASCUNHO":  return new String[]{ACAO_ENVIAR_ANALISE};
            case "ANALISE":   return new String[]{ACAO_APROVAR, ACAO_REPROVAR};
            case "REPROVADA": return new String[]{ACAO_REABRIR};
            default:          return new String[]{};
        }
    }
}
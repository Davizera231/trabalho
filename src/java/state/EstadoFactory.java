package state;

public class EstadoFactory {

    private EstadoFactory() {}

    // Quando carregar a Proposta do banco (status vem como String),
    // precisamos reconstruir o objeto Estado correto
    public static EstadoProposta fromString(String status) {
        if (status == null) return new EstadoRascunho();
        switch (status.toUpperCase()) {
            case "RASCUNHO":  return new EstadoRascunho();
            case "ANALISE":   return new EstadoAnalise();
            case "APROVADA":  return new EstadoAprovada();
            case "REPROVADA": return new EstadoReprovada();
            default:
                throw new IllegalArgumentException(
                    "Status desconhecido: " + status
                );
        }
    }
}
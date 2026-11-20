package sistemaGerenciamentoEstoque.exception;

public class EstoqueInsuficienteException extends EstoqueException {

    private static final long serialVersionUID = 1L;

    private final String nomeProduto;
    private final double quantidadeSolicitada;
    private final double quantidadeDisponivel;

    public EstoqueInsuficienteException(String nomeProduto,
                                        double quantidadeSolicitada,
                                        double quantidadeDisponivel) {
        super(String.format(
                "Estoque insuficiente para o produto '%s'. Solicitado: %.2f, disponível: %.2f.",
                nomeProduto, quantidadeSolicitada, quantidadeDisponivel));
        this.nomeProduto = nomeProduto;
        this.quantidadeSolicitada = quantidadeSolicitada;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public double getQuantidadeSolicitada() {
        return quantidadeSolicitada;
    }

    public double getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }
}

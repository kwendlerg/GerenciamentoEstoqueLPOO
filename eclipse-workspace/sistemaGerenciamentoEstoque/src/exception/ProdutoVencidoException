package sistemaGerenciamentoEstoque.exception;

import java.time.LocalDate;

public class ProdutoVencidoException extends EstoqueException {

    private static final long serialVersionUID = 1L;

    private final String nomeProduto;
    private final LocalDate dataValidade;

    public ProdutoVencidoException(String nomeProduto, LocalDate dataValidade) {
        super(String.format(
                "O produto '%s' está vencido (validade: %s). Movimentação bloqueada.",
                nomeProduto, dataValidade));
        this.nomeProduto = nomeProduto;
        this.dataValidade = dataValidade;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }
}

package sistemaGerenciamentoEstoque.exception;

public class ProdutoNaoEncontradoException extends EstoqueException {

    private static final long serialVersionUID = 1L;

    private final int idBuscado;

    public ProdutoNaoEncontradoException(int idBuscado) {
        super("Produto com ID " + idBuscado + " não foi encontrado no sistema.");
        this.idBuscado = idBuscado;
    }

    public ProdutoNaoEncontradoException(String nomeBuscado) {
        super("Produto com nome '" + nomeBuscado + "' não foi encontrado.");
        this.idBuscado = -1;
    }

    public int getIdBuscado() {
        return idBuscado;
    }
}

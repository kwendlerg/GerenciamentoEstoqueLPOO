package sistemaGerenciamentoEstoque.exception;

public class EstoqueException extends Exception {

    private static final long serialVersionUID = 1L;

    public EstoqueException(String mensagem) {
        super(mensagem);
    }

    public EstoqueException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}

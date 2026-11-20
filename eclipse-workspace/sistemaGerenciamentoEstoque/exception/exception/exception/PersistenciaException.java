package sistemaGerenciamentoEstoque.exception;

public class PersistenciaException extends EstoqueException {

    private static final long serialVersionUID = 1L;

    public PersistenciaException(String operacao, Throwable causa) {
        super("Falha ao executar operação no banco de dados: " + operacao, causa);
    }

    public PersistenciaException(String mensagem) {
        super(mensagem);
    }
}

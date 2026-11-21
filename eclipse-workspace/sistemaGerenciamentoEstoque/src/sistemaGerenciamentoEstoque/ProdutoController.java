
package sistemaGerenciamentoEstoque;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutoController {

    private ProdutoDAO dao;

    public ProdutoController() {
        dao = new ProdutoDAO();
    }

    public void cadastrarProduto(Produto produto)
            throws SQLException {

        if (produto == null) {
            throw new IllegalArgumentException(
                "Produto invalido."
            );
        }

        dao.inserir(produto);
    }

    public Produto buscarProduto(int id)
            throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                "ID invalido."
            );
        }

        return dao.buscarPorId(id);
    }

    public ArrayList<Produto> listarProdutos()
            throws SQLException {

        return dao.listarTodos();
    }

    public void atualizarProduto(Produto produto)
            throws SQLException {

        if (produto == null || produto.getId() <= 0) {
            throw new IllegalArgumentException(
                "Produto invalido."
            );
        }

        dao.atualizar(produto);
    }

    public void excluirProduto(int id)
            throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                "ID invalido."
            );
        }

        dao.excluir(id);
    }
}

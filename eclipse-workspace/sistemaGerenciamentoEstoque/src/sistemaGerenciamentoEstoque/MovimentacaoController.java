
package sistemaGerenciamentoEstoque;

import java.sql.SQLException;
import java.util.ArrayList;

public class MovimentacaoController {

    private RegistroMovimentacaoDAO dao;
    private ProdutoDAO produtoDAO;
    private FuncionarioDAO funcionarioDAO;

    public MovimentacaoController() {
        dao = new RegistroMovimentacaoDAO();
        produtoDAO = new ProdutoDAO();
        funcionarioDAO = new FuncionarioDAO();
    }

    public void validarEntrada(Produto produto,
                               Funcionario funcionario,
                               double quantidade) {

        if (produto == null || funcionario == null) {
            throw new IllegalArgumentException(
                "Produto ou funcionario invalido."
            );
        }

        if (!Double.isFinite(quantidade) || quantidade <= 0) {
            throw new IllegalArgumentException(
                "Quantidade invalida."
            );
        }
    }

    public void validarSaida(Produto produto,
                             Funcionario funcionario,
                             double quantidade) {

        validarEntrada(produto, funcionario, quantidade);

        if (quantidade > produto.getQuantidade()) {
            throw new IllegalArgumentException(
                "Estoque insuficiente."
            );
        }
    }

    public void registrarEntrada(Produto produto,
                                 Funcionario funcionario,
                                 double quantidade) throws SQLException {

        validarEntrada(produto, funcionario, quantidade);

        if (produto.getId() <= 0 || funcionario.getId() <= 0) {
            throw new IllegalArgumentException(
                "Produto e funcionario devem estar cadastrados."
            );
        }

        Produto produtoBanco = produtoDAO.buscarPorId(produto.getId());
        Funcionario funcionarioBanco =
            funcionarioDAO.buscarPorId(funcionario.getId());

        if (produtoBanco == null || funcionarioBanco == null) {
            throw new IllegalArgumentException(
                "Produto ou funcionario nao encontrado."
            );
        }

        double novaQuantidade = produtoBanco.getQuantidade() + quantidade;

        if (!Double.isFinite(novaQuantidade)) {
            throw new IllegalArgumentException(
                "Quantidade final invalida."
            );
        }

        RegistroMovimentacao registro = new RegistroMovimentacao(
            produtoBanco, funcionarioBanco, "ENTRADA", quantidade
        );

        produtoDAO.atualizarQuantidade(produtoBanco.getId(), novaQuantidade);
        dao.inserir(registro);
    }

    public void registrarSaida(Produto produto,
                               Funcionario funcionario,
                               double quantidade) throws SQLException {

        validarEntrada(produto, funcionario, quantidade);

        if (produto.getId() <= 0 || funcionario.getId() <= 0) {
            throw new IllegalArgumentException(
                "Produto e funcionario devem estar cadastrados."
            );
        }

        Produto produtoBanco = produtoDAO.buscarPorId(produto.getId());
        Funcionario funcionarioBanco =
            funcionarioDAO.buscarPorId(funcionario.getId());

        if (produtoBanco == null || funcionarioBanco == null) {
            throw new IllegalArgumentException(
                "Produto ou funcionario nao encontrado."
            );
        }

        validarSaida(produtoBanco, funcionarioBanco, quantidade);

        double novaQuantidade = produtoBanco.getQuantidade() - quantidade;

        RegistroMovimentacao registro = new RegistroMovimentacao(
            produtoBanco, funcionarioBanco, "SAIDA", quantidade
        );

        produtoDAO.atualizarQuantidade(produtoBanco.getId(), novaQuantidade);
        dao.inserir(registro);
    }

    public ArrayList<RegistroMovimentacao> listarMovimentacoes()
            throws SQLException {

        return dao.listarTodos();
    }

    public ArrayList<RegistroMovimentacao> listarPorProduto(int id)
            throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                "ID invalido."
            );
        }

        return dao.listarPorProduto(id);
    }
}

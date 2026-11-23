
package sistemaGerenciamentoEstoque;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import sistemaGerenciamentoEstoque.exception.EstoqueException;
import sistemaGerenciamentoEstoque.exception.EstoqueInsuficienteException;
import sistemaGerenciamentoEstoque.exception.PersistenciaException;
import sistemaGerenciamentoEstoque.exception.ProdutoNaoEncontradoException;
import sistemaGerenciamentoEstoque.exception.ProdutoVencidoException;

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
                             double quantidade) throws EstoqueInsuficienteException {

        validarEntrada(produto, funcionario, quantidade);

        if (quantidade > produto.getQuantidade()) {
            throw new EstoqueInsuficienteException(
                produto.getNome(), quantidade, produto.getQuantidade()
            );
        }
    }

    public void validarValidade(Produto produto) throws ProdutoVencidoException {

        LocalDate validade = produto.getDataValidade();

        if (validade != null && validade.isBefore(LocalDate.now())) {
            throw new ProdutoVencidoException(produto.getNome(), validade);
        }
    }

    public void registrarEntrada(Produto produto,
                                 Funcionario funcionario,
                                 double quantidade) throws EstoqueException {

        validarEntrada(produto, funcionario, quantidade);

        Produto produtoBanco = buscarProdutoNoBanco(produto.getId());
        Funcionario funcionarioBanco = buscarFuncionarioNoBanco(funcionario.getId());

        validarValidade(produtoBanco);

        double novaQuantidade = produtoBanco.getQuantidade() + quantidade;

        if (!Double.isFinite(novaQuantidade)) {
            throw new IllegalArgumentException(
                "Quantidade final invalida."
            );
        }

        RegistroMovimentacao registro = new RegistroMovimentacao(
            produtoBanco, funcionarioBanco, "ENTRADA", quantidade
        );

        try {
            produtoDAO.atualizarQuantidade(produtoBanco.getId(), novaQuantidade);
            dao.inserir(registro);
        } catch (SQLException e) {
            throw new PersistenciaException("registrar entrada", e);
        }
    }

    public void registrarSaida(Produto produto,
                               Funcionario funcionario,
                               double quantidade) throws EstoqueException {

        validarEntrada(produto, funcionario, quantidade);

        Produto produtoBanco = buscarProdutoNoBanco(produto.getId());
        Funcionario funcionarioBanco = buscarFuncionarioNoBanco(funcionario.getId());

        validarValidade(produtoBanco);
        validarSaida(produtoBanco, funcionarioBanco, quantidade);

        double novaQuantidade = produtoBanco.getQuantidade() - quantidade;

        RegistroMovimentacao registro = new RegistroMovimentacao(
            produtoBanco, funcionarioBanco, "SAIDA", quantidade
        );

        try {
            produtoDAO.atualizarQuantidade(produtoBanco.getId(), novaQuantidade);
            dao.inserir(registro);
        } catch (SQLException e) {
            throw new PersistenciaException("registrar saida", e);
        }
    }

    public ArrayList<RegistroMovimentacao> listarMovimentacoes()
            throws PersistenciaException {

        try {
            return dao.listarTodos();
        } catch (SQLException e) {
            throw new PersistenciaException("listar movimentacoes", e);
        }
    }

    public ArrayList<RegistroMovimentacao> listarPorProduto(int id)
            throws EstoqueException {

        buscarProdutoNoBanco(id);

        try {
            return dao.listarPorProduto(id);
        } catch (SQLException e) {
            throw new PersistenciaException("listar movimentacoes do produto", e);
        }
    }

    private Produto buscarProdutoNoBanco(int id) throws EstoqueException {

        if (id <= 0) {
            throw new ProdutoNaoEncontradoException(id);
        }

        Produto produto;
        try {
            produto = produtoDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new PersistenciaException("buscar produto", e);
        }

        if (produto == null) {
            throw new ProdutoNaoEncontradoException(id);
        }
        return produto;
    }

    private Funcionario buscarFuncionarioNoBanco(int id) throws EstoqueException {

        Funcionario funcionario;
        try {
            funcionario = funcionarioDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw new PersistenciaException("buscar funcionario", e);
        }

        if (funcionario == null) {
            throw new EstoqueException("Funcionario com ID " + id + " nao foi encontrado.");
        }
        return funcionario;
    }
}

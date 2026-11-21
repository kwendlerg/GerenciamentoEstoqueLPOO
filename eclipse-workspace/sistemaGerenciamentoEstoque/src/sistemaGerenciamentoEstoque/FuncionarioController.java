
package sistemaGerenciamentoEstoque;

import java.sql.SQLException;
import java.util.ArrayList;

public class FuncionarioController {

    private FuncionarioDAO dao;

    public FuncionarioController() {
        dao = new FuncionarioDAO();
    }

    public void cadastrarFuncionario(Funcionario funcionario)
            throws SQLException {

        if (funcionario == null) {
            throw new IllegalArgumentException(
                "Funcionario invalido."
            );
        }

        dao.inserir(funcionario);
    }

    public Funcionario buscarFuncionario(int id)
            throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                "ID invalido."
            );
        }

        return dao.buscarPorId(id);
    }

    public ArrayList<Funcionario> listarFuncionarios()
            throws SQLException {

        return dao.listarTodos();
    }

    public void atualizarFuncionario(Funcionario funcionario)
            throws SQLException {

        if (funcionario == null || funcionario.getId() <= 0) {
            throw new IllegalArgumentException(
                "Funcionario invalido."
            );
        }

        dao.atualizar(funcionario);
    }

    public void excluirFuncionario(int id)
            throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                "ID invalido."
            );
        }

        dao.excluir(id);
    }
}

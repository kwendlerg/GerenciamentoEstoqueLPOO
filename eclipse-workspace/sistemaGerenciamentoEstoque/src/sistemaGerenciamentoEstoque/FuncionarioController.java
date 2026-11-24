
package sistemaGerenciamentoEstoque;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

        if (funcionario.getLogin() == null || funcionario.getSenha() == null) {
            throw new IllegalArgumentException(
                "Informe o login e a senha."
            );
        }

        if (funcionario.getSenha().length() < 4) {
            throw new IllegalArgumentException(
                "A senha deve ter pelo menos 4 caracteres."
            );
        }

        if (dao.buscarPorLogin(funcionario.getLogin()) != null) {
            throw new IllegalArgumentException(
                "Ja existe um funcionario com esse login."
            );
        }

        funcionario.setSenha(gerarHash(funcionario.getSenha()));
        dao.inserir(funcionario);
    }

    public Funcionario autenticar(String login, String senha)
            throws SQLException {

        if (login == null || login.trim().isEmpty()
                || senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException(
                "Preencha o login e a senha."
            );
        }

        Funcionario funcionario = dao.buscarPorLogin(login.trim());

        if (funcionario == null
                || funcionario.getSenha() == null
                || !funcionario.getSenha().equals(gerarHash(senha))) {
            return null;
        }

        return funcionario;
    }

    public Funcionario buscarPorLogin(String login)
            throws SQLException {

        return dao.buscarPorLogin(login);
    }

    public boolean existeUsuarioComLogin()
            throws SQLException {

        for (Funcionario funcionario : dao.listarTodos()) {
            if (funcionario.getLogin() != null) {
                return true;
            }
        }
        return false;
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

    private String gerarHash(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));

            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 nao disponivel.", e);
        }
    }
}

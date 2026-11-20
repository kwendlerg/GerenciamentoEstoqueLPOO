package sistemaGerenciamentoEstoque;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class FuncionarioDAO {
	
	public void inserir(Funcionario funcionario) throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "INSERT INTO funcionarios (nome, data_admissao, tipo_funcionario) VALUES (?, ?, ?)";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		
		stmt.setString(1, funcionario.getNome());
		stmt.setString(2, String.valueOf(funcionario.getDataAdmissao()));
		stmt.setString(3, getTipo(funcionario));
		stmt.executeUpdate();
		
		stmt.close();
		conexao.close();
	}
	
	public void atualizar(Funcionario funcionario) throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "UPDATE funcionarios SET nome = ?, data_admissao = ?, tipo_funcionario = ? WHERE id = ?";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		
		stmt.setString(1, funcionario.getNome());
		stmt.setString(2, String.valueOf(funcionario.getDataAdmissao()));
		stmt.setString(3, getTipo(funcionario));
		stmt.setInt(4, funcionario.getId());
		stmt.executeUpdate();
		
		stmt.close();
		conexao.close();
	}
	
	public void excluir(int id) throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "DELETE FROM funcionarios WHERE id = ?";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		
		stmt.setInt(1, id);
		stmt.executeUpdate();
		
		stmt.close();
		conexao.close();
	}
	
	public Funcionario buscarPorId(int id) throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "SELECT * FROM funcionarios WHERE id = ?";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		
		Funcionario funcionario = null;
		if (rs.next()) {
			funcionario = criarFuncionario(rs);
		}
		
		rs.close();
		stmt.close();
		conexao.close();
		return funcionario;
	}
	
	public ArrayList<Funcionario> listarTodos() throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "SELECT * FROM funcionarios";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<Funcionario> lista = new ArrayList<Funcionario>();
		while (rs.next()) {
			lista.add(criarFuncionario(rs));
		}
		
		rs.close();
		stmt.close();
		conexao.close();
		return lista;
	}
	
	private String getTipo(Funcionario funcionario) {
		if (funcionario instanceof Adm) {
			return "ADM";
		} else {
			return "ALMOXARIFE";
		}
	}
	
	private Funcionario criarFuncionario(ResultSet rs) throws SQLException {
		String nome = rs.getString("nome");
		String tipo = rs.getString("tipo_funcionario");
		
		LocalDate dataAdmissao = null;
		String data = rs.getString("data_admissao");
		if (data != null && !data.equals("null")) {
			dataAdmissao = LocalDate.parse(data);
		}
		
		Funcionario funcionario;
		if (tipo.equals("ADM")) {
			funcionario = new Adm(nome, dataAdmissao);
		} else {
			funcionario = new Almoxarife(nome, dataAdmissao);
		}
		
		funcionario.setId(rs.getInt("id"));
		return funcionario;
	}
}

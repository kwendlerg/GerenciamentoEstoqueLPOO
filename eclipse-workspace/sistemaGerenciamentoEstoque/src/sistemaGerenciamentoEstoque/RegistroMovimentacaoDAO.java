package sistemaGerenciamentoEstoque;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RegistroMovimentacaoDAO {
	
	private ProdutoDAO produtoDAO = new ProdutoDAO();
	private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
	
	public void inserir(RegistroMovimentacao registro) throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "INSERT INTO movimentacoes (id_produto, id_funcionario_responsavel, quantidade, tipo_movimentacao, data_hora) "
				+ "VALUES (?, ?, ?, ?, ?)";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		
		stmt.setInt(1, registro.getProduto().getId());
		stmt.setInt(2, registro.getResponsavel().getId());
		stmt.setDouble(3, registro.getQuantidade());
		stmt.setString(4, registro.getTipoMovimentacao());
		stmt.setString(5, registro.getDataHora().toString());
		stmt.executeUpdate();
		
		stmt.close();
		conexao.close();
	}
	
	public void excluir(int id) throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "DELETE FROM movimentacoes WHERE id = ?";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		
		stmt.setInt(1, id);
		stmt.executeUpdate();
		
		stmt.close();
		conexao.close();
	}
	
	public ArrayList<RegistroMovimentacao> listarTodos() throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "SELECT * FROM movimentacoes";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<RegistroMovimentacao> lista = new ArrayList<RegistroMovimentacao>();
		while (rs.next()) {
			lista.add(criarRegistro(rs));
		}
		
		rs.close();
		stmt.close();
		conexao.close();
		return lista;
	}
	
	public ArrayList<RegistroMovimentacao> listarPorProduto(int idProduto) throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "SELECT * FROM movimentacoes WHERE id_produto = ?";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		stmt.setInt(1, idProduto);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<RegistroMovimentacao> lista = new ArrayList<RegistroMovimentacao>();
		while (rs.next()) {
			lista.add(criarRegistro(rs));
		}
		
		rs.close();
		stmt.close();
		conexao.close();
		return lista;
	}
	
	private RegistroMovimentacao criarRegistro(ResultSet rs) throws SQLException {
		Produto produto = produtoDAO.buscarPorId(rs.getInt("id_produto"));
		Funcionario responsavel = funcionarioDAO.buscarPorId(rs.getInt("id_funcionario_responsavel"));
		String tipo = rs.getString("tipo_movimentacao");
		double quantidade = rs.getDouble("quantidade");
		
		RegistroMovimentacao registro = new RegistroMovimentacao(produto, responsavel, tipo, quantidade);
		registro.setId(rs.getInt("id"));
		registro.setDataHora(LocalDateTime.parse(rs.getString("data_hora")));
		return registro;
	}
}

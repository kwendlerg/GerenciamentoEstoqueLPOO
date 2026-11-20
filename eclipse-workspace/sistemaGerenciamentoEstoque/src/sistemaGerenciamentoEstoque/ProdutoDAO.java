package sistemaGerenciamentoEstoque;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ProdutoDAO {
	
	public void inserir(Produto produto) throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "INSERT INTO produtos (nome, custo, quantidade, lote, data_validade, tipo_produto, "
				+ "tamanho, numero_serie, marca, voltagem, unidade_medida) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		
		preencherCampos(stmt, produto);
		stmt.executeUpdate();
		
		stmt.close();
		conexao.close();
	}
	
	public void atualizar(Produto produto) throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "UPDATE produtos SET nome = ?, custo = ?, quantidade = ?, lote = ?, data_validade = ?, tipo_produto = ?, "
				+ "tamanho = ?, numero_serie = ?, marca = ?, voltagem = ?, unidade_medida = ? WHERE id = ?";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		
		preencherCampos(stmt, produto);
		stmt.setInt(12, produto.getId());
		stmt.executeUpdate();
		
		stmt.close();
		conexao.close();
	}
	
	public void atualizarQuantidade(int id, double quantidade) throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "UPDATE produtos SET quantidade = ? WHERE id = ?";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		
		stmt.setDouble(1, quantidade);
		stmt.setInt(2, id);
		stmt.executeUpdate();
		
		stmt.close();
		conexao.close();
	}
	
	public void excluir(int id) throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "DELETE FROM produtos WHERE id = ?";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		
		stmt.setInt(1, id);
		stmt.executeUpdate();
		
		stmt.close();
		conexao.close();
	}
	
	public Produto buscarPorId(int id) throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "SELECT * FROM produtos WHERE id = ?";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		
		Produto produto = null;
		if (rs.next()) {
			produto = criarProduto(rs);
		}
		
		rs.close();
		stmt.close();
		conexao.close();
		return produto;
	}
	
	public ArrayList<Produto> listarTodos() throws SQLException {
		Connection conexao = ConexaoSQLite.conectar();
		String sql = "SELECT * FROM produtos";
		PreparedStatement stmt = conexao.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<Produto> lista = new ArrayList<Produto>();
		while (rs.next()) {
			lista.add(criarProduto(rs));
		}
		
		rs.close();
		stmt.close();
		conexao.close();
		return lista;
	}
	
	private void preencherCampos(PreparedStatement stmt, Produto produto) throws SQLException {
		String tipo = "CONSUMO";
		String tamanho = null;
		String numeroSerie = null;
		String marca = null;
		String voltagem = null;
		
		if (produto instanceof Epi) {
			Epi epi = (Epi) produto;
			tipo = "EPI";
			tamanho = epi.getTamanho();
		} else if (produto instanceof Maquinario) {
			Maquinario maquinario = (Maquinario) produto;
			tipo = "MAQUINARIO";
			numeroSerie = String.valueOf(maquinario.getNumeroSerie());
			marca = maquinario.getMarca();
			voltagem = String.valueOf(maquinario.getVoltagem());
		}
		
		stmt.setString(1, produto.getNome());
		stmt.setDouble(2, produto.getCusto());
		stmt.setDouble(3, produto.getQuantidade());
		stmt.setString(4, String.valueOf(produto.getLote()));
		stmt.setString(5, String.valueOf(produto.getDataValidade()));
		stmt.setString(6, tipo);
		stmt.setString(7, tamanho);
		stmt.setString(8, numeroSerie);
		stmt.setString(9, marca);
		stmt.setString(10, voltagem);
		stmt.setString(11, produto.getUnidadeMedida());
	}
	
	private Produto criarProduto(ResultSet rs) throws SQLException {
		String tipo = rs.getString("tipo_produto");
		String nome = rs.getString("nome");
		double custo = rs.getDouble("custo");
		double quantidade = rs.getDouble("quantidade");
		int lote = Integer.parseInt(rs.getString("lote"));
		String unidadeMedida = rs.getString("unidade_medida");
		
		LocalDate dataValidade = null;
		String data = rs.getString("data_validade");
		if (data != null && !data.equals("null")) {
			dataValidade = LocalDate.parse(data);
		}
		
		Produto produto;
		if (tipo.equals("EPI")) {
			String tamanho = rs.getString("tamanho");
			produto = new Epi(nome, custo, quantidade, lote, dataValidade, tamanho, unidadeMedida);
		} else if (tipo.equals("MAQUINARIO")) {
			int numeroSerie = Integer.parseInt(rs.getString("numero_serie"));
			String marca = rs.getString("marca");
			int voltagem = Integer.parseInt(rs.getString("voltagem"));
			produto = new Maquinario(nome, custo, quantidade, lote, dataValidade, unidadeMedida, numeroSerie, marca, voltagem);
		} else {
			produto = new MaterialConsumo(nome, custo, quantidade, lote, dataValidade, unidadeMedida);
		}
		
		produto.setId(rs.getInt("id"));
		produto.setUnidadeMedida(unidadeMedida);
		return produto;
	}
}

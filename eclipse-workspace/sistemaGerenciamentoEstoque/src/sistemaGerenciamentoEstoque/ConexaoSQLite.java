package sistemaGerenciamentoEstoque;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoSQLite {
	
	public static Connection conectar() throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new SQLException("Driver do SQLite não encontrado. Adicione o arquivo "
					+ "sqlite-jdbc-3.41.2.2.jar (pasta lib) no Build Path do projeto.");
		}

		Connection conexao = DriverManager.getConnection("jdbc:sqlite:gerenciamento_estoque.db");
		criarTabelas(conexao);
		return conexao;
	}
	
	private static void criarTabelas(Connection conexao) throws SQLException {
		Statement stmt = conexao.createStatement();
		
		stmt.execute("CREATE TABLE IF NOT EXISTS funcionarios ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "nome TEXT NOT NULL, "
				+ "data_admissao TEXT, "
				+ "salario REAL, "
				+ "tipo_funcionario TEXT NOT NULL, "
				+ "login TEXT, "
				+ "senha TEXT)");

		adicionarColuna(conexao, "funcionarios", "login", "TEXT");
		adicionarColuna(conexao, "funcionarios", "senha", "TEXT");
		
		stmt.execute("CREATE TABLE IF NOT EXISTS produtos ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "nome TEXT NOT NULL, "
				+ "custo REAL, "
				+ "quantidade INTEGER NOT NULL, "
				+ "lote TEXT, "
				+ "data_validade TEXT, "
				+ "tipo_produto TEXT NOT NULL, "
				+ "tamanho TEXT, "
				+ "numero_ca TEXT, "
				+ "numero_serie TEXT, "
				+ "marca TEXT, "
				+ "voltagem TEXT, "
				+ "unidade_medida TEXT)");
		
		stmt.execute("CREATE TABLE IF NOT EXISTS movimentacoes ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "id_produto INTEGER, "
				+ "id_funcionario_responsavel INTEGER, "
				+ "quantidade INTEGER NOT NULL, "
				+ "tipo_movimentacao TEXT NOT NULL, "
				+ "data_hora TEXT NOT NULL, "
				+ "FOREIGN KEY(id_produto) REFERENCES produtos(id), "
				+ "FOREIGN KEY(id_funcionario_responsavel) REFERENCES funcionarios(id))");
		
		stmt.close();
	}

	private static void adicionarColuna(Connection conexao, String tabela, String coluna, String tipo) throws SQLException {
		Statement stmt = conexao.createStatement();
		ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tabela + ")");

		boolean existe = false;
		while (rs.next()) {
			if (rs.getString("name").equalsIgnoreCase(coluna)) {
				existe = true;
			}
		}
		rs.close();

		if (!existe) {
			stmt.execute("ALTER TABLE " + tabela + " ADD COLUMN " + coluna + " " + tipo);
		}
		stmt.close();
	}
}

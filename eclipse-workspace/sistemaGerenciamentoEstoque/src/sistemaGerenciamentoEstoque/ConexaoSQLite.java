package sistemaGerenciamentoEstoque;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoSQLite {
	
	public static Connection conectar() throws SQLException {
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
				+ "tipo_funcionario TEXT NOT NULL)");
		
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
}

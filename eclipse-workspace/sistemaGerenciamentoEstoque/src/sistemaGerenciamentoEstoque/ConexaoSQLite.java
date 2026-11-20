package sistemaGerenciamentoEstoque;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoSQLite {
	
	public static Connection conectar() throws SQLException {
		Connection conexao = DriverManager.getConnection("jdbc:sqlite:gerenciamento_estoque.db");
		return conexao;
	}
}

package sistemaGerenciamentoEstoque;
import java.time.LocalDate;

public abstract class Funcionario {
	private int id;
	private String nome;
	private LocalDate dataAdmissao;
	private String login;
	private String senha;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
	    if (nome == null || nome.trim().isEmpty()) {
	        throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
	    }
	    this.nome = nome;
	}
	public LocalDate getDataAdmissao() {
		return dataAdmissao;
	}
	public void setDataAdmissao(LocalDate dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		if (login == null || login.trim().isEmpty()) {
			throw new IllegalArgumentException("O login não pode ser vazio.");
		}
		this.login = login.trim();
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		if (senha == null || senha.isEmpty()) {
			throw new IllegalArgumentException("A senha não pode ser vazia.");
		}
		this.senha = senha;
	}
	
	protected Funcionario(String nome,LocalDate dataAdmissao) {
		setNome(nome);
		setDataAdmissao(dataAdmissao);
	}
	
	protected Funcionario(int id, String nome,LocalDate dataAdmissao) {
		setId(id);
		setNome(nome);
		setDataAdmissao(dataAdmissao);
	}
	
	public abstract void exibirDetalhes();
}

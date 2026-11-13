package sistemaGerenciamentoEstoque;
import java.time.LocalDate;

public abstract class Funcionario {
	private int id;
	private String nome;
	private LocalDate dataAdmissao;
	
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

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
		this.nome = nome;
	}
	public LocalDate getDataAdmissao() {
		return dataAdmissao;
	}
	public void setDataAdmissão(LocalDate dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}
	
	protected Funcionario(String nome,LocalDate dataAdmissao) {
		this.nome = nome;
		this.dataAdmissao = dataAdmissao;
	}
	
	protected Funcionario(int id, String nome,LocalDate dataAdmissao) {
		this.id = id;
		this.nome = nome;
		this.dataAdmissao = dataAdmissao;
	}
	
	public abstract void exibirDetalhes();
}

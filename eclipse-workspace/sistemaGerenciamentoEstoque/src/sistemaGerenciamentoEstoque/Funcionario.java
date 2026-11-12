package sistemaGerenciamentoEstoque;
import java.time.LocalDate;

public abstract class Funcionario {
	private int id;
	private String nome;
	private LocalDate dataAdmissao;
	private double salario;
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
	public double getSalario() {
		return salario;
	}
	public void setSalario(double salario) {
		this.salario = salario;
	}
	
	protected Funcionario(String nome,LocalDate dataAdmissao, double salario) {
		this.nome = nome;
		this.dataAdmissao = dataAdmissao;
		this.salario = salario;
	}
	
	protected Funcionario(int id, String nome,LocalDate dataAdmissao, double salario) {
		this.id = id;
		this.nome = nome;
		this.dataAdmissao = dataAdmissao;
		this.salario = salario;
	}
	
	public abstract void exibirDetalhes();
}

package sistemaGerenciamentoEstoque;
import java.time.LocalDate;

public abstract class Produto {
	private int id;
	private String nome;
	private double custo;
	private double quantidade;
	private String unidadeMedida;
	private int lote;
	private LocalDate dataValidade;
	
	
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
	public double getCusto() {
		return custo;
	}
	public void setCusto(double custo) {
		this.custo = custo;
	}
	public double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}
	public String getUnidadeMedida() {
		return unidadeMedida;
	}
	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}
	public int getLote() {
		return lote;
	}
	public void setLote(int lote) {
		this.lote = lote;
	}
	public LocalDate getDataValidade() {
		return dataValidade;
	}
	public void setDataValidade(LocalDate dataValidade) {
		this.dataValidade = dataValidade;
	}
	
	protected Produto(String nome, double custo, double quantidade, int lote, LocalDate dataValidade, String unidadeMedida) {
		this.nome = nome;
		this.custo = custo;
		this.quantidade = quantidade;
		this.unidadeMedida = unidadeMedida;
		this.lote = lote;
		this.dataValidade = dataValidade;
	}
	
	protected Produto(int id, String nome, double custo, double quantidade, int lote, LocalDate dataValidade) {
		this.id = id;
		this.nome = nome;
		this.custo = custo;
		this.quantidade = quantidade;
		this.lote = lote;
		this.dataValidade = dataValidade;
	}
	
	public abstract void exibirDetalhes();
	
}

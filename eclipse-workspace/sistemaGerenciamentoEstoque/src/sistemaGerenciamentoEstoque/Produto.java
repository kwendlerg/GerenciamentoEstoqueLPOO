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
	    if (nome == null || nome.trim().isEmpty()) {
	        throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
	    }
	    this.nome = nome;
	}
	public double getCusto() {
		return custo;
	}
	public void setCusto(double custo) {
		if (custo < 0) {
			throw new IllegalArgumentException("O custo não pode ser negativo.");
		}	
		this.custo = custo;
	}
	public double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(double quantidade) {
		if (quantidade < 0) {
			throw new IllegalArgumentException("A quantidade não pode ser negativa.");
		}
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
		if (lote < 0) {
			throw new IllegalArgumentException("O lote não pode ser negativo.");
		}
		this.lote = lote;
	}
	public LocalDate getDataValidade() {
		return dataValidade;
	}
	public void setDataValidade(LocalDate dataValidade) {
		this.dataValidade = dataValidade;
	}
	
	protected Produto(String nome, double custo, double quantidade, int lote, LocalDate dataValidade, String unidadeMedida) {
		setNome(nome);
		setCusto(custo);
		setQuantidade(quantidade);
		setLote(lote);
		setDataValidade(dataValidade);
	}
	
	protected Produto(int id, String nome, double custo, double quantidade, int lote, LocalDate dataValidade) {
		setId(id);
		setNome(nome);
		setCusto(custo);
		setQuantidade(quantidade);
		setLote(lote);
		setDataValidade(dataValidade);
	}
	
	public abstract void exibirDetalhes();
	
}

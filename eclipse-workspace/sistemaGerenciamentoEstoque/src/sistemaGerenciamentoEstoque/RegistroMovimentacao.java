package sistemaGerenciamentoEstoque;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class RegistroMovimentacao {
    private int id;
    
    private Produto produto;
    private Funcionario responsavel;
    
    private String tipoMovimentacao;
    private double quantidade;
    private LocalDateTime dataHora;

    public RegistroMovimentacao(Produto produto, Funcionario responsavel, String tipoMovimentacao, double quantidade) {
        this.produto = produto;
        this.responsavel = responsavel;
        this.tipoMovimentacao = tipoMovimentacao;
        this.quantidade = quantidade;
        this.dataHora = LocalDateTime.now();
    }

    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public Funcionario getResponsavel() {
		return responsavel;
	}
	public void setResponsavel(Funcionario responsavel) {
		this.responsavel = responsavel;
	}
	public String getTipoMovimentacao() {
		return tipoMovimentacao;
	}
	public void setTipoMovimentacao(String tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}
	public double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}
	public LocalDateTime getDataHora() {
		return dataHora;
	}
	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}
	
	
	public void exibirDetalhes() {
		
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		
        System.out.println("--- Registro de Movimentação ---");
        System.out.println("Data/Hora: " + dataHora.format(formatador));
        System.out.println("Tipo: " + tipoMovimentacao);
        System.out.println("Produto: " + produto.getNome());
        System.out.println("Responsável: " + responsavel.getNome());
        System.out.println("Quantidade: " + quantidade);
        System.out.println("--------------------------------");
    }
}

package sistemaGerenciamentoEstoque;
import java.time.LocalDate;

public class Maquinario extends Produto{
	private int numeroSerie;
	private String marca;
	private int voltagem;
	
	public Maquinario(String nome, double custo, double quantidade, int lote, LocalDate dataValidade, String unidadeMedida, int numeroSerie, String marca, int voltagem) {
		super(nome, custo, quantidade, lote, dataValidade, unidadeMedida);
		this.numeroSerie = numeroSerie;
		this.marca = marca;
		this.voltagem = voltagem;
	}

	public int getNumeroSerie() {
		return numeroSerie;
	}
	public void setNumeroSerie(int numeroSerie) {
		this.numeroSerie = numeroSerie;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public int getVoltagem() {
		return voltagem;
	}
	public void setVoltagem(int voltagem) {
		this.voltagem = voltagem;
	}
	@Override
	public void exibirDetalhes() {
		System.out.println("--- Detalhes do MAQUINARIO ---");
		System.out.println("ID: " + getId());
		System.out.println("Nome: " + getNome());
		System.out.println("Numero de Serie: " + getNumeroSerie());
		System.out.println("Marca: " + getMarca());
		System.out.println("Voltagem: " + getVoltagem());
		System.out.println("Quantidade: " + getQuantidade() + getUnidadeMedida());
		System.out.println("Custo: R$ " + getCusto());
		System.out.println("Lote: " + getLote());
		System.out.println("Data de Validade: " + getDataValidade());
		System.out.println("-----------------------");
	}
}
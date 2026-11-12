package sistemaGerenciamentoEstoque;
import java.time.LocalDate;

public class Epi extends Produto{
	private String tamanho;
	
	public Epi(String nome, double custo, double quantidade, int lote, LocalDate dataValidade, String tamanho, String unidadeMedida) {
		super(nome, custo, quantidade, lote, dataValidade, unidadeMedida);
		setTamanho(tamanho);
	}

	public String getTamanho() {
		return tamanho;
	}
	
	public void setTamanho(String tamanho) {
		String t = tamanho.toUpperCase();
		if(t.equals("PP") || t.equals("P") || t.equals("M") || t.equals("G") || t.equals("GG")) {
			this.tamanho = t;
		}else {
			throw new IllegalArgumentException("Tamanho inválido. Aceitos apenas: PP, P, M, G ou GG.");
		}
	}
	
	@Override
	public void exibirDetalhes() {
		System.out.println("--- Detalhes do EPI ---");
		System.out.println("Nome: " + getNome());
		System.out.println("Tamanho: " + getTamanho());
		System.out.println("Quantidade: " + getQuantidade() + getUnidadeMedida());
		System.out.println("Custo: R$ " + getCusto());
		System.out.println("Lote: " + getLote());
		System.out.println("Data de Validade: " + getDataValidade());
		System.out.println("-----------------------");
	}
}

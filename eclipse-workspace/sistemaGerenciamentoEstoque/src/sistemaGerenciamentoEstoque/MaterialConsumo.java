package sistemaGerenciamentoEstoque;
import java.time.LocalDate;

public class MaterialConsumo extends Produto{
	
	public MaterialConsumo(String nome, double custo, double quantidade, int lote, LocalDate dataValidade, String unidadeMedida) {
		super(nome, custo, quantidade, lote, dataValidade, unidadeMedida);
	}
	
	@Override
	public void exibirDetalhes() {
		System.out.println("--- Detalhes do MAQUINARIO ---");
		System.out.println("Nome: " + getNome());
		System.out.println("Quantidade: " + getQuantidade() + getUnidadeMedida());
		System.out.println("Custo: R$ " + getCusto());
		System.out.println("Lote: " + getLote());
		System.out.println("Data de Validade: " + getDataValidade());
		System.out.println("-----------------------");
	}
}

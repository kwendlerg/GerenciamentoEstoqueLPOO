package sistemaGerenciamentoEstoque;
import java.time.LocalDate;

public class Almoxarife extends Funcionario{
	
	public Almoxarife(String nome, LocalDate dataAdmissao) {
		super(nome, dataAdmissao);
	}
	
	@Override
	public void exibirDetalhes() {
		System.out.println("--- Detalhes do MAQUINARIO ---");
		System.out.println("ID: " + getId());
		System.out.println("Nome: " + getNome());
		System.out.println("Data de Admissão; " + getDataAdmissao());
		System.out.println("-----------------------");
	}
}

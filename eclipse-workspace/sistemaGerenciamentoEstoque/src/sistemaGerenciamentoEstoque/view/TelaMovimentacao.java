package sistemaGerenciamentoEstoque.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import sistemaGerenciamentoEstoque.Adm;
import sistemaGerenciamentoEstoque.Funcionario;
import sistemaGerenciamentoEstoque.MovimentacaoController;
import sistemaGerenciamentoEstoque.Produto;
import sistemaGerenciamentoEstoque.ProdutoController;
import sistemaGerenciamentoEstoque.RegistroMovimentacao;
import sistemaGerenciamentoEstoque.exception.EstoqueException;
import sistemaGerenciamentoEstoque.exception.EstoqueInsuficienteException;
import sistemaGerenciamentoEstoque.exception.PersistenciaException;
import sistemaGerenciamentoEstoque.exception.ProdutoNaoEncontradoException;
import sistemaGerenciamentoEstoque.exception.ProdutoVencidoException;

public class TelaMovimentacao extends JFrame {

	private static final long serialVersionUID = 1L;

	private JComboBox<String> cmbProduto;
	private JTextField txtQuantidade;
	private JButton btnEntrada;
	private JButton btnSaida;
	private JTable tblHistorico;
	private DefaultTableModel modelo;

	private ArrayList<Produto> produtos;
	private ProdutoController produtoController;
	private MovimentacaoController movimentacaoController;

	// Guarda a ação de atualização enviada pela tela de estoque.
	private final Runnable aoMovimentar;

	public TelaMovimentacao(Runnable aoMovimentar) {
		this.aoMovimentar = aoMovimentar;
		produtoController = new ProdutoController();
		movimentacaoController = new MovimentacaoController();

		setTitle("Movimentação de Estoque");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(750, 450);
		setLocationRelativeTo(null);

		Funcionario usuario = TelaLogin.usuarioLogado;
		String tipoUsuario = "ALMOXARIFE";
		if (usuario instanceof Adm) {
			tipoUsuario = "ADM";
		}

		JPanel painelTopo = new JPanel(new BorderLayout());
		painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

		JLabel lblUsuario = new JLabel("Responsável: " + usuario.getNome() + " (" + tipoUsuario + ")");
		painelTopo.add(lblUsuario, BorderLayout.NORTH);

		JPanel painelCampos = new JPanel(new FlowLayout(FlowLayout.LEFT));
		painelCampos.add(new JLabel("Produto:"));
		cmbProduto = new JComboBox<String>();
		painelCampos.add(cmbProduto);

		painelCampos.add(new JLabel("Quantidade:"));
		txtQuantidade = new JTextField(6);
		painelCampos.add(txtQuantidade);

		btnEntrada = new JButton("Registrar entrada");
		btnSaida = new JButton("Registrar saída");
		painelCampos.add(btnEntrada);
		painelCampos.add(btnSaida);
		painelTopo.add(painelCampos, BorderLayout.CENTER);

		String[] colunas = { "Data/Hora", "Tipo", "Produto", "Quantidade", "Responsável" };
		modelo = new DefaultTableModel(colunas, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int linha, int coluna) {
				return false;
			}
		};
		tblHistorico = new JTable(modelo);

		JPanel painelHistorico = new JPanel(new BorderLayout());
		painelHistorico.setBorder(BorderFactory.createTitledBorder("Histórico de movimentações"));
		painelHistorico.add(new JScrollPane(tblHistorico), BorderLayout.CENTER);

		add(painelTopo, BorderLayout.NORTH);
		add(painelHistorico, BorderLayout.CENTER);

		btnEntrada.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrar("ENTRADA");
			}
		});

		btnSaida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrar("SAIDA");
			}
		});

		carregarProdutos();
		carregarHistorico();
	}

	private void carregarProdutos() {
		cmbProduto.removeAllItems();
		try {
			produtos = produtoController.listarProdutos();
			for (Produto produto : produtos) {
				cmbProduto.addItem(produto.getId() + " - " + produto.getNome()
						+ " (estoque: " + produto.getQuantidade() + ")");
			}
		} catch (SQLException e) {
			produtos = new ArrayList<Produto>();
			JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
		}
	}

	private void carregarHistorico() {
		modelo.setRowCount(0);
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		try {
			ArrayList<RegistroMovimentacao> registros = movimentacaoController.listarMovimentacoes();
			for (RegistroMovimentacao registro : registros) {
				String nomeProduto = "(produto excluído)";
				if (registro.getProduto() != null) {
					nomeProduto = registro.getProduto().getNome();
				}

				String nomeResponsavel = "(funcionário excluído)";
				if (registro.getResponsavel() != null) {
					nomeResponsavel = registro.getResponsavel().getNome();
				}

				modelo.addRow(new Object[] {
						registro.getDataHora().format(formato),
						registro.getTipoMovimentacao(),
						nomeProduto,
						registro.getQuantidade(),
						nomeResponsavel
				});
			}
		} catch (PersistenciaException e) {
			JOptionPane.showMessageDialog(this, "Erro ao carregar histórico: " + e.getMessage());
		}
	}

	private void registrar(String tipo) {
		int indice = cmbProduto.getSelectedIndex();
		if (indice < 0) {
			JOptionPane.showMessageDialog(this, "Cadastre um produto antes.");
			return;
		}

		double quantidade;
		try {
			quantidade = Double.parseDouble(txtQuantidade.getText().trim().replace(",", "."));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Digite uma quantidade válida.");
			return;
		}

		Produto produto = produtos.get(indice);
		Funcionario usuario = TelaLogin.usuarioLogado;

		try {
			if (tipo.equals("ENTRADA")) {
				movimentacaoController.registrarEntrada(produto, usuario, quantidade);
			} else {
				movimentacaoController.registrarSaida(produto, usuario, quantidade);
			}

			// Atualiza a tabela principal assim que o controlador conclui a entrada ou saída.
			aoMovimentar.run();
			JOptionPane.showMessageDialog(this, "Movimentação registrada!");
			txtQuantidade.setText("");
			carregarProdutos();
			cmbProduto.setSelectedIndex(indice);
			carregarHistorico();

		} catch (EstoqueInsuficienteException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Estoque insuficiente", JOptionPane.WARNING_MESSAGE);
		} catch (ProdutoVencidoException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Produto vencido", JOptionPane.WARNING_MESSAGE);
		} catch (ProdutoNaoEncontradoException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Produto não encontrado", JOptionPane.WARNING_MESSAGE);
			carregarProdutos();
		} catch (PersistenciaException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Erro no banco de dados", JOptionPane.ERROR_MESSAGE);
		} catch (EstoqueException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
}

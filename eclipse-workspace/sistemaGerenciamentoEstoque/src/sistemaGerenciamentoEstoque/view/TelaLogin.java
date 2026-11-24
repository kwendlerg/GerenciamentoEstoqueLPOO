package sistemaGerenciamentoEstoque.view;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sistemaGerenciamentoEstoque.Adm;
import sistemaGerenciamentoEstoque.Almoxarife;
import sistemaGerenciamentoEstoque.Funcionario;
import sistemaGerenciamentoEstoque.FuncionarioController;

public class TelaLogin extends JFrame {

	private static final long serialVersionUID = 1L;

	public static Funcionario usuarioLogado;

	private JTextField txtId;
	private JTextField txtNome;
	private JButton btnEntrar;
	private JButton btnCadastrar;
	private FuncionarioController controller;

	public TelaLogin() {
		controller = new FuncionarioController();

		setTitle("Login - Gerenciamento de Estoque");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Divide a janela em cabeçalho e formulário.
        JPanel raiz = new JPanel(new java.awt.BorderLayout());
        raiz.setBackground(VisualEstoque.FUNDO);
        raiz.add(VisualEstoque.cabecalho("Gestão de Estoque"), java.awt.BorderLayout.NORTH);

        JPanel margem = new JPanel(new java.awt.BorderLayout());
        margem.setOpaque(false);
        margem.setBorder(BorderFactory.createEmptyBorder(18, 22, 22, 22));
        // Alinha os rótulos, campos e botões numa coluna.
        JPanel painel = VisualEstoque.cartao(new GridLayout(0, 1, 0, 8));

        JLabel rotuloId = new JLabel("ID do funcionário");
        txtId = new JTextField(28);
        VisualEstoque.campo(txtId, rotuloId);
        painel.add(rotuloId);
        painel.add(txtId);

        JLabel rotuloNome = new JLabel("Nome");
        txtNome = new JTextField(28);
        VisualEstoque.campo(txtNome, rotuloNome);
        painel.add(rotuloNome);
        painel.add(txtNome);

        btnEntrar = new JButton("Entrar");
        btnCadastrar = new JButton("Primeiro acesso");
        VisualEstoque.botao(btnEntrar, VisualEstoque.VERDE, java.awt.Color.WHITE);
        VisualEstoque.botao(btnCadastrar, VisualEstoque.FUNDO, VisualEstoque.MARINHO);
        painel.add(btnEntrar);
        painel.add(btnCadastrar);

        margem.add(painel);
        raiz.add(margem);
        setContentPane(raiz);
        pack();
        setLocationRelativeTo(null);

		// O clique em Entrar chama o método que valida o login.
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				entrar();
			}
		});

		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				primeiroAcesso();
			}
		});

		// Permite entrar pressionando Enter.
		getRootPane().setDefaultButton(btnEntrar);
	}

	private void entrar() {
		String textoId = txtId.getText().trim();
		String nome = txtNome.getText().trim();

		if (textoId.isEmpty() || nome.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Preencha o ID e o nome.");
			return;
		}

		int id;
		try {
			id = Integer.parseInt(textoId);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "O ID deve ser um número.");
			return;
		}

		try {
			// Consulta o funcionário pelo ID e compara o nome cadastrado.
			Funcionario funcionario = controller.buscarFuncionario(id);

			if (funcionario == null || !funcionario.getNome().equalsIgnoreCase(nome)) {
				JOptionPane.showMessageDialog(this, "ID ou nome incorretos.");
				return;
			}

			// Guarda o usuário da sessão para a tela aplicar as permissões.
			usuarioLogado = funcionario;
			JOptionPane.showMessageDialog(this, "Bem-vindo, " + funcionario.getNome() + "!");

			new TelaEstoque().setVisible(true);
			dispose();

		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Erro ao acessar o banco: " + e.getMessage());
		}
	}

	private void primeiroAcesso() {
		try {
			if (!controller.listarFuncionarios().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Já existem funcionários cadastrados.\n"
						+ "Peça para um ADM cadastrar você no menu Funcionários.");
				return;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Erro ao acessar o banco: " + e.getMessage());
			return;
		}

		JOptionPane.showMessageDialog(this, "Nenhum funcionário cadastrado.\nCadastre o primeiro administrador (ADM).");

		Funcionario cadastrado = cadastrarFuncionario(this, true);
		if (cadastrado != null) {
			txtId.setText(String.valueOf(cadastrado.getId()));
			txtNome.setText(cadastrado.getNome());
		}
	}

	public static Funcionario cadastrarFuncionario(Component pai, boolean somenteAdm) {
		String nome = JOptionPane.showInputDialog(pai, "Nome do funcionário:");
		if (nome == null || nome.trim().isEmpty()) {
			return null;
		}

		String tipo = "ADM";
		if (!somenteAdm) {
			String[] tipos = { "ADM", "ALMOXARIFE" };
			JComboBox<String> comboTipo = new JComboBox<String>(tipos);
			int opcao = JOptionPane.showConfirmDialog(pai, comboTipo, "Tipo do funcionário", JOptionPane.OK_CANCEL_OPTION);
			if (opcao != JOptionPane.OK_OPTION) {
				return null;
			}
			tipo = comboTipo.getSelectedItem().toString();
		}

		Funcionario funcionario;
		if (tipo.equals("ADM")) {
			funcionario = new Adm(nome.trim(), LocalDate.now());
		} else {
			funcionario = new Almoxarife(nome.trim(), LocalDate.now());
		}

		try {
			FuncionarioController controller = new FuncionarioController();
			controller.cadastrarFuncionario(funcionario);

			ArrayList<Funcionario> lista = controller.listarFuncionarios();
			Funcionario cadastrado = lista.get(lista.size() - 1);

			JOptionPane.showMessageDialog(pai, "Funcionário cadastrado!\nNome: " + cadastrado.getNome()
					+ "\nTipo: " + tipo + "\nID: " + cadastrado.getId()
					+ "\nUse o ID e o nome para entrar.");
			return cadastrado;

		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(pai, e.getMessage());
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(pai, "Erro ao cadastrar: " + e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new TelaLogin().setVisible(true);
			}
		});
	}
}

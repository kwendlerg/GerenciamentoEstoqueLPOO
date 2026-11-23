package sistemaGerenciamentoEstoque.view;

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
		setSize(360, 180);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel painel = new JPanel(new GridLayout(3, 2, 10, 10));
		painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		painel.add(new JLabel("ID do funcionário:"));
		txtId = new JTextField();
		painel.add(txtId);

		painel.add(new JLabel("Nome:"));
		txtNome = new JTextField();
		painel.add(txtNome);

		btnEntrar = new JButton("Entrar");
		btnCadastrar = new JButton("Cadastrar");
		painel.add(btnEntrar);
		painel.add(btnCadastrar);

		add(painel);

		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				entrar();
			}
		});

		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cadastrar();
			}
		});

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
			Funcionario funcionario = controller.buscarFuncionario(id);

			if (funcionario == null || !funcionario.getNome().equalsIgnoreCase(nome)) {
				JOptionPane.showMessageDialog(this, "ID ou nome incorretos.");
				return;
			}

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

	private void cadastrar() {
		String nome = JOptionPane.showInputDialog(this, "Nome do funcionário:");
		if (nome == null || nome.trim().isEmpty()) {
			return;
		}

		String[] tipos = { "ADM", "ALMOXARIFE" };
		JComboBox<String> comboTipo = new JComboBox<String>(tipos);
		int opcao = JOptionPane.showConfirmDialog(this, comboTipo, "Tipo do funcionário", JOptionPane.OK_CANCEL_OPTION);
		if (opcao != JOptionPane.OK_OPTION) {
			return;
		}

		Funcionario funcionario;
		if (comboTipo.getSelectedItem().equals("ADM")) {
			funcionario = new Adm(nome.trim(), LocalDate.now());
		} else {
			funcionario = new Almoxarife(nome.trim(), LocalDate.now());
		}

		try {
			controller.cadastrarFuncionario(funcionario);

			ArrayList<Funcionario> lista = controller.listarFuncionarios();
			Funcionario cadastrado = lista.get(lista.size() - 1);

			JOptionPane.showMessageDialog(this, "Funcionário cadastrado!\nSeu ID é: " + cadastrado.getId()
					+ "\nUse o ID e o nome para entrar.");

			txtId.setText(String.valueOf(cadastrado.getId()));
			txtNome.setText(cadastrado.getNome());

		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new TelaLogin().setVisible(true);
			}
		});
	}
}

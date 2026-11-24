package sistemaGerenciamentoEstoque.view;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import sistemaGerenciamentoEstoque.Adm;
import sistemaGerenciamentoEstoque.Almoxarife;
import sistemaGerenciamentoEstoque.Funcionario;
import sistemaGerenciamentoEstoque.FuncionarioController;

public class TelaLogin extends JFrame {

	private static final long serialVersionUID = 1L;

	public static Funcionario usuarioLogado;

	private JTextField txtLogin;
	private JPasswordField txtSenha;
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

        JLabel rotuloLogin = new JLabel("Login");
        txtLogin = new JTextField(28);
        VisualEstoque.campo(txtLogin, rotuloLogin);
        painel.add(rotuloLogin);
        painel.add(txtLogin);

        // A senha aparece como asteriscos enquanto é digitada.
        JLabel rotuloSenha = new JLabel("Senha");
        txtSenha = new JPasswordField(28);
        txtSenha.setEchoChar('*');
        VisualEstoque.campo(txtSenha, rotuloSenha);
        painel.add(rotuloSenha);
        painel.add(txtSenha);

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
		String login = txtLogin.getText().trim();
		String senha = new String(txtSenha.getPassword());

		if (login.isEmpty() || senha.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Preencha o login e a senha.");
			return;
		}

		try {
			// Confere o login e a senha no banco.
			Funcionario funcionario = controller.autenticar(login, senha);

			if (funcionario == null) {
				JOptionPane.showMessageDialog(this, "Login ou senha incorretos.");
				txtSenha.setText("");
				txtSenha.requestFocusInWindow();
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
			if (controller.existeUsuarioComLogin()) {
				JOptionPane.showMessageDialog(this, "Já existem usuários cadastrados.\n"
						+ "Peça para um ADM cadastrar você no menu Funcionários.");
				return;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Erro ao acessar o banco: " + e.getMessage());
			return;
		}

		JOptionPane.showMessageDialog(this, "Nenhum usuário cadastrado.\nCadastre o primeiro administrador (ADM).");

		Funcionario cadastrado = cadastrarFuncionario(this, true);
		if (cadastrado != null) {
			txtLogin.setText(cadastrado.getLogin());
			txtSenha.setText("");
			txtSenha.requestFocusInWindow();
		}
	}

	public static Funcionario cadastrarFuncionario(Component pai, boolean somenteAdm) {
		JTextField campoNome = new JTextField();
		JTextField campoLogin = new JTextField();
		JPasswordField campoSenha = new JPasswordField();
		JPasswordField campoConfirmar = new JPasswordField();
		campoSenha.setEchoChar('*');
		campoConfirmar.setEchoChar('*');

		String[] tipos = { "ADM", "ALMOXARIFE" };
		JComboBox<String> comboTipo = new JComboBox<String>(tipos);
		if (somenteAdm) {
			comboTipo.setEnabled(false);
		}

		Object[] campos = {
				"Nome:", campoNome,
				"Login:", campoLogin,
				"Senha (mínimo 4 caracteres):", campoSenha,
				"Confirmar senha:", campoConfirmar,
				"Tipo:", comboTipo
		};

		int opcao = JOptionPane.showConfirmDialog(pai, campos, "Cadastrar funcionário", JOptionPane.OK_CANCEL_OPTION);
		if (opcao != JOptionPane.OK_OPTION) {
			return null;
		}

		String nome = campoNome.getText().trim();
		String login = campoLogin.getText().trim();
		String senha = new String(campoSenha.getPassword());
		String confirmar = new String(campoConfirmar.getPassword());
		String tipo = comboTipo.getSelectedItem().toString();

		if (nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
			JOptionPane.showMessageDialog(pai, "Preencha nome, login e senha.");
			return null;
		}

		if (!senha.equals(confirmar)) {
			JOptionPane.showMessageDialog(pai, "As senhas não conferem.");
			return null;
		}

		Funcionario funcionario;
		if (tipo.equals("ADM")) {
			funcionario = new Adm(nome, LocalDate.now());
		} else {
			funcionario = new Almoxarife(nome, LocalDate.now());
		}
		funcionario.setLogin(login);
		funcionario.setSenha(senha);

		try {
			FuncionarioController controller = new FuncionarioController();
			controller.cadastrarFuncionario(funcionario);

			Funcionario cadastrado = controller.buscarPorLogin(login);

			JOptionPane.showMessageDialog(pai, "Funcionário cadastrado!\nNome: " + cadastrado.getNome()
					+ "\nTipo: " + tipo + "\nLogin: " + cadastrado.getLogin()
					+ "\nUse o login e a senha para entrar.");
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

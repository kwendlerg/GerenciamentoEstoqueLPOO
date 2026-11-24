/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemaGerenciamentoEstoque.view;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Micro
 */
public class TelaEstoque extends javax.swing.JFrame {

    /**
     * Creates new form TelaEstoque
     */
   
public TelaEstoque() {
    javax.swing.UIManager.put("OptionPane.yesButtonText", "Sim");
    javax.swing.UIManager.put("OptionPane.noButtonText", "Não");

    initComponents();
    configurarUsuario();
    organizarTela();
    carregarProdutos();
}

    // Mostra quem está logado, cria o menu e aplica as permissões de cada tipo
    private void configurarUsuario() {
        sistemaGerenciamentoEstoque.Funcionario usuario = TelaLogin.usuarioLogado;
        boolean ehAdm = usuario instanceof sistemaGerenciamentoEstoque.Adm;

        String tipoUsuario = "ALMOXARIFE";
        if (ehAdm) {
            tipoUsuario = "ADM";
        }
        setTitle("Gerenciamento de Estoque - " + usuario.getNome() + " (" + tipoUsuario + ")");

        javax.swing.JMenuBar barraMenu = new javax.swing.JMenuBar();

        javax.swing.JMenu menuMovimentacao = new javax.swing.JMenu("Movimentação");
        javax.swing.JMenuItem itemMovimentar = new javax.swing.JMenuItem("Entrada / Saída de estoque");
        itemMovimentar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Passa o método que recarrega a tabela após uma movimentação.
                TelaMovimentacao tela = new TelaMovimentacao(TelaEstoque.this::carregarProdutos);
                tela.setVisible(true);
            }
        });
        menuMovimentacao.add(itemMovimentar);
        barraMenu.add(menuMovimentacao);

        if (ehAdm) {
            javax.swing.JMenu menuFuncionarios = new javax.swing.JMenu("Funcionários");
            javax.swing.JMenuItem itemCadastrar = new javax.swing.JMenuItem("Cadastrar funcionário");
            itemCadastrar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    TelaLogin.cadastrarFuncionario(TelaEstoque.this, false);
                }
            });
            menuFuncionarios.add(itemCadastrar);
            barraMenu.add(menuFuncionarios);
        }

        javax.swing.JMenu menuConta = new javax.swing.JMenu("Conta");
        javax.swing.JMenuItem itemSair = new javax.swing.JMenuItem("Sair / trocar usuário");
        itemSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TelaLogin.usuarioLogado = null;
                dispose();
                new TelaLogin().setVisible(true);
            }
        });
        menuConta.add(itemSair);
        barraMenu.add(menuConta);

        setJMenuBar(barraMenu);

        // Almoxarife só movimenta o estoque: não cadastra nem exclui produtos
        if (!ehAdm) {
            btnMostrar.setEnabled(false);
            btnExcluir.setEnabled(false);
            btnMostrar.setToolTipText("Apenas ADM pode cadastrar produtos.");
            btnExcluir.setToolTipText("Apenas ADM pode excluir produtos.");
        }

        pack();
        setLocationRelativeTo(null);
    }


    private void organizarTela() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(VisualEstoque.FUNDO);
        // Coloca os menus existentes ao lado do título.
        JPanel topo = VisualEstoque.cabecalho("Gestão de Estoque");
        JMenuBar menu = getJMenuBar();
        setJMenuBar(null);
        topo.add(menu, BorderLayout.EAST);
        raiz.add(topo, BorderLayout.NORTH);

        JPanel conteudo = new JPanel(new BorderLayout(16, 0));
        conteudo.setOpaque(false);
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 18, 18, 18));

        // Monta o formulário no lado esquerdo da janela.
        JPanel cadastro = VisualEstoque.cartao(new BorderLayout(0, 12));
        cadastro.add(VisualEstoque.titulo("Cadastrar produto", 18, VisualEstoque.MARINHO), BorderLayout.NORTH);
        JPanel campos = new JPanel(new GridBagLayout());
        campos.setOpaque(false);
        GridBagConstraints pos = new GridBagConstraints();
        pos.anchor = GridBagConstraints.WEST;
        pos.insets = new Insets(4, 0, 4, 8);
        JLabel[] rotulos = {jLabel1, jLabel5, jLabel2, jLabel3, jLabel4, jLabel6, jLabel7};
        JComponent[] entradas = {txtNome, cmbTipo, txtCusto, txtQuantidade, cmbUnidade, txtLote, txtValidade};
        jLabel7.setText("Validade");
        txtValidade.setToolTipText("Formato dd/MM/aaaa");
        // Alinha cada rótulo com seu campo na mesma linha.
        for (int i = 0; i < entradas.length; i++) {
            pos.gridy = i;
            pos.gridx = 0;
            pos.weightx = 0;
            pos.fill = GridBagConstraints.NONE;
            campos.add(rotulos[i], pos);
            pos.gridx = 1;
            pos.weightx = 1;
            pos.fill = GridBagConstraints.HORIZONTAL;
            if (entradas[i] instanceof JTextField campo) {
                campo.setColumns(13);
                VisualEstoque.campo(campo, rotulos[i]);
            } else {
                rotulos[i].setLabelFor(entradas[i]);
            }
            campos.add(entradas[i], pos);
        }
        cadastro.add(campos, BorderLayout.CENTER);
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        acoes.setOpaque(false);
        VisualEstoque.botao(btnMostrar, VisualEstoque.VERDE, Color.WHITE);
        VisualEstoque.botao(btnLimpar, VisualEstoque.FUNDO, VisualEstoque.MARINHO);
        acoes.add(btnMostrar);
        acoes.add(btnLimpar);
        cadastro.add(acoes, BorderLayout.SOUTH);
        conteudo.add(cadastro, BorderLayout.WEST);

        // Reserva o lado direito para a busca e a tabela.
        JPanel produtos = VisualEstoque.cartao(new BorderLayout(0, 10));
        JPanel busca = new JPanel(new BorderLayout(8, 0));
        busca.setOpaque(false);
        VisualEstoque.campo(txtBusca, jLabel8);
        VisualEstoque.botao(btnBuscar, VisualEstoque.VERDE, Color.WHITE);
        busca.add(jLabel8, BorderLayout.WEST);
        busca.add(txtBusca, BorderLayout.CENTER);
        busca.add(btnBuscar, BorderLayout.EAST);
        JPanel barra = new JPanel(new BorderLayout(0, 8));
        barra.setOpaque(false);
        barra.add(VisualEstoque.titulo("Produtos", 18, VisualEstoque.MARINHO), BorderLayout.NORTH);
        barra.add(busca, BorderLayout.SOUTH);
        produtos.add(barra, BorderLayout.NORTH);

        // Aumenta a altura das linhas e destaca a seleção.
        tblProdutos.setRowHeight(28);
        tblProdutos.setSelectionBackground(new Color(0xD5EFEA));
        tblProdutos.setSelectionForeground(VisualEstoque.MARINHO);
        tblProdutos.setShowVerticalLines(false);
        tblProdutos.setGridColor(VisualEstoque.BORDA);
        tblProdutos.setFillsViewportHeight(true);
        tblProdutos.getTableHeader().setPreferredSize(new Dimension(0, 30));
        jScrollPane1.setPreferredSize(new Dimension(650, 325));
        jScrollPane1.setBorder(BorderFactory.createLineBorder(VisualEstoque.BORDA));
        produtos.add(jScrollPane1, BorderLayout.CENTER);

        JPanel exclusao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        exclusao.setOpaque(false);
        VisualEstoque.botao(btnExcluir, new Color(0xFCE8EC), VisualEstoque.VERMELHO);
        exclusao.add(btnExcluir);
        produtos.add(exclusao, BorderLayout.SOUTH);
        conteudo.add(produtos, BorderLayout.CENTER);
        raiz.add(conteudo, BorderLayout.CENTER);
        setContentPane(raiz);
        pack();
        setMinimumSize(new Dimension(860, getHeight()));
        setLocationRelativeTo(null);
    }

    // Consulta os produtos salvos e atualiza a tabela.
    private void carregarProdutos() {
        sistemaGerenciamentoEstoque.ProdutoController controller =
            new sistemaGerenciamentoEstoque.ProdutoController();

        try {
            java.util.ArrayList<sistemaGerenciamentoEstoque.Produto> produtos =
                controller.listarProdutos();

            javax.swing.table.DefaultTableModel modelo =
                (javax.swing.table.DefaultTableModel) tblProdutos.getModel();
            modelo.setRowCount(0);

            for (sistemaGerenciamentoEstoque.Produto produto : produtos) {
                String tipo;
                if (produto instanceof sistemaGerenciamentoEstoque.Epi) {
                    tipo = "EPI";
                } else if (produto instanceof sistemaGerenciamentoEstoque.Maquinario) {
                    tipo = "Maquinário";
                } else {
                    tipo = "Material de consumo";
                }

                String validade = "";
                if (produto.getDataValidade() != null) {
                    validade = produto.getDataValidade().format(
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    );
                }

                modelo.addRow(new Object[]{
                    produto.getId(),
                    produto.getNome(),
                    tipo,
                    produto.getCusto(),
                    produto.getQuantidade(),
                    produto.getUnidadeMedida(),
                    produto.getLote(),
                    validade,
                    "Cadastrado"
                });
            }
        } catch (java.sql.SQLException erro) {
            javax.swing.JOptionPane.showMessageDialog(
                this, "Erro ao carregar produtos: " + erro.getMessage()
            );
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        btnLimpar = new javax.swing.JButton();
        btnMostrar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtCusto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtQuantidade = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbUnidade = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtLote = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtValidade = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProdutos = new javax.swing.JTable();
        btnExcluir = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtBusca = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nome:");

        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        btnMostrar.setText("Adicionar produto");
        btnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarActionPerformed(evt);
            }
        });

        jLabel2.setText("Custo:");

        txtCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustoActionPerformed(evt);
            }
        });

        jLabel3.setText("Quantidade:");

        txtQuantidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantidadeActionPerformed(evt);
            }
        });

        jLabel4.setText("Unidade:");

        cmbUnidade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione...", "un", "kg", "L", "m" }));

        jLabel5.setText("Tipo:");

        cmbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione...", "EPI", "Maquinário", "Material de consumo" }));

        jLabel6.setText("Lote:");

        txtLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoteActionPerformed(evt);
            }
        });

        jLabel7.setText("Validade (dd/MM/aaaa):");

        txtValidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValidadeActionPerformed(evt);
            }
        });

        tblProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Tipo", "Custo", "Quantidade", "Unidade", "Lote", "Validade", "Situação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.String.class,
                java.lang.Integer.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false,
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblProdutos);

        btnExcluir.setText("Excluir selecionado");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        jLabel8.setText("Buscar por nome:");

        txtBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscaActionPerformed(evt);
            }
        });

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtValidade)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtLote, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnBuscar)
                                .addGap(49, 49, 49))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnExcluir)
                                    .addComponent(btnMostrar))
                                .addGap(18, 18, 18)))))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(btnLimpar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cmbUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtLote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtValidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMostrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtBusca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnExcluir))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpar)
                .addContainerGap(71, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
 txtNome.setText("");
txtCusto.setText("");
txtLote.setText("");
txtValidade.setText("");
txtQuantidade.setText("");//Identifica o campo e altera o txt nele   
// Volta para a primeira opção
cmbUnidade.setSelectedIndex(0);
// Volta o tipo para "Selecione..."
cmbTipo.setSelectedIndex(0);
txtNome.requestFocusInWindow();//Volta para o primeiro para escrever nele     
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarActionPerformed
   // TODO add your handling code here:
// Lê o nome sem espaços nas pontas
String nome = txtNome.getText().trim();

// Impede nome vazio
if (nome.isEmpty()) {
    javax.swing.JOptionPane.showMessageDialog(this, "Digite o nome.");
    return;
}
// Impede continuar sem escolher uma unidade
if (cmbUnidade.getSelectedIndex() == 0) {
    javax.swing.JOptionPane.showMessageDialog(this, "Selecione a unidade.");
    return;
}

// Guarda o texto da opção escolhida
String unidade = cmbUnidade.getSelectedItem().toString();
// Exige a escolha do tipo de produto
if (cmbTipo.getSelectedIndex() == 0) {
    javax.swing.JOptionPane.showMessageDialog(this, "Selecione o tipo de produto.");
    return;
}
// Guarda o tipo escolhido
String tipo = cmbTipo.getSelectedItem().toString();
try {
    // Converte o custo digitado em número
    String textoCusto = txtCusto.getText().trim().replace(",", ".");
    double custo = Double.parseDouble(textoCusto);

    // Impede custo negativo ou não finito
    if (!Double.isFinite(custo) || custo < 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Digite um custo válido, zero ou maior.");
        return;
    }
    

    // Converte a quantidade em número
String textoQuantidade = txtQuantidade.getText().trim().replace(",", ".");
double quantidade = Double.parseDouble(textoQuantidade);

// Impede quantidade negativa ou não finita
if (!Double.isFinite(quantidade) || quantidade < 0) {
    javax.swing.JOptionPane.showMessageDialog(
        this, "Digite uma quantidade válida, zero ou maior."
    );
    return;
}
// Converte o lote para um número inteiro
int lote;
// Define o formato e rejeita datas impossíveis
java.time.format.DateTimeFormatter formato =
    java.time.format.DateTimeFormatter.ofPattern("dd/MM/uuuu")
        .withResolverStyle(java.time.format.ResolverStyle.STRICT);

java.time.LocalDate validade;

try {
    // Converte o texto para uma data
    validade = java.time.LocalDate.parse(
        txtValidade.getText().trim(), formato
    );
} catch (java.time.format.DateTimeParseException erroData) {
    // Avisa se a data estiver vazia ou inválida
    javax.swing.JOptionPane.showMessageDialog(
        this, "Digite uma data válida no formato dd/MM/aaaa."
    );
    return;
}
try {
    // Converte o lote em inteiro
    lote = Integer.parseInt(txtLote.getText().trim());
} catch (NumberFormatException erroLote) {
    // Mostra o erro específico do lote
    javax.swing.JOptionPane.showMessageDialog(
        this, "Digite um lote inteiro válido. Exemplo: 123."
    );
    return;
}
// Impede continuar com uma validade anterior a hoje
if (validade.isBefore(java.time.LocalDate.now())) {
    javax.swing.JOptionPane.showMessageDialog(
        this, "O produto está vencido. Confira a validade."
    );
    return;
}

// Impede lote negativo
if (lote < 0) {
    javax.swing.JOptionPane.showMessageDialog(this, "O lote não pode ser negativo.");
    return;
}
// Acessa o modelo que guarda as linhas da tabela
javax.swing.table.DefaultTableModel modelo =
    (javax.swing.table.DefaultTableModel) tblProdutos.getModel();


sistemaGerenciamentoEstoque.Produto produto;
// Cria o tipo de produto escolhido e pede os dados específicos quando necessário.
switch (tipo) {
    case "EPI":
        String[] tamanhos = {"PP", "P", "M", "G", "GG"};
        String tamanho = (String) javax.swing.JOptionPane.showInputDialog(
            this, "Selecione o tamanho do EPI.", "Dados do EPI",
            javax.swing.JOptionPane.QUESTION_MESSAGE, null, tamanhos, "M");
        if (tamanho == null) return;
        produto = new sistemaGerenciamentoEstoque.Epi(
            nome, custo, quantidade, lote, validade, tamanho, unidade);
        break;
    case "Maquinário":
        javax.swing.JTextField serie = new javax.swing.JTextField();
        javax.swing.JTextField marca = new javax.swing.JTextField();
        javax.swing.JTextField voltagem = new javax.swing.JTextField();
        Object[] campos = {"Número de série:", serie, "Marca:", marca, "Voltagem (V):", voltagem};
        if (javax.swing.JOptionPane.showConfirmDialog(this, campos, "Dados do maquinário",
                javax.swing.JOptionPane.OK_CANCEL_OPTION) != javax.swing.JOptionPane.OK_OPTION) return;
        try {
            int numeroSerie = Integer.parseInt(serie.getText().trim());
            int volts = Integer.parseInt(voltagem.getText().trim());
            String textoMarca = marca.getText().trim();
            if (numeroSerie <= 0 || volts <= 0 || textoMarca.isEmpty()) {
                throw new IllegalArgumentException();
            }
            produto = new sistemaGerenciamentoEstoque.Maquinario(
                nome, custo, quantidade, lote, validade, unidade, numeroSerie, textoMarca, volts);
        } catch (IllegalArgumentException erroMaquinario) {
            javax.swing.JOptionPane.showMessageDialog(
                this, "Preencha a marca e informe série e voltagem como inteiros positivos.");
            return;
        }
        break;
    case "Material de consumo":
        produto = new sistemaGerenciamentoEstoque.MaterialConsumo(
            nome, custo, quantidade, lote, validade, unidade);
        break;
    default:
        javax.swing.JOptionPane.showMessageDialog(this, "Selecione um tipo de produto válido.");
        return;
}
sistemaGerenciamentoEstoque.ProdutoController controller =
    new sistemaGerenciamentoEstoque.ProdutoController();

try {
    // Entrega o produto validado ao controlador para gravar no banco.
    controller.cadastrarProduto(produto);

    carregarProdutos();

} catch (java.sql.SQLException erro) {
    javax.swing.JOptionPane.showMessageDialog(
        this, "Erro ao salvar produto: " + erro.getMessage()
    );
    return;
}

// Mostra os dados validados
javax.swing.JOptionPane.showMessageDialog(
    this, "Nome: " + nome
        + "\nTipo: " + tipo
        + "\nCusto: " + custo
        + "\nQuantidade: " + quantidade
        + "\nUnidade: " + unidade
        + "\nLote: " + lote
        + "\nValidade: " + validade.format(formato)
);
// Limpa os campos após fechar a mensagem
btnLimpar.doClick();
} catch (NumberFormatException erro) {
   // Avisa se algum campo numérico estiver inválido
// Orienta sobre os campos numéricos
javax.swing.JOptionPane.showMessageDialog(
    this, "Preencha custo e quantidade com números válidos.\n"
        
);
}
    }//GEN-LAST:event_btnMostrarActionPerformed

    private void txtCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustoActionPerformed

    private void txtQuantidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantidadeActionPerformed

    private void txtLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLoteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoteActionPerformed

    private void txtValidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValidadeActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        int[] linhas = tblProdutos.getSelectedRows();

        if (linhas.length == 0) {
            javax.swing.JOptionPane.showMessageDialog(
                this, "Selecione pelo menos um produto."
            );
            return;
        }

        int resposta = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Excluir " + linhas.length + " produto(s) selecionado(s)?",
            "Confirmar exclusão",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (resposta != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        sistemaGerenciamentoEstoque.ProdutoController controller =
            new sistemaGerenciamentoEstoque.ProdutoController();
        javax.swing.table.DefaultTableModel modelo =
            (javax.swing.table.DefaultTableModel) tblProdutos.getModel();

        try {
            for (int i = 0; i < linhas.length; i++) {
                int linhaModelo = tblProdutos.convertRowIndexToModel(linhas[i]);
                int id = (Integer) modelo.getValueAt(linhaModelo, 0);
                controller.excluirProduto(id);
            }
            carregarProdutos();
            javax.swing.JOptionPane.showMessageDialog(this, "Produto(s) excluído(s).");
        } catch (java.sql.SQLException erro) {
            carregarProdutos();
            javax.swing.JOptionPane.showMessageDialog(
                this, "Erro ao excluir produto: " + erro.getMessage()
            );
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscaActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
// Lê o nome pesquisado
String busca = txtBusca.getText().trim();

// Acessa os dados da tabela
javax.swing.table.DefaultTableModel modelo =
    (javax.swing.table.DefaultTableModel) tblProdutos.getModel();

// Cria o filtro de visualização
javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> filtro =
    new javax.swing.table.TableRowSorter<>(modelo);

tblProdutos.setRowSorter(filtro);

if (busca.isEmpty()) {
    // Busca vazia mostra todos os produtos
    filtro.setRowFilter(null);
} else {
    // Filtra a coluna Nome (0), ignorando maiúsculas e minúsculas
    filtro.setRowFilter(javax.swing.RowFilter.regexFilter(
        "(?iu)" + java.util.regex.Pattern.quote(busca), 1
    ));
}
// Limpa o campo após aplicar a busca
txtBusca.setText("");// TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaEstoque.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaEstoque.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaEstoque.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaEstoque.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnMostrar;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JComboBox<String> cmbUnidade;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblProdutos;
    private javax.swing.JTextField txtBusca;
    private javax.swing.JTextField txtCusto;
    private javax.swing.JTextField txtLote;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtQuantidade;
    private javax.swing.JTextField txtValidade;
    // End of variables declaration//GEN-END:variables
}

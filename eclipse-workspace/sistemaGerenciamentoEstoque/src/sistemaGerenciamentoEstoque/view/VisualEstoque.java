package sistemaGerenciamentoEstoque.view;

import java.awt.*;
import javax.swing.*;

final class VisualEstoque {
    // Centraliza as cores usadas nas telas.
    static final Color MARINHO = new Color(0x14263D);
    static final Color VERDE = new Color(0x00776B);
    static final Color FUNDO = new Color(0xF2F5F9);
    static final Color BORDA = new Color(0xD5DEE9);
    static final Color VERMELHO = new Color(0xA62339);

    private VisualEstoque() {}

    static JLabel titulo(String texto, int tamanho, Color cor) {
        JLabel label = new JLabel(texto);
        label.setFont(label.getFont().deriveFont(Font.BOLD, (float) tamanho));
        label.setForeground(cor);
        return label;
    }

    static JPanel cabecalho(String texto) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(MARINHO);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        painel.add(titulo(texto, 23, Color.WHITE), BorderLayout.WEST);
        return painel;
    }

    // Cria um painel branco com borda e espaço interno.
    static JPanel cartao(LayoutManager layout) {
        JPanel painel = new JPanel(layout);
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        return painel;
    }

    // Aplica o mesmo estilo aos botões das duas telas.
    static void botao(AbstractButton botao, Color fundo, Color texto) {
        botao.setBackground(fundo);
        botao.setForeground(texto);
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(fundo.darker()),
            BorderFactory.createEmptyBorder(9, 12, 9, 12)));
        botao.setFont(botao.getFont().deriveFont(Font.BOLD));
        if (!botao.isEnabled()) {
            botao.setBackground(BORDA);
            botao.setForeground(MARINHO);
        }
    }

    // Associa o rótulo ao campo e adiciona espaço interno para digitação.
    static void campo(JTextField campo, JLabel rotulo) {
        rotulo.setLabelFor(campo);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA),
            BorderFactory.createEmptyBorder(7, 8, 7, 8)));
    }
}

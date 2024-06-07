package view.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class RoundTextField extends JTextField {

    private final int arcWidth = 15;  // Largura do arco das bordas arredondadas
    private final int arcHeight = 15; // Altura do arco das bordas arredondadas
    private final Color shadowColor; // Cor da sombra
    private final int shadowSize = 4; // Tamanho da sombra
    private float borderWidth = 1f; // Largura da borda

    public RoundTextField(int columns, Color shadowColor) {
        super(columns);
        this.shadowColor = shadowColor;
        setOpaque(false); // Permite desenhar um fundo personalizado
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Desenha a sombra
        g2.setColor(shadowColor);
        // Deslocamento da sombra no eixo X
        int shadowOffsetX = 4;
        // Deslocamento da sombra no eixo Y
        int shadowOffsetY = 4;
        g2.fillRoundRect(shadowOffsetX, shadowOffsetY, getWidth() - shadowSize, getHeight() - shadowSize, arcWidth, arcHeight);

        // Desenha o fundo
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - shadowSize, getHeight() - shadowSize, arcWidth, arcHeight);

        // Desenha o texto
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());

        // Define a largura da borda
        g2.setStroke(new BasicStroke(borderWidth));

        g2.drawRoundRect(0, 0, getWidth() - shadowSize, getHeight() - shadowSize, arcWidth, arcHeight);
        g2.dispose();
    }

    @Override
    public void setBorder(Border border) {
        // Ignorar a chamada para evitar substituição do border
    }

    @Override
    public Insets getInsets() {
        // Ajusta os insets para incluir o deslocamento do texto
        Insets insets = super.getInsets();
        int textXOffset = 10;
        return new Insets(insets.top, textXOffset, insets.bottom, insets.right);
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        repaint();
    }
}

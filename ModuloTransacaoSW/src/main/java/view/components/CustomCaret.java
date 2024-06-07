package view.components;

import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class CustomCaret extends DefaultCaret {
    private final Color caretColor;

    public CustomCaret(Color caretColor) {
        this.caretColor = caretColor;
    }

    @Override
    protected synchronized void damage(Rectangle r) {
        if (r == null) {
            return;
        }

        JTextComponent comp = getComponent();
        comp.repaint(r.x, r.y, r.width, r.height);
    }

    @Override
    public void install(JTextComponent c) {
        super.install(c);
        c.setCaretColor(caretColor);
    }
}
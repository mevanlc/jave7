package de.jdemo.extensions;

import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import de.jdemo.util.GuiUtilities;

/**
 * @author Markus Gebhard
 */
public class SwingTextPanel {

  private JComponent content;

  public SwingTextPanel(CharSequence text) {
    this(text, GuiUtilities.DEFAULT_FIXED_WIDTH_FONT);
  }

  public SwingTextPanel(CharSequence text, Font font) {
    JTextPane textPane = new JTextPane();
    textPane.setEditable(false);
    textPane.setText(text.toString());
    textPane.setFont(font);
    content = new JScrollPane(textPane);
  }

  public JComponent getContent() {
    return content;
  }

}
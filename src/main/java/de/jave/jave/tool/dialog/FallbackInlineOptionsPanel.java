package de.jave.jave.tool.dialog;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Default inline options content shown when the active tool has no
 * inline panel of its own. PHASE0 ships this as the only registered
 * panel so the host region renders end-to-end before any tool migrates.
 */
public class FallbackInlineOptionsPanel implements IInlineToolOptions {
   private final JComponent content;

   public FallbackInlineOptionsPanel() {
      JPanel panel = new JPanel(new BorderLayout());
      JLabel label = new JLabel("No inline options for this tool — use Tool Options Dialog.");
      label.setHorizontalAlignment(SwingConstants.CENTER);
      panel.add(label, BorderLayout.CENTER);
      this.content = panel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }
}

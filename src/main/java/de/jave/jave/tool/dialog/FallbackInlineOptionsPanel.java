package de.jave.jave.tool.dialog;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Default inline options content shown when the active tool has no
 * inline panel of its own. Renders as blank space.
 */
public class FallbackInlineOptionsPanel implements IInlineToolOptions {
   private final JComponent content = new JPanel();

   @Override
   public JComponent getContent() {
      return this.content;
   }
}

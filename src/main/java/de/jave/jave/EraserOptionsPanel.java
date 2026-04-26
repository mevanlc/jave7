package de.jave.jave;

import de.jave.jave.tool.dialog.IInlineToolOptions;
import javax.swing.JComponent;

public class EraserOptionsPanel implements IInlineToolOptions {
   private final JComponent content;

   public EraserOptionsPanel(JComponent content) {
      this.content = content;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }
}

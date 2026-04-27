package de.jave.jave;

import de.jave.jave.tool.dialog.IInlineToolOptions;
import javax.swing.JComponent;

public class WatermarkOptionsPanel implements IInlineToolOptions {
   private final JComponent content;

   public WatermarkOptionsPanel(JComponent content) {
      this.content = content;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }
}

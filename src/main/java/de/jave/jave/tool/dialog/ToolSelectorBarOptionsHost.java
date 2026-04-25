package de.jave.jave.tool.dialog;

import de.jave.jave.Tool;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Bottom region of the tool selector bar that hosts the active tool's
 * inline options panel — or a fallback panel when the tool has none.
 *
 * <p>Wired from {@code JavEApplication.setTool(int)} alongside the
 * existing legacy {@code ToolOptionsDialog.setTool} call.
 */
public class ToolSelectorBarOptionsHost {
   private final JPanel panel;
   private final IInlineToolOptions fallback;
   private IInlineToolOptions current;

   public ToolSelectorBarOptionsHost(IInlineToolOptions fallback) {
      this.fallback = fallback;
      this.panel = new JPanel(new BorderLayout());
   }

   public JComponent getContent() {
      return this.panel;
   }

   public void setTool(Tool tool) {
      IInlineToolOptions next = tool.getInlineOptionsPanel();
      if (next == null) {
         next = this.fallback;
      }
      if (next == this.current) {
         return;
      }
      this.panel.removeAll();
      this.panel.add(next.getContent(), BorderLayout.CENTER);
      this.panel.revalidate();
      this.panel.repaint();
      this.current = next;
   }
}

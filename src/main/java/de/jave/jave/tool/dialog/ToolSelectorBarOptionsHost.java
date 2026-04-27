package de.jave.jave.tool.dialog;

import de.jave.jave.Tool;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Bottom region of the tool selector bar that hosts the active tool's
 * inline options panel — or a fallback panel when the tool has none.
 *
 * <p>Wired from {@code JavEApplication.setTool(int)}.
 */
public class ToolSelectorBarOptionsHost {
   private final JPanel panel;
   private final IInlineToolOptions fallback;
   private IInlineToolOptions current;
   private int minWidth;

   public ToolSelectorBarOptionsHost(IInlineToolOptions fallback) {
      this.fallback = fallback;
      this.panel = new JPanel(new BorderLayout()) {
         @Override
         public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(Math.max(d.width, ToolSelectorBarOptionsHost.this.minWidth), d.height);
         }
      };
   }

   public void setMinWidth(int width) {
      this.minWidth = width;
      this.panel.revalidate();
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

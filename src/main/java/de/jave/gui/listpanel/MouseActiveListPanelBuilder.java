package de.jave.gui.listpanel;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IBlock;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class MouseActiveListPanelBuilder<T> {
   private final IClickHandler<T> clickHandler;
   private final JPanel panel;

   public MouseActiveListPanelBuilder(IClickHandler<T> clickHandler) {
      Ensure.ensureArgumentNotNull(clickHandler);
      this.clickHandler = clickHandler;
      this.panel = new JPanel(new GridDialogLayout(1, false));
   }

   public void add(JComponent component, final T value) {
      MouseActivePanel mouseActivePanel = new MouseActivePanel(component, new IBlock() {
         @Override
         public void execute() {
            MouseActiveListPanelBuilder.this.clickHandler.handleClick(value);
         }
      });
      this.panel.add(mouseActivePanel, GridDialogLayoutData.FILL_HORIZONTAL);
   }

   public JComponent createComponent() {
      JScrollPane scrollPane = new JScrollPane(this.panel, 20, 31);
      scrollPane.setBorder(null);
      scrollPane.setPreferredSize(new Dimension(440, 240));
      scrollPane.getVerticalScrollBar().setUnitIncrement(12);
      scrollPane.getVerticalScrollBar().setBlockIncrement(12);
      return scrollPane;
   }
}

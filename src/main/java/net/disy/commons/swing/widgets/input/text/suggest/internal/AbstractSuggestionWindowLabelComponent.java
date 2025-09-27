package net.disy.commons.swing.dialog.input.text.suggest.internal;

import java.awt.Cursor;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.color.SwingColors;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.layout.grid.GridDialogLayout;

public abstract class AbstractSuggestionWindowLabelComponent implements IComponentContainer {
   private final JComponent content;

   public AbstractSuggestionWindowLabelComponent(String labelText, Icon optionalIcon) {
      Ensure.ensureArgumentNotNull(labelText);
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.setBackground(SwingColors.getTextAreaBackgroundColor());
      JLabel label = new JLabel(labelText);
      label.setIcon(optionalIcon);
      panel.add(label);
      this.content = new JScrollPane(panel);
      this.content.setCursor(this.getCursor());
   }

   protected abstract Cursor getCursor();

   @Override
   public final JComponent getContent() {
      return this.content;
   }
}

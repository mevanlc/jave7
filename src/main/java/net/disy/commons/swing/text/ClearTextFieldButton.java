package net.disy.commons.swing.text;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JTextField;
import net.disy.commons.core.util.StringUtilities;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.color.SwingColors;
import net.disy.commons.swing.events.AbstractDocumentChangeListener;
import net.disy.commons.swing.icon.CommonIcons;

public final class ClearTextFieldButton extends JButton {
   private final JTextField textField;

   public ClearTextFieldButton(final JTextField textField, String toolTipText) {
      super(new SmartAction(CommonIcons.DELETE) {
         @Override
         protected void execute(Component parentComponent) {
            textField.setText("");
            textField.requestFocus();
         }
      });
      this.setPreferredSize(new Dimension(this.getIcon().getIconWidth() + 2, this.getIcon().getIconHeight() + 2));
      this.setBorderPainted(false);
      this.setBackground(SwingColors.getTextAreaBackgroundColor());
      this.setFocusPainted(false);
      this.setFocusable(false);
      this.setToolTipText(toolTipText);
      this.textField = textField;
      textField.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            ClearTextFieldButton.this.updateEnabled();
         }
      });
      this.updateEnabled();
   }

   private void updateEnabled() {
      this.setEnabled(!StringUtilities.isNullOrEmpty(this.textField.getText()));
   }
}

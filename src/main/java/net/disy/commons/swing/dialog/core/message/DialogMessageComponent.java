package net.disy.commons.swing.dialog.core.message;

import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.dialog.core.IDialogConstants;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.message.AbstractMessageTypeUi;
import net.disy.commons.swing.message.MessageTypeUi;
import net.disy.commons.swing.widgets.AutoWrappingLabel;

public class DialogMessageComponent extends JPanel {
   private final AutoWrappingLabel messageLabel;
   private final JLabel iconLabel;

   public DialogMessageComponent(boolean withIcon) {
      super(new BorderLayout());
      this.setBackground(IDialogConstants.HEADER_BACKGROUND_COLOR);
      this.iconLabel = new JLabel();
      this.messageLabel = new AutoWrappingLabel("!Dialog.message!", 330);
      this.messageLabel.setBackground(IDialogConstants.HEADER_BACKGROUND_COLOR);
      this.messageLabel.setForeground(IDialogConstants.HEADER_TEXT_COLOR);
      this.messageLabel.setFont(IDialogConstants.MESSAGE_LABEL_FONT);
      JPanel iconPanel = new JPanel(new GridDialogLayout(1, false, 3, 0));
      iconPanel.add(this.iconLabel);
      iconPanel.setBackground(IDialogConstants.HEADER_BACKGROUND_COLOR);
      if (withIcon) {
         this.add(iconPanel, "West");
      } else {
         this.add(Box.createHorizontalStrut(10), "West");
      }

      this.add(this.messageLabel.getContent(), "Center");
   }

   public void setMessage(IBasicMessage message) {
      if (message != null) {
         this.iconLabel.setIcon(MessageTypeUi.getInstance().getIcon(message.getType()));
         this.messageLabel.setForeground(AbstractMessageTypeUi.getColor(message.getType()));
         this.messageLabel.setText(message.getText());
      }
   }
}

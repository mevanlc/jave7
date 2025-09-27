package net.disy.commons.swing.dialog.message;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.IMessage;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.message.LargeIconMessageTypeUi;
import net.disy.commons.swing.message.MessageTypeUi;
import net.disy.commons.swing.widgets.AutoWrappingLabel;

public class MessageDialogPage extends AbstractDialogPage {
   private final IMessage message;

   public MessageDialogPage(IMessage message) {
      super("");
      Ensure.ensureArgumentNotNull(message);
      this.message = message;
   }

   @Override
   public JComponent createContent() {
      GridDialogLayoutData iconData = new GridDialogLayoutData();
      iconData.setGrabExcessVerticalSpace(true);
      iconData.setVerticalAlignment(GridAlignment.FILL);
      GridDialogLayoutData textData = new GridDialogLayoutData();
      textData.setGrabExcessHorizontalSpace(true);
      textData.setHorizontalAlignment(GridAlignment.FILL);
      Icon icon = new LargeIconMessageTypeUi().getIcon(this.message.getType());
      AutoWrappingLabel label = new AutoWrappingLabel(this.message.getText(), 294);
      JPanel panel = new JPanel(new GridDialogLayout(2, false, 13, 0));
      label.setBackground(panel.getBackground());
      panel.add(new JLabel(icon), iconData);
      panel.add(label.getContent(), textData);
      return panel;
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      return this.getDefaultMessage();
   }

   @Override
   public String getDescription() {
      return null;
   }

   @Override
   public String getTitle() {
      return this.message.getTitle() == null ? MessageTypeUi.getInstance().getLabel(this.message.getType()) : this.message.getTitle();
   }
}

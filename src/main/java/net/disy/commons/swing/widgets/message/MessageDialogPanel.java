package net.disy.commons.swing.dialog.message;

import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.IMessage;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.dialog.input.IRequestFinishListener;
import net.disy.commons.swing.dialog.input.ISmartDialogPanel;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;

public class MessageDialogPanel implements ISmartDialogPanel {
   private final IMessage message;

   public MessageDialogPanel(IMessage message) {
      this.message = message;
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
   }

   @Override
   public void addRequestFinishListener(IRequestFinishListener requestFinishListener) {
   }

   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      return this.message;
   }

   @Override
   public void requestFocus() {
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      JComponent content = new MessageDialogPage(this.message).createContent();
      panel.add(content, GridDialogLayoutDataFactory.createHorizontalSpanData(columnCount));
   }

   @Override
   public int getColumnCount() {
      return 1;
   }
}

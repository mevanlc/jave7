package net.disy.commons.swing.dialog.input.text.suggest.internal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import net.disy.commons.core.message.IMessage;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.message.MessageTypeUi;
import net.disy.commons.swing.util.GuiUtilities;

public class OptionalMessageButton implements IComponentContainer {
   private final JButton button = new JButton();

   public OptionalMessageButton(final ObjectModel<IMessage> model) {
      this.updateButton(model);
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            OptionalMessageButton.this.updateButton(model);
         }
      });
      this.button.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            MessageDialogFactory.showMessageDialog(GuiUtilities.getWindowFor(e), model.getValue());
         }
      });
   }

   private void updateButton(ObjectModel<IMessage> model) {
      IMessage message = model.getValue();
      if (message == null) {
         this.button.setVisible(false);
      } else {
         this.button.setIcon(MessageTypeUi.getInstance().getIcon(message.getType()));
         this.button.setText(message.getTitle());
         this.button.setVisible(true);
      }
   }

   @Override
   public JComponent getContent() {
      return this.button;
   }
}

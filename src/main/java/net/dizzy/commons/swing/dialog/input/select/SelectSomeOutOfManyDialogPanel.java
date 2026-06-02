package net.dizzy.commons.swing.dialog.input.select;

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.dizzy.commons.core.message.BasicMessage;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.model.FixedOptionsObjectSelectionModel;
import net.dizzy.commons.swing.component.IComponentContainer;

public class SelectSomeOutOfManyDialogPanel<T> extends AbstractChangeableModel implements IComponentContainer {
   private final JPanel panel = new JPanel();
   private final FixedOptionsObjectSelectionModel<T> model;
   private final ISomeOutOfManyDialogPanelConfiguration<T> configuration;

   public SelectSomeOutOfManyDialogPanel(ISomeOutOfManyDialogPanelConfiguration<T> configuration) {
      this(null, configuration);
   }

   public SelectSomeOutOfManyDialogPanel(FixedOptionsObjectSelectionModel<T> model, ISomeOutOfManyDialogPanelConfiguration<T> configuration) {
      this.model = model;
      this.configuration = configuration;
   }

   @Override public JComponent getContent() { return panel; }

   public IBasicMessage createOptionalCurrentMessage() {
      return model == null || model.getFirstSelectedValue() != null ? null : new BasicMessage(configuration.getNoItemSelectedErrorMessageText(), MessageType.ERROR);
   }
}

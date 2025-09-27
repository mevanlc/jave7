package net.disy.commons.swing.dialog.input.object;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.ObjectUtilities;
import net.disy.commons.swing.dialog.input.AbstractLabeledSmartDialogPanel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;
import net.disy.commons.swing.dialog.input.object.component.IObjectAttributeInputComponentFactory;
import net.disy.commons.swing.dialog.input.object.component.IObjectInputComponent;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public abstract class AbstractObjectSmartDialogPanel<O> extends AbstractLabeledSmartDialogPanel {
   private final ObjectModel<O> stringModel;
   private final IObjectInputComponent<O, ?> inputComponent;
   private final IMessageProducingValidator validator;

   public AbstractObjectSmartDialogPanel(
      String label, final ObjectModel<O> stringModel, IObjectAttributeInputComponentFactory<O, ?> componentFactory, IMessageProducingValidator validator
   ) {
      super(label, validator);
      Ensure.ensureArgumentNotNull(stringModel);
      Ensure.ensureArgumentNotNull(validator);
      this.stringModel = stringModel;
      this.validator = validator;
      this.inputComponent = componentFactory.createComponent();
      this.inputComponent.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            stringModel.setValue(AbstractObjectSmartDialogPanel.this.inputComponent.getValue());
         }
      });
      stringModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AbstractObjectSmartDialogPanel.this.updateInputComponent();
         }
      });
      this.updateInputComponent();
   }

   private void updateInputComponent() {
      if (!ObjectUtilities.equals(this.inputComponent.getValue(), this.stringModel.getValue())) {
         this.inputComponent.setValue(this.stringModel.getValue());
      }
   }

   @Override
   protected int getMainComponentColumnCount() {
      return 1;
   }

   @Override
   protected JComponent fillMainComponentInto(JPanel panel, int columnCount) {
      GridDialogLayoutData layoutData = new GridDialogLayoutData();
      layoutData.setGrabExcessHorizontalSpace(true);
      layoutData.setHorizontalAlignment(GridAlignment.FILL);
      layoutData.setHorizontalSpan(columnCount);
      panel.add(this.inputComponent.getComponent(), layoutData);
      return this.inputComponent.getComponent();
   }

   @Override
   public final IBasicMessage createOptionalCurrentMessage() {
      return this.validator.createOptionalCurrentMessage();
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
      this.stringModel.addChangeListener(listener);
   }

   public void selectAll() {
      this.inputComponent.selectAll();
   }

   @Override
   public void requestFocus() {
      this.inputComponent.requestFocus();
   }

   public void setEditable(boolean editable) {
      this.inputComponent.setEditable(editable);
   }

   public boolean isEditable() {
      return this.inputComponent.isEditable();
   }

   @Override
   protected void setMainComponentEnabled(boolean enabled) {
      this.inputComponent.setEnabled(enabled);
   }

   @Override
   protected JComponent[] getOtherComponents() {
      return new JComponent[]{this.inputComponent.getComponent()};
   }

   @Override
   public void update() {
      this.inputComponent.update();
   }
}

package net.disy.commons.swing.dialog.input.select;

import javax.swing.JPanel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.input.AbstractSmartDialogPanel;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;
import net.disy.commons.swing.ui.AbstractObjectUi;

public class BooleanRadioButtonComponent extends AbstractSmartDialogPanel {
   private final RadioButtonPanel<Boolean> booleanRadioButtonPanel;
   private final BooleanModel model;

   public BooleanRadioButtonComponent(BooleanModel model, final String trueValueLabel, final String falseValueLabel) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.booleanRadioButtonPanel = new RadioButtonPanel<>(
         new Boolean[]{Boolean.TRUE, Boolean.FALSE}, new BooleanRadioButtonComponent.BooleanModelDecoratorModel(model), new AbstractObjectUi<Boolean>() {
            public String getLabel(Boolean value) {
               return value ? trueValueLabel : falseValueLabel;
            }
         }
      );
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
      this.model.addChangeListener(listener);
   }

   @Override
   public void requestFocus() {
      this.booleanRadioButtonPanel.getContent().requestFocus();
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      panel.add(this.booleanRadioButtonPanel.getContent(), GridDialogLayoutDataFactory.createHorizontalSpanData(columnCount, GridDialogLayoutData.FILL_BOTH));
   }

   @Override
   public int getColumnCount() {
      return 1;
   }

   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      return null;
   }

   public void setEnabled(boolean isEnabled) {
      this.booleanRadioButtonPanel.setEnabled(isEnabled);
   }

   private static final class BooleanModelDecoratorModel extends ObjectModel<Boolean> {
      private final BooleanModel model;

      private BooleanModelDecoratorModel(BooleanModel model) {
         this.model = model;
      }

      public Boolean getValue() {
         return this.model.getValue();
      }

      public void setValue(Boolean value) {
         this.model.setValue(value);
      }

      @Override
      public void addChangeListener(IChangeListener listener) {
         this.model.addChangeListener(listener);
      }

      @Override
      public void removeChangeListener(IChangeListener listener) {
         this.model.removeChangeListener(listener);
      }
   }
}

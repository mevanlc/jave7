package net.disy.commons.swing.dialog.input.combo;

import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;
import net.disy.commons.swing.dialog.input.ISelectableItemsPanelConfiguration;
import net.disy.commons.swing.ui.AbstractObjectUi;
import net.disy.commons.swing.ui.DefaultObjectUi;
import net.disy.commons.swing.ui.IObjectUi;

public class BooleanModelSmartDialogPanel extends ComboSelectionDialogPanel<Boolean> {
   private static final ISelectableItemsPanelConfiguration<Boolean> CONFIGURATION = new ISelectableItemsPanelConfiguration<Boolean>() {
      public Boolean[] getItems() {
         return new Boolean[]{null, Boolean.TRUE, Boolean.FALSE};
      }

      @Override
      public IObjectUi<Boolean> getObjectUi() {
         return new AbstractObjectUi<Boolean>() {
            public String getLabel(Boolean value) {
               return value == null ? " " : new DefaultObjectUi<Boolean>().getLabel(value);
            }
         };
      }
   };

   public BooleanModelSmartDialogPanel(String label, ObjectModel<Boolean> model, IMessageProducingValidator validator) {
      super(label, model, CONFIGURATION, validator);
   }
}

package net.disy.commons.swing.dialog.input.check;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.ActionWidgetFactory;
import net.disy.commons.swing.action.SmartToggleAction;
import net.disy.commons.swing.dialog.input.AbstractSmartDialogPanel;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;

public class CheckBoxSmartDialogPanel extends AbstractSmartDialogPanel {
   private final JCheckBox checkBox;
   private final BooleanModel model;

   public CheckBoxSmartDialogPanel(BooleanModel model, String label) {
      this.model = model;
      Ensure.ensureArgumentNotNull(model);
      this.checkBox = ActionWidgetFactory.createCheckBox(new SmartToggleAction(model, label));
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
      this.model.addChangeListener(listener);
   }

   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      return null;
   }

   @Override
   public void requestFocus() {
      this.checkBox.requestFocus();
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      GridDialogLayoutData layoutData = GridDialogLayoutDataFactory.createHorizontalSpanData(columnCount);
      panel.add(this.checkBox, layoutData);
   }

   @Override
   public int getColumnCount() {
      return 1;
   }

   public void setEnabled(boolean value) {
      this.checkBox.setEnabled(value);
   }
}

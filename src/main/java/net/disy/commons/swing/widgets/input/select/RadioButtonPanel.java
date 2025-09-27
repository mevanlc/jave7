package net.disy.commons.swing.dialog.input.select;

import java.awt.Component;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.ui.IObjectUi;

public class RadioButtonPanel<T> {
   private final JComponent content;
   private final JRadioButton[] radioButtons;

   public RadioButtonPanel(final T[] values, final ObjectModel<T> model, IObjectUi<T> objectUi) {
      Ensure.ensureArgumentNotNull(values);
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(objectUi);
      ButtonGroup group = new ButtonGroup();
      this.radioButtons = new JRadioButton[values.length];

      for (int i = 0; i < this.radioButtons.length; i++) {
         final T currentValue = values[i];
         String label = objectUi.getLabel(currentValue);
         this.radioButtons[i] = new JRadioButton(new SmartAction(label) {
            @Override
            protected void execute(Component parentComponent) {
               model.setValue(currentValue);
            }
         });
         this.radioButtons[i].setSelected(currentValue == model.getValue());
         group.add(this.radioButtons[i]);
      }

      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            for (int i = 0; i < values.length; i++) {
               if (values[i].equals(model.getValue())) {
                  RadioButtonPanel.this.radioButtons[i].setSelected(true);
                  return;
               }
            }
         }
      });
      JPanel panel = new JPanel(new GridDialogLayout(1, false, 0, 0));

      for (int i = 0; i < this.radioButtons.length; i++) {
         panel.add(this.radioButtons[i]);
      }

      this.content = panel;
      panel.setEnabled(false);
   }

   public JComponent getContent() {
      return this.content;
   }

   public void setEnabled(boolean isEnabled) {
      for (JRadioButton radioButton : this.radioButtons) {
         radioButton.setEnabled(isEnabled);
      }
   }
}

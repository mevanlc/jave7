package net.dizzy.commons.swing.dialog.input.select;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.dizzy.commons.core.model.FixedOptionsObjectSelectionModel;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.swing.component.IComponentContainer;

public class RadioButtonPanel<T> implements IComponentContainer {
   private final JPanel panel = new JPanel();

   public RadioButtonPanel(FixedOptionsObjectSelectionModel<T> model, net.dizzy.commons.swing.ui.IObjectUi<T> ui) {
      ButtonGroup group = new ButtonGroup();
      for (T value : model.getAllValues()) {
         JRadioButton button = new JRadioButton(ui.getLabel(value));
         button.addActionListener(event -> model.setSelectedValue(value));
         group.add(button);
         panel.add(button);
      }
   }

   public RadioButtonPanel(T[] values, FixedOptionsObjectSelectionModel<T> model, net.dizzy.commons.swing.ui.IObjectUi<T> ui) {
      this(model, ui);
   }

   public RadioButtonPanel(T[] values, ObjectModel<T> model, net.dizzy.commons.swing.ui.IObjectUi<T> ui) {
      ButtonGroup group = new ButtonGroup();
      for (T value : values) {
         JRadioButton button = new JRadioButton(ui.getLabel(value));
         button.setSelected(java.util.Objects.equals(model.getValue(), value));
         button.addActionListener(event -> model.setValue(value));
         group.add(button);
         panel.add(button);
      }
   }

   @Override public JComponent getContent() { return panel; }
}

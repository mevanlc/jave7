package net.disy.commons.swing.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import net.disy.commons.core.model.ObjectModel;

public class ButtonGroupLinker<T> {
   private final ObjectModel<T> model;
   private final ButtonGroup buttonGroup = new ButtonGroup();
   private final Map<ButtonModel, T> valueByButtonModel = new HashMap<>();
   private final ActionListener listener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
         ButtonGroupLinker.this.model.setValue(ButtonGroupLinker.this.valueByButtonModel.get(ButtonGroupLinker.this.buttonGroup.getSelection()));
      }
   };

   public ButtonGroupLinker(ObjectModel<T> model) {
      this.model = model;
   }

   public AbstractButton addButton(AbstractButton button, T value) {
      if (this.valueByButtonModel.containsKey(button.getModel())) {
         throw new IllegalArgumentException("A button with equal model has already been linked. Tryed to link '" + value + "' to " + button);
      } else {
         this.buttonGroup.add(button);
         this.valueByButtonModel.put(button.getModel(), value);
         button.addActionListener(this.listener);
         button.setSelected(value.equals(this.model.getValue()));
         return button;
      }
   }
}

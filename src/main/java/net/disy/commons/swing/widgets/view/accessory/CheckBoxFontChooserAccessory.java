package net.disy.commons.swing.fontchooser.view.accessory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class CheckBoxFontChooserAccessory implements IFontChooserAccessory {
   private final JCheckBox checkBox;

   public CheckBoxFontChooserAccessory(final BooleanModel model, String label) {
      this.checkBox = new JCheckBox(label, model.getValue());
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            CheckBoxFontChooserAccessory.this.checkBox.setSelected(model.getValue());
         }
      });
      this.checkBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            model.setValue(CheckBoxFontChooserAccessory.this.checkBox.isSelected());
         }
      });
   }

   @Override
   public JComponent getContent() {
      return this.checkBox;
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.checkBox.setEnabled(enabled);
   }

   @Override
   public void setModel(FontModel model) {
   }
}

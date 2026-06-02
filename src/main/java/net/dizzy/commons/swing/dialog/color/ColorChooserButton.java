package net.dizzy.commons.swing.dialog.color;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;

import net.dizzy.commons.swing.color.widgets.ColorModel;
import net.dizzy.commons.swing.component.IComponentContainer;

public class ColorChooserButton implements IComponentContainer {
   private final JButton button = new JButton();

   public ColorChooserButton(ColorModel model) {
      button.setBackground(model.getValue());
      button.addActionListener(event -> {
         Color color = JColorChooser.showDialog(button, "Choose color", model.getValue());
         if (color != null) {
            model.setValue(color);
            button.setBackground(color);
         }
      });
   }

   @Override public JComponent getContent() { return button; }
}

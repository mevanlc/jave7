package de.jave.figlet.swing.application;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class DimensionTextField {
   private final JTextField sizeBar = new JTextField(12);

   public DimensionTextField() {
      this.sizeBar.setEditable(false);
   }

   public void setDimension(Dimension dimension) {
      if (dimension.height != 0 && dimension.width != 0) {
         this.sizeBar.setText(dimension.width + " x " + dimension.height);
      } else {
         this.sizeBar.setText("");
      }
   }

   public JComponent getContent() {
      return this.sizeBar;
   }
}

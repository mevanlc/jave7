package net.disy.commons.swing.dialog.color;

import java.awt.Color;
import net.disy.commons.swing.color.widgets.ColorModel;

public class ColorIndicator extends ColorChooserLabel {
   public ColorIndicator(Color color) {
      this(new ColorModel(color));
   }

   public ColorIndicator(ColorModel colorModel) {
      super(colorModel, new DefaultColorChooserConfiguration(true));
      this.setEditable(false);
   }
}

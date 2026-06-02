package net.dizzy.commons.swing.color.widgets;

import java.awt.Color;

import net.dizzy.commons.core.model.ObjectModel;

public class ColorModel extends ObjectModel<Color> {
   public ColorModel(Color color) {
      super(color);
   }

   public Color getColor() {
      return getValue();
   }

   public void setColor(Color color) {
      setValue(color);
   }
}

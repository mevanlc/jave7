package net.disy.commons.swing.color.widgets;

import java.awt.Color;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.Ensure;

public class ColorModel extends AbstractChangeableModel {
   private Color color;
   private final boolean supressEventsOnEquality;

   public ColorModel() {
      this(Color.BLACK);
   }

   public ColorModel(Color color) {
      this(color, true);
   }

   public ColorModel(Color color, boolean supressEventsOnEquality) {
      this.supressEventsOnEquality = supressEventsOnEquality;
      this.setColor(color);
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color color) {
      Ensure.ensureArgumentNotNull(color);
      if (!this.supressEventsOnEquality || !color.equals(this.color)) {
         this.color = color;
         this.fireChangeEvent();
      }
   }
}

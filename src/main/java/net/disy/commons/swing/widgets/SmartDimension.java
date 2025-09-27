package net.disy.commons.swing.geometry;

import java.awt.Dimension;

public class SmartDimension extends Dimension {
   public SmartDimension() {
   }

   public SmartDimension(int width, int height) {
      super(width, height);
   }

   public SmartDimension(Dimension d) {
      super(d);
   }

   public SmartDimension resize(int widthDelta, int heightDelta) {
      return new SmartDimension(this.width + widthDelta, this.height + heightDelta);
   }
}

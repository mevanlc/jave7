package de.jave.image2ascii.model;

import net.dizzy.commons.core.model.AbstractChangeableModel;

public class Image2AsciiOutputOptionsModel extends AbstractChangeableModel {
   private static final int RESULT_WIDTH_DEFAULT = 72;
   private static final int SHAPE_DEFAULT = 100;
   private int outputWidth = 72;
   private double shapeFactor = 1.0;

   public void setOutputWidth(int outputWidth) {
      if (this.outputWidth != outputWidth) {
         this.outputWidth = outputWidth;
         this.fireChangeEvent();
      }
   }

   public int getOutputWidth() {
      return this.outputWidth;
   }

   public void setShapeFactor(double shapeFactor) {
      if (this.shapeFactor != shapeFactor) {
         this.shapeFactor = shapeFactor;
         this.fireChangeEvent();
      }
   }

   public double getShapeFactor() {
      return this.shapeFactor;
   }

   public void reset() {
      this.setShapeFactor(1.0);
   }
}

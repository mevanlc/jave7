package de.jave.jave.tool.fractal;

import net.disy.commons.core.model.AbstractChangeableModel;

public class FractalCutoutModel extends AbstractChangeableModel {
   private static final double DEFAULT_X_SPAN = 2.0;
   private static final double DEFAULT_X = -0.3;
   private static final double DEFAULT_Y = 0.0;
   private double x = -0.3;
   private double y = 0.0;
   private double xSpan = 2.0;

   public void setX(double x) {
      if (this.x != x) {
         this.x = x;
         this.fireChangeEvent();
      }
   }

   public double getX() {
      return this.x;
   }

   public void setY(double y) {
      if (this.y != y) {
         this.y = y;
         this.fireChangeEvent();
      }
   }

   public double getY() {
      return this.y;
   }

   public void setXSpan(double xSpan) {
      if (this.xSpan != xSpan) {
         this.xSpan = xSpan;
         this.fireChangeEvent();
      }
   }

   public double getXSpan() {
      return this.xSpan;
   }

   public void reset() {
      this.x = -0.3;
      this.y = 0.0;
      this.xSpan = 2.0;
      this.fireChangeEvent();
   }
}

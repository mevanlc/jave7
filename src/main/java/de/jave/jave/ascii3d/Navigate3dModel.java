package de.jave.jave.ascii3d;

import net.disy.commons.core.model.AbstractChangeableModel;

public class Navigate3dModel extends AbstractChangeableModel {
   private static final double DEFAULT_ZOOM = 7.0;
   private static final int DEFAULT_ALPHA = 0;
   private int alpha = 0;
   private double zoom = 7.0;

   public int getAlpha() {
      return this.alpha;
   }

   public double getZoom() {
      return this.zoom;
   }

   public void reset() {
      this.setValues(0, 7.0);
   }

   private void setValues(int alpha, double zoom) {
      if (this.alpha != alpha || this.zoom != zoom) {
         this.alpha = alpha;
         this.zoom = zoom;
         this.fireChangeEvent();
      }
   }

   public void setAlpha(int alpha) {
      if (this.alpha != alpha) {
         this.alpha = alpha;
         this.fireChangeEvent();
      }
   }

   public void setZoom(double zoom) {
      if (this.zoom != zoom) {
         this.zoom = zoom;
         this.fireChangeEvent();
      }
   }

   public void doRotateRight() {
      this.setAlpha(this.alpha + 5);
   }

   public void doZoomIn() {
      this.setZoom(this.zoom * 1.2);
   }

   public void doZoomOut() {
      this.setZoom(this.zoom / 1.2);
   }

   public void doRotateLeft() {
      this.setAlpha(this.alpha - 5);
   }
}

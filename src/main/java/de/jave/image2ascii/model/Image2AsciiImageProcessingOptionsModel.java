package de.jave.image2ascii.model;

import de.jave.image.Rotation;
import de.jave.image.greyscale.algorithm.dithering.DitheringAlgorithms;
import de.jave.image.greyscale.algorithm.dithering.IGreyscaleDithering;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.util.ObjectUtilities;

public class Image2AsciiImageProcessingOptionsModel extends AbstractChangeableModel {
   private static final int SHARPEN_DEFAULT = 0;
   private static final int GAMMA_DEFAULT = 1;
   private int highlightValue = 100;
   private int shadowValue = 0;
   private double gammaValue = 1.0;
   private double sharpenValue = 0.0;
   private boolean invert = false;
   private boolean normalized = true;
   private Rotation rotation = Rotation.NONE;
   private IGreyscaleDithering dithering = DitheringAlgorithms.getAllGreyscaleDitheringAlgorithms()[0];

   public boolean isNormalize() {
      return this.normalized;
   }

   public void setNormalized(boolean normalized) {
      if (this.normalized != normalized) {
         this.normalized = normalized;
         this.fireChangeEvent();
      }
   }

   public boolean isInvert() {
      return this.invert;
   }

   public void setInvert(boolean invert) {
      if (this.invert != invert) {
         this.invert = invert;
         this.fireChangeEvent();
      }
   }

   public int getHighlightValue() {
      return this.highlightValue;
   }

   public void setHighlightValue(int highlightValue) {
      if (this.highlightValue != highlightValue) {
         this.highlightValue = highlightValue;
         this.fireChangeEvent();
      }
   }

   public int getShadowValue() {
      return this.shadowValue;
   }

   public void setShadowValue(int shadowValue) {
      if (this.shadowValue != shadowValue) {
         this.shadowValue = shadowValue;
         this.fireChangeEvent();
      }
   }

   public double getGammaValue() {
      return this.gammaValue;
   }

   public void setGammaValue(double gammaValue) {
      if (this.gammaValue != gammaValue) {
         this.gammaValue = gammaValue;
         this.fireChangeEvent();
      }
   }

   public double getSharpenValue() {
      return this.sharpenValue;
   }

   public void setSharpenValue(double sharpenValue) {
      if (this.sharpenValue != sharpenValue) {
         this.sharpenValue = sharpenValue;
         this.fireChangeEvent();
      }
   }

   public Rotation getRotation() {
      return this.rotation;
   }

   public void setRotation(Rotation rotation) {
      if (this.rotation != rotation) {
         this.rotation = rotation;
         this.fireChangeEvent();
      }
   }

   public IGreyscaleDithering getDithering() {
      return this.dithering;
   }

   public void setDithering(IGreyscaleDithering dithering) {
      if (!ObjectUtilities.equals(this.dithering, dithering)) {
         this.dithering = dithering;
         this.fireChangeEvent();
      }
   }

   public void reset() {
      this.setGammaValue(1.0);
      this.setHighlightValue(100);
      this.setShadowValue(0);
      this.setSharpenValue(0.0);
      this.setNormalized(true);
      this.setInvert(false);
   }
}

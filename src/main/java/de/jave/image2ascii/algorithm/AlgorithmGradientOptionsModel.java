package de.jave.image2ascii.algorithm;

import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.Ensure;

public class AlgorithmGradientOptionsModel extends AbstractChangeableModel {
   private String gradient;
   private boolean steganogram = false;
   private String steganogramText = "";

   public AlgorithmGradientOptionsModel(AsciiGradientConfiguration gradientConfiguration) {
      this.gradient = gradientConfiguration.getDefaultGradient();
   }

   public void setSteganogramText(String steganogramText) {
      Ensure.ensureArgumentNotNull(steganogramText);
      if (!this.steganogramText.equals(steganogramText)) {
         this.steganogramText = steganogramText;
         this.fireChangeEvent();
      }
   }

   public String getSteganogramText() {
      return this.steganogramText;
   }

   public void setGradient(String gradient) {
      Ensure.ensureArgumentNotNull(gradient);
      if (!this.gradient.equals(gradient)) {
         this.gradient = gradient;
         this.fireChangeEvent();
      }
   }

   public String getGradient() {
      return this.gradient;
   }

   public void setSteganogram(boolean steganogram) {
      if (this.steganogram != steganogram) {
         this.steganogram = steganogram;
         this.fireChangeEvent();
      }
   }

   public boolean isSteganogram() {
      return this.steganogram;
   }
}

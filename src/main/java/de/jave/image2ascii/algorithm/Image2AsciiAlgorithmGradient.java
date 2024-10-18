package de.jave.image2ascii.algorithm;

import de.jave.image.IValueRaster;
import de.jave.image2ascii.AbstractImage2AsciiAlgorithm;
import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.Image2AsciiIcons;
import de.jave.image2ascii.algorithm.dialog.Image2AsciiAlgorithmGradientOptionsPanel;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import javax.swing.Icon;
import net.disy.commons.core.model.IChangeableModel;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.progress.ProgressUtilities;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IDisposableComponentContainer;

public class Image2AsciiAlgorithmGradient extends AbstractImage2AsciiAlgorithm {
   private final AlgorithmGradientOptionsModel optionsModel;
   private final AsciiGradientConfiguration gradientConfiguration;

   public Image2AsciiAlgorithmGradient(AsciiGradientConfiguration gradientConfiguration) {
      Ensure.ensureArgumentNotNull(gradientConfiguration);
      this.gradientConfiguration = gradientConfiguration;
      this.optionsModel = new AlgorithmGradientOptionsModel(gradientConfiguration);
   }

   @Override
   public IChangeableModel getOptionsModel() {
      return this.optionsModel;
   }

   @Override
   public void setGreyscaleTable(AsciiGreyscaleTable greyscaleTable) {
   }

   @Override
   public void setSpecialChars(String specialCharacters) {
      this.optionsModel.setGradient(specialCharacters);
   }

   @Override
   public String getName() {
      return "Gradient";
   }

   @Override
   public Icon getIcon() {
      return Image2AsciiIcons.ALGORITHM_GRADIENT_ICON;
   }

   @Override
   public Dimension getImageSizeForSettings(Dimension originalImageSize, int textWidth, double shapeFactor) {
      return super.getScaledImageSize(originalImageSize, textWidth, shapeFactor, 1, 1);
   }

   @Override
   public boolean isMonochromeImageRequired() {
      return false;
   }

   @Override
   public IDisposableComponentContainer createAdjustmentComponent() {
      return new Image2AsciiAlgorithmGradientOptionsPanel(this.optionsModel, this.gradientConfiguration);
   }

   @Override
   public CharacterPlate convert(IValueRaster raster, IProgressMonitor progressMonitor, ICancelable cancelable) throws InterruptedException {
      String gradient = this.optionsModel.getGradient();
      if (gradient.length() == 0) {
         gradient = " ";
      }

      int resolution = gradient.length();
      int width = raster.getWidth();
      int height = raster.getHeight();
      progressMonitor.beginTask("Converting...", height);
      CharacterPlate cp = new CharacterPlate(width, height);
      boolean stegano = this.optionsModel.isSteganogram();
      String steganoText = this.optionsModel.getSteganogramText();

      for (int y = 0; y < height; y++) {
         Thread.yield();
         ProgressUtilities.checkInterrupted(cancelable);

         for (int x = 0; x < width; x++) {
            double d = (double)raster.getValueAt(x, y) / 255.0;
            int i = (int)((1.0 - d) * (double)resolution);
            if (i >= resolution) {
               i = resolution - 1;
            }

            if (stegano) {
               int characterIndex = (y * width + x) / 8;
               if (characterIndex < steganoText.length()) {
                  int bitIndex = (y * width + x) % 8;
                  boolean currentBit = (steganoText.charAt(characterIndex) & 1 << bitIndex) > 0;
                  boolean iBit = (i & 1) > 0;
                  if (iBit != currentBit) {
                     if ((x + y) % 2 == 0) {
                        i--;
                     } else {
                        i++;
                     }
                  }

                  if (i < 0) {
                     i += 2;
                  } else if (i >= resolution) {
                     i -= 2;
                  }
               }
            }

            cp.setForce(x, y, gradient.charAt(i));
         }

         progressMonitor.worked(1);
      }

      return cp;
   }
}

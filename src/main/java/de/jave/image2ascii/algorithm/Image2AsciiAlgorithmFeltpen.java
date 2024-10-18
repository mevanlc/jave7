package de.jave.image2ascii.algorithm;

import de.jave.image.IValueRaster;
import de.jave.image2ascii.AbstractImage2AsciiAlgorithm;
import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.algorithm.dialog.Image2AsciiAlgotihmFeltpenOptionsPanel;
import de.jave.jave.filter.Filter;
import de.jave.jave.pixelplate.PixelPlate;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.lib.CharacterPlate;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Vector;
import javax.swing.Icon;
import net.disy.commons.core.model.IChangeableModel;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IDisposableComponentContainer;

public class Image2AsciiAlgorithmFeltpen extends AbstractImage2AsciiAlgorithm {
   private final AlgorithmFeltpenOptionsModel optionsModel = new AlgorithmFeltpenOptionsModel();
   private final Filter filter;

   public Image2AsciiAlgorithmFeltpen(Filter filter) {
      Ensure.ensureArgumentNotNull(filter);
      this.filter = filter;
   }

   @Override
   public String getName() {
      return "Felt Pen";
   }

   @Override
   public Icon getIcon() {
      return null;
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
   }

   @Override
   public IDisposableComponentContainer createAdjustmentComponent() {
      return new Image2AsciiAlgotihmFeltpenOptionsPanel(this.optionsModel);
   }

   @Override
   public Dimension getImageSizeForSettings(Dimension originalImageSize, int textWidth, double shapeFactor) {
      return super.getScaledImageSize(originalImageSize, textWidth, shapeFactor, 3, 4);
   }

   @Override
   public boolean isMonochromeImageRequired() {
      return false;
   }

   @Override
   public CharacterPlate convert(IValueRaster raster, IProgressMonitor progressMonitor, ICancelable cancelable) throws InterruptedException {
      int width = raster.getWidth();
      int height = raster.getHeight();
      PixelPlateMode[] feltpenGradientModes = PixelPlate.FELTPEN_GRADIENT_MODES;
      int gradientCount = feltpenGradientModes.length;
      int[] colors = new int[256];
      boolean doMedianCut = this.optionsModel.isMedianCut();
      if (doMedianCut) {
         int[] histogramm = new int[256];

         for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
               histogramm[raster.getValueAt(x, y)]++;
            }
         }

         Vector runs = new Vector(gradientCount + 2);
         HistogrammRun h = new HistogrammRun(histogramm, 0, 255);
         runs.addElement(h);
         runs.addElement(h.split());
         HistogrammRun currentRun = null;

         while (runs.size() < gradientCount + 1) {
            int maxSize = 0;
            int maxIndex = 0;

            for (int i = 0; i < runs.size(); i++) {
               currentRun = (HistogrammRun)runs.elementAt(i);
               if (currentRun.isSplitable() && currentRun.getSize() > maxSize) {
                  maxSize = currentRun.getSize();
                  maxIndex = i;
               }
            }

            currentRun = (HistogrammRun)runs.elementAt(maxIndex);
            runs.insertElementAt(currentRun.split(), maxIndex + 1);
         }

         for (int ix = 0; ix < runs.size(); ix++) {
            currentRun = (HistogrammRun)runs.elementAt(ix);

            for (int j = currentRun.getStart(); j <= currentRun.getEnd(); j++) {
               colors[j] = ix;
            }
         }
      } else {
         for (int ix = 0; ix < 256; ix++) {
            colors[ix] = ix * (gradientCount + 1) / 255;
         }
      }

      CharacterPlate cp = new CharacterPlate(width / 3, height / 4);
      cp.setMix(true);
      progressMonitor.beginTask("Converting...", gradientCount * height);

      for (int ix = 0; ix < gradientCount; ix++) {
         PixelPlate plate = new PixelPlate(new Rectangle(width / 3, height / 4), this.filter);
         plate.setMode(feltpenGradientModes[gradientCount - ix - 1]);

         for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
               if (ix == colors[raster.getValueAt(x, y)]) {
                  plate.set(x, y);
               }
            }
         }

         LocatedCharacterPlate result = plate.convert();
         result.pasteInto(cp);
         progressMonitor.worked(1);
      }

      return cp;
   }
}

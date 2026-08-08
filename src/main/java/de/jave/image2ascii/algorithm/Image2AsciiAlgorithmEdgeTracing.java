package de.jave.image2ascii.algorithm;

import de.jave.image.IValueRaster;
import de.jave.image2ascii.AbstractImage2AsciiAlgorithm;
import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.algorithm.dialog.Image2AsciiAlgorithmEdgeTracingOptionsPanel;
import de.jave.jave.filter.Filter;
import de.jave.jave.filter.FilterMode;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import javax.swing.Icon;
import net.dizzy.commons.core.model.IChangeableModel;
import net.dizzy.commons.core.progress.ICancelable;
import net.dizzy.commons.core.progress.IProgressMonitor;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.component.IDisposableComponentContainer;

public class Image2AsciiAlgorithmEdgeTracing extends AbstractImage2AsciiAlgorithm {
   private final AlgorithmEdgeTracingOptionsModel optionsModel = new AlgorithmEdgeTracingOptionsModel();
   private final Filter filter;

   public Image2AsciiAlgorithmEdgeTracing(Filter filter) {
      Ensure.ensureArgumentNotNull(filter);
      this.filter = filter;
   }

   @Override
   public Icon getIcon() {
      return null;
   }

   @Override
   public String getName() {
      return "Edge Tracing";
   }

   @Override
   public IDisposableComponentContainer createAdjustmentComponent() {
      return new Image2AsciiAlgorithmEdgeTracingOptionsPanel(this.optionsModel);
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
   public Dimension getImageSizeForSettings(Dimension originalImageSize, int textWidth, double shapeFactor) {
      return super.getScaledImageSize(originalImageSize, textWidth, shapeFactor, 1, 1);
   }

   @Override
   public boolean isMonochromeImageRequired() {
      return true;
   }

   @Override
   public CharacterPlate convert(IValueRaster raster, IProgressMonitor progressMonitor, ICancelable cancelable) throws InterruptedException {
      int width = raster.getWidth();
      int height = raster.getHeight() + 1;
      char fillChar = ':';
      CharacterPlate result = new CharacterPlate(width, height);
      progressMonitor.beginTask("Converting...", height);

      for (int y = -1; y < height - 1; y++) {
         for (int x = 0; x < width; x++) {
            if (isSet(raster, x, y)) {
               if (!isSet(raster, x - 1, y) && !isSet(raster, x + 1, y) && !isSet(raster, x, y - 1) && !isSet(raster, x, y + 1)) {
                  result.set(x, y + 1, 'O');
               } else if (!isSet(raster, x - 1, y) && !isSet(raster, x, y - 1)) {
                  result.set(x, y + 1, '/');
               } else if (!isSet(raster, x + 1, y) && !isSet(raster, x, y - 1)) {
                  result.set(x, y + 1, '\\');
               } else if (!isSet(raster, x - 1, y) && !isSet(raster, x, y + 1)) {
                  result.set(x, y + 1, '\\');
               } else if (!isSet(raster, x + 1, y) && !isSet(raster, x, y + 1)) {
                  result.set(x, y + 1, '/');
               } else if (!isSet(raster, x, y + 1)) {
                  result.set(x, y + 1, '_');
               } else if (!isSet(raster, x - 1, y) || !isSet(raster, x + 1, y)) {
                  result.set(x, y + 1, '|');
               }
            } else if (isSet(raster, x, y + 1) && isSet(raster, x - 1, y + 1) && isSet(raster, x + 1, y + 1)) {
               result.set(x, y + 1, '_');
            }
         }

         progressMonitor.worked(1);
      }

      if (this.optionsModel.isSmoothing()) {
         this.filter.filter(result, FilterMode.IMAGE2ASCII_SIMPLE_EDGE);
      }

      if (this.optionsModel.isFill()) {
         for (int y = -1; y < height - 1; y++) {
            for (int xx = 0; xx < width; xx++) {
               if (result.glyphAt(xx, y + 1) == ' ' && isSet(raster, xx, y)) {
                  result.set(xx, y + 1, ':');
               }
            }
         }
      }

      return result;
   }

   private static boolean isSet(IValueRaster pixels, int x, int y) {
      return y >= 0 && x >= 0 && y < pixels.getHeight() && x < pixels.getWidth() ? pixels.getValueAt(x, y) == 0 : false;
   }
}

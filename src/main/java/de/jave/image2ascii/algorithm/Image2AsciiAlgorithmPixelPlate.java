package de.jave.image2ascii.algorithm;

import de.jave.image.IValueRaster;
import de.jave.image2ascii.AbstractImage2AsciiAlgorithm;
import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.algorithm.dialog.Image2AsciiAlgorithmPixelPlateOptionsPanel;
import de.jave.jave.filter.Filter;
import de.jave.jave.pixelplate.PixelPlate;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.Icon;
import net.dizzy.commons.core.model.IChangeableModel;
import net.dizzy.commons.core.progress.ICancelable;
import net.dizzy.commons.core.progress.IProgressMonitor;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.component.IDisposableComponentContainer;

public class Image2AsciiAlgorithmPixelPlate extends AbstractImage2AsciiAlgorithm {
   private final AlgorithmPixelPlateOptionsModel optionsModel = new AlgorithmPixelPlateOptionsModel();
   private final Filter filter;

   public Image2AsciiAlgorithmPixelPlate(Filter filter) {
      Ensure.ensureArgumentNotNull(filter);
      this.filter = filter;
   }

   @Override
   public IChangeableModel getOptionsModel() {
      return this.optionsModel;
   }

   @Override
   public Icon getIcon() {
      return null;
   }

   @Override
   public String getName() {
      return "B/W";
   }

   @Override
   public void setGreyscaleTable(AsciiGreyscaleTable greyscaleTable) {
   }

   @Override
   public void setSpecialChars(String specialCharacters) {
   }

   @Override
   public IDisposableComponentContainer createAdjustmentComponent() {
      return new Image2AsciiAlgorithmPixelPlateOptionsPanel(this.optionsModel);
   }

   @Override
   public Dimension getImageSizeForSettings(Dimension originalImageSize, int textWidth, double shapeFactor) {
      return super.getScaledImageSize(originalImageSize, textWidth, shapeFactor, this.getHorizontalPixelsPerChar(), this.getVerticalPixelsPerChar());
   }

   private int getVerticalPixelsPerChar() {
      return this.optionsModel.getPixelPlateMode().getRasterY();
   }

   private int getHorizontalPixelsPerChar() {
      return this.optionsModel.getPixelPlateMode().getRasterX();
   }

   @Override
   public boolean isMonochromeImageRequired() {
      return true;
   }

   @Override
   public CharacterPlate convert(IValueRaster raster, IProgressMonitor progressMonitor, ICancelable cancelable) throws InterruptedException {
      int width = raster.getWidth();
      int height = raster.getHeight();
      progressMonitor.beginTask("Converting...", height);
      PixelPlate plate = new PixelPlate(new Rectangle(width / this.getHorizontalPixelsPerChar(), height / this.getVerticalPixelsPerChar()), this.filter);
      plate.setMode(this.optionsModel.getPixelPlateMode());

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            if (raster.getValueAt(x, y) == 0) {
               plate.set(x, y);
            }
         }

         progressMonitor.worked(1);
      }

      CharacterPlate result = plate.convert();
      result.replace('\u0000', ' ');
      return result;
   }
}

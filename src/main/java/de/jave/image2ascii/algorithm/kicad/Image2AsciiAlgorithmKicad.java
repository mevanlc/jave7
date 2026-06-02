package de.jave.image2ascii.algorithm.kicad;

import de.jave.image.IValueRaster;
import de.jave.image2ascii.AbstractImage2AsciiAlgorithm;
import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.Image2AsciiIcons;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.util.Calendar;
import java.util.Date;
import javax.swing.Icon;
import net.dizzy.commons.core.model.IChangeableModel;
import net.dizzy.commons.core.progress.ICancelable;
import net.dizzy.commons.core.progress.IProgressMonitor;
import net.dizzy.commons.core.progress.ProgressUtilities;
import net.dizzy.commons.swing.component.IDisposableComponentContainer;

public class Image2AsciiAlgorithmKicad extends AbstractImage2AsciiAlgorithm {
   private static final int PIXEL_COUNT_LIMIT = 16384;
   private final KiCadOptionsModel optionsModel = new KiCadOptionsModel();

   @Override
   public IChangeableModel getOptionsModel() {
      return this.optionsModel;
   }

   @Override
   public String getName() {
      return "KiCad electronic module";
   }

   @Override
   public Icon getIcon() {
      return Image2AsciiIcons.ALGORITHM_KICAD;
   }

   @Override
   public Dimension getImageSizeForSettings(Dimension originalImageSize, int textWidth, double shapeFactor) {
      if (originalImageSize.width * originalImageSize.height > 16384) {
         double scaleFactor = Math.sqrt(16384.0 / (double)originalImageSize.width / (double)originalImageSize.height);
         int newWidth = (int)Math.max(1.0, scaleFactor * (double)originalImageSize.width);
         int newHeight = (int)Math.max(1.0, scaleFactor * (double)originalImageSize.height);
         return new Dimension(newWidth, newHeight);
      } else {
         return originalImageSize;
      }
   }

   @Override
   public boolean isMonochromeImageRequired() {
      return true;
   }

   @Override
   public IDisposableComponentContainer createAdjustmentComponent() {
      return new KicadOptionsPanel(this.optionsModel);
   }

   @Override
   public void setGreyscaleTable(AsciiGreyscaleTable greyscaleTable) {
   }

   @Override
   public void setSpecialChars(String specialCharacters) {
   }

   @Override
   public CharacterPlate convert(IValueRaster pixels, IProgressMonitor progressMonitor, ICancelable cancelable) throws InterruptedException {
      int width = pixels.getWidth();
      int height = pixels.getHeight();
      progressMonitor.beginTask("Converting...", height);
      String moduleName = this.optionsModel.getModuleName();
      if (moduleName == null || moduleName.length() == 0) {
         moduleName = "Logo";
      }

      Date now = Calendar.getInstance().getTime();
      KiCadModuleFileContentBuilder builder = new KiCadModuleFileContentBuilder(now);
      builder.addModuleIndex(moduleName);
      double widthInInch = Math.max(0.1, this.optionsModel.getWidthInInch());
      int padSize = (int)Math.round(widthInInch / (double)width * 10000.0);
      builder.startModule(moduleName);

      for (int y = 0; y < height; y++) {
         Thread.yield();
         ProgressUtilities.checkInterrupted(cancelable);
         KiCadLineRun run = null;

         for (int x = 0; x < width; x++) {
            Thread.yield();
            ProgressUtilities.checkInterrupted(cancelable);
            int value = pixels.getValueAt(x, y);
            if (value == 1) {
               if (run == null) {
                  run = new KiCadLineRun(x, y);
               } else {
                  run.addSegment();
               }
            } else if (run != null) {
               this.addPadRun(builder, run, padSize, width, height);
               run = null;
            }
         }

         if (run != null) {
            this.addPadRun(builder, run, padSize, width, height);
         }

         progressMonitor.worked(1);
      }

      builder.endModule(moduleName);
      builder.endLibrary();
      return builder.createCharacterPlate();
   }

   private void addPadRun(KiCadModuleFileContentBuilder builder, KiCadLineRun run, int padSize, int width, int height) {
      int padWidth = padSize * run.getWidth();
      int xPosition = -width / 2 * padSize + run.getX() * padSize + padWidth / 2;
      int yPosition = -height / 2 * padSize + run.getY() * padSize + padSize / 2;
      builder.addMechanicalPad(xPosition, yPosition, padWidth, padSize);
   }
}

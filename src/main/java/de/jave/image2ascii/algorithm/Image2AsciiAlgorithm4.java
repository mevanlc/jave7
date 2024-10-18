package de.jave.image2ascii.algorithm;

import de.jave.image.IValueRaster;
import de.jave.image2ascii.AbstractImage2AsciiAlgorithm;
import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.image2ascii.Image2AsciiIcons;
import de.jave.image2ascii.SharedImage2AsciiOptions;
import de.jave.image2ascii.algorithm.dialog.Image2AsciiAlgorithm4OptionsPanel;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import javax.swing.Icon;
import net.disy.commons.core.model.IChangeableModel;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.progress.ProgressUtilities;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IDisposableComponentContainer;

public class Image2AsciiAlgorithm4 extends AbstractImage2AsciiAlgorithm {
   private final Algorithm4OptionsModel optionsModel;
   private final AsciiGreyscaleTableConfiguration greyscaleTableConfiguration;

   public Image2AsciiAlgorithm4(SharedImage2AsciiOptions sharedOptions, AsciiGreyscaleTableConfiguration greyscaleTableConfiguration) {
      Ensure.ensureArgumentNotNull(sharedOptions);
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      this.optionsModel = new Algorithm4OptionsModel(sharedOptions);
      this.greyscaleTableConfiguration = greyscaleTableConfiguration;
   }

   @Override
   public IChangeableModel getOptionsModel() {
      return this.optionsModel;
   }

   @Override
   public IDisposableComponentContainer createAdjustmentComponent() {
      return new Image2AsciiAlgorithm4OptionsPanel(this.optionsModel, this.greyscaleTableConfiguration);
   }

   @Override
   public String getName() {
      return "4 Pixels per Character";
   }

   @Override
   public Icon getIcon() {
      return Image2AsciiIcons.ALGORITHM_4PIXEL_ICON;
   }

   @Override
   public void setGreyscaleTable(AsciiGreyscaleTable greyscaleTable) {
      this.optionsModel.getGreyScaleTableSelectionModel().setAutomaticFromFont(false);
      this.optionsModel.getGreyScaleTableSelectionModel().setGreyScaleTable(greyscaleTable);
   }

   @Override
   public void setSpecialChars(String specialCharacters) {
      this.optionsModel.getBannedCharactersModel().setBannedCharacters(specialCharacters);
   }

   @Override
   public Dimension getImageSizeForSettings(Dimension originalImageSize, int textWidth, double shapeFactor) {
      return super.getScaledImageSize(originalImageSize, textWidth, shapeFactor, this.getHorizontalPixelsPerChar(), this.getVerticalPixelsPerChar());
   }

   private int getVerticalPixelsPerChar() {
      return this.isOptimized() ? 8 : 2;
   }

   private int getHorizontalPixelsPerChar() {
      return 2;
   }

   @Override
   public boolean isMonochromeImageRequired() {
      return false;
   }

   protected boolean isOptimized() {
      return this.optionsModel.isOptimize();
   }

   private String getBannedCharacters() {
      return this.optionsModel.getBannedCharactersModel().getBannedCharacters();
   }

   @Override
   public CharacterPlate convert(IValueRaster raster, IProgressMonitor progressMonitor, ICancelable cancelable) throws InterruptedException {
      AsciiGreyscaleTable table = this.optionsModel.getGreyScaleTableSelectionModel().getActualGreyscaleTable(this.optionsModel.getDisplayFontModel());
      int width = raster.getWidth();
      int height = raster.getHeight();
      CharacterPlate result = null;
      String bannedCharacters = this.getBannedCharacters();
      table.setDoNotUse4(bannedCharacters);
      if (this.isOptimized()) {
         width /= 2;
         height /= 8;
         progressMonitor.beginTask("Converting...", height);
         result = new CharacterPlate(width, height);

         for (int y = 0; y < height; y++) {
            Thread.yield();
            ProgressUtilities.checkInterrupted(cancelable);
            int y0 = y * 8;

            for (int x = 0; x < width; x++) {
               int x0 = x * 2;
               int NW = (
                     0 * raster.getValueAt(x0, y0) + 1 * raster.getValueAt(x0, y0 + 1) + 1 * raster.getValueAt(x0, y0 + 2) + 1 * raster.getValueAt(x0, y0 + 3)
                  )
                  / 3;
               int SW = (
                     1 * raster.getValueAt(x0, y0 + 4)
                        + 1 * raster.getValueAt(x0, y0 + 5)
                        + 1 * raster.getValueAt(x0, y0 + 6)
                        + 0 * raster.getValueAt(x0, y0 + 7)
                  )
                  / 3;
               int NE = (
                     0 * raster.getValueAt(x0 + 1, y0)
                        + 1 * raster.getValueAt(x0 + 1, y0 + 1)
                        + 1 * raster.getValueAt(x0 + 1, y0 + 2)
                        + 1 * raster.getValueAt(x0 + 1, y0 + 3)
                  )
                  / 3;
               int SE = (
                     1 * raster.getValueAt(x0 + 1, y0 + 4)
                        + 1 * raster.getValueAt(x0 + 1, y0 + 5)
                        + 1 * raster.getValueAt(x0 + 1, y0 + 6)
                        + 0 * raster.getValueAt(x0 + 1, y0 + 7)
                  )
                  / 3;
               result.setForce(x, y, table.getCharForBrightness(NW, NE, SW, SE));
            }

            progressMonitor.worked(1);
         }
      } else {
         width /= 2;
         height /= 2;
         progressMonitor.beginTask("Converting...", height);
         result = new CharacterPlate(width, height);

         for (int y = 0; y < height; y++) {
            Thread.yield();
            ProgressUtilities.checkInterrupted(cancelable);

            for (int x = 0; x < width; x++) {
               result.setForce(
                  x,
                  y,
                  table.getCharForBrightness(
                     raster.getValueAt(x * 2, y * 2),
                     raster.getValueAt(x * 2 + 1, y * 2),
                     raster.getValueAt(x * 2, y * 2 + 1),
                     raster.getValueAt(x * 2 + 1, y * 2 + 1)
                  )
               );
            }

            progressMonitor.worked(1);
         }
      }

      return result;
   }
}

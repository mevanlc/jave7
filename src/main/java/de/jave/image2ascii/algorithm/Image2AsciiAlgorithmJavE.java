package de.jave.image2ascii.algorithm;

import de.jave.image.IValueRaster;
import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image2ascii.AbstractImage2AsciiAlgorithm;
import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.image2ascii.Image2AsciiIcons;
import de.jave.image2ascii.SharedImage2AsciiOptions;
import de.jave.image2ascii.algorithm.dialog.Image2AsciiAlgorithmJavEOptionsPanel;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import javax.swing.Icon;
import net.dizzy.commons.core.model.IChangeableModel;
import net.dizzy.commons.core.progress.ICancelable;
import net.dizzy.commons.core.progress.IProgressMonitor;
import net.dizzy.commons.core.progress.NullProgressMonitor;
import net.dizzy.commons.core.progress.ProgressUtilities;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.component.IDisposableComponentContainer;

public class Image2AsciiAlgorithmJavE extends AbstractImage2AsciiAlgorithm {
   public static final String NAME = "JavE Algorithm (4/1)";
   private final AsciiGreyscaleTableConfiguration greyscaleTableConfiguration;
   private final AlgorithmJavEOptionsModel optionsModel;

   public Image2AsciiAlgorithmJavE(SharedImage2AsciiOptions sharedOptions, AsciiGreyscaleTableConfiguration greyscaleTableConfiguration) {
      Ensure.ensureArgumentNotNull(sharedOptions);
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      this.greyscaleTableConfiguration = greyscaleTableConfiguration;
      this.optionsModel = new AlgorithmJavEOptionsModel(sharedOptions);
   }

   @Override
   public IChangeableModel getOptionsModel() {
      return this.optionsModel;
   }

   @Override
   public IDisposableComponentContainer createAdjustmentComponent() {
      return new Image2AsciiAlgorithmJavEOptionsPanel(this.optionsModel, this.greyscaleTableConfiguration);
   }

   @Override
   public String getName() {
      return "JavE Algorithm (4/1)";
   }

   @Override
   public Icon getIcon() {
      return Image2AsciiIcons.ALGORITHM_JAVE_ICON;
   }

   @Override
   public Dimension getImageSizeForSettings(Dimension originalImageSize, int textWidth, double shapeFactor) {
      return super.getScaledImageSize(originalImageSize, textWidth, shapeFactor, 2, 8);
   }

   @Override
   public boolean isMonochromeImageRequired() {
      return false;
   }

   @Override
   public void setSpecialChars(String specialCharacters) {
      this.optionsModel.getBannedCharactersModel().setBannedCharacters(specialCharacters);
   }

   @Override
   public void setGreyscaleTable(AsciiGreyscaleTable greyscaleTable) {
      this.optionsModel.getGreyScaleTableSelectionModel().setAutomaticFromFont(false);
      this.optionsModel.getGreyScaleTableSelectionModel().setGreyScaleTable(greyscaleTable);
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
      String notUse = this.getBannedCharacters();
      table.setDoNotUse4(notUse);
      width /= 2;
      height /= 8;
      progressMonitor.beginTask("Converting...", height);
      result = new CharacterPlate(width, height);
      GGreyscaleImage g1 = new GGreyscaleImage(width, height);

      for (int y = 0; y < height; y++) {
         Thread.yield();
         ProgressUtilities.checkInterrupted(cancelable);
         int y0 = y * 8;

         for (int x = 0; x < width; x++) {
            int x0 = x * 2;
            int NW = (0 * raster.getValueAt(x0, y0) + 1 * raster.getValueAt(x0, y0 + 1) + 1 * raster.getValueAt(x0, y0 + 2) + 1 * raster.getValueAt(x0, y0 + 3))
               / 3;
            int SW = (
                  1 * raster.getValueAt(x0, y0 + 4) + 1 * raster.getValueAt(x0, y0 + 5) + 1 * raster.getValueAt(x0, y0 + 6) + 0 * raster.getValueAt(x0, y0 + 7)
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
            int avg = (NW + NE + SW + SE) / 4;
            g1.set(x, y, avg);
            int d = 5;
            if (NW <= 5 + avg && NW >= avg - 5 && NE <= 5 + avg && NE >= avg - 5 && SW <= 5 + avg && SW >= avg - 5 && SE <= 5 + avg && SE >= avg - 5) {
               result.setForce(x, y, '\u0000');
            } else {
               result.setForce(x, y, table.getCharForBrightness(NW, NE, SW, SE));
            }
         }

         progressMonitor.worked(1);
      }

      CharacterPlate p1 = Image2AsciiAlgorithm1.convert(g1, table, true, notUse, new NullProgressMonitor(), cancelable);
      if (p1 == null) {
         return null;
      } else {
         for (int y = 0; y < height; y++) {
            Thread.yield();
            ProgressUtilities.checkInterrupted(cancelable);

            for (int xx = 0; xx < width; xx++) {
               if (result.glyphAt(xx, y) == 0) {
                  result.setForce(xx, y, p1.glyphAt(xx, y));
               }
            }
         }

         return result;
      }
   }
}

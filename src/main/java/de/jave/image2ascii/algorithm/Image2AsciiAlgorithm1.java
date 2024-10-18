package de.jave.image2ascii.algorithm;

import de.jave.image.IValueRaster;
import de.jave.image2ascii.AbstractImage2AsciiAlgorithm;
import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.image2ascii.Image2AsciiIcons;
import de.jave.image2ascii.SharedImage2AsciiOptions;
import de.jave.image2ascii.algorithm.dialog.Image2AsciiAlgorithm1OptionsPanel;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import javax.swing.Icon;
import net.disy.commons.core.model.IChangeableModel;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.progress.ProgressUtilities;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IDisposableComponentContainer;

public class Image2AsciiAlgorithm1 extends AbstractImage2AsciiAlgorithm {
   private final Algorithm1OptionsModel optionsModel;
   private final AsciiGreyscaleTableConfiguration greyscaleOptionsPanel;

   public Image2AsciiAlgorithm1(SharedImage2AsciiOptions sharedOptions, AsciiGreyscaleTableConfiguration greyscaleOptionsPanel) {
      Ensure.ensureArgumentNotNull(sharedOptions);
      Ensure.ensureArgumentNotNull(greyscaleOptionsPanel);
      this.greyscaleOptionsPanel = greyscaleOptionsPanel;
      this.optionsModel = new Algorithm1OptionsModel(sharedOptions);
   }

   @Override
   public IChangeableModel getOptionsModel() {
      return this.optionsModel;
   }

   @Override
   public String getName() {
      return "1 Pixel per Character";
   }

   @Override
   public Icon getIcon() {
      return Image2AsciiIcons.ALGORITHM_1PIXEL_ICON;
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
      return new Image2AsciiAlgorithm1OptionsPanel(this.optionsModel, this.greyscaleOptionsPanel);
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
   public CharacterPlate convert(IValueRaster pixels, IProgressMonitor progressMonitor, ICancelable cancelable) throws InterruptedException {
      String bannedCharacters = this.getBannedCharacters();
      boolean dithering = this.optionsModel.isDithering();
      AsciiGreyscaleTable table = this.optionsModel.getGreyScaleTableSelectionModel().getActualGreyscaleTable(this.optionsModel.getDisplayFontModel());
      return convert(pixels, table, dithering, bannedCharacters, progressMonitor, cancelable);
   }

   public static final CharacterPlate convert(
      IValueRaster pixels, AsciiGreyscaleTable table, boolean dithering, String doNotUse, IProgressMonitor progressMonitor, ICancelable cancelable
   ) throws InterruptedException {
      table.setDoNotUse(doNotUse);
      int width = pixels.getWidth();
      int height = pixels.getHeight();
      progressMonitor.beginTask("Converting...", height);
      CharacterPlate cp = new CharacterPlate(width, height);
      if (dithering) {
         int[][] lastLineErrors = new int[width + 4][3];

         for (int y = 0; y < height; y++) {
            Thread.yield();
            ProgressUtilities.checkInterrupted(cancelable);
            int y0 = y % 3;
            int y1 = (y + 1) % 3;
            int y2 = (y + 2) % 3;

            for (int x = 0; x < width; x++) {
               double oldError = 0.0;
               oldError += 0.023809523809523808 * (double)lastLineErrors[x][y0];
               oldError += 0.047619047619047616 * (double)lastLineErrors[x + 1][y0];
               oldError += 0.09523809523809523 * (double)lastLineErrors[x + 2][y0];
               oldError += 0.047619047619047616 * (double)lastLineErrors[x + 3][y0];
               oldError += 0.023809523809523808 * (double)lastLineErrors[x + 4][y0];
               oldError += 0.047619047619047616 * (double)lastLineErrors[x][y1];
               oldError += 0.09523809523809523 * (double)lastLineErrors[x + 1][y1];
               oldError += 0.19047619047619047 * (double)lastLineErrors[x + 2][y1];
               oldError += 0.09523809523809523 * (double)lastLineErrors[x + 3][y1];
               oldError += 0.047619047619047616 * (double)lastLineErrors[x + 4][y1];
               oldError += 0.09523809523809523 * (double)lastLineErrors[x][y2];
               oldError += 0.19047619047619047 * (double)lastLineErrors[x + 1][y2];
               int value = (int)((double)pixels.getValueAt(x, y) - oldError);
               char ch = table.getCharForBrightness(value);
               cp.setForce(x, y, ch);
               lastLineErrors[x + 2][y2] = table.getBrightnessForChar(ch) - value;
            }

            progressMonitor.worked(1);
         }
      } else {
         for (int y = 0; y < height; y++) {
            Thread.yield();
            ProgressUtilities.checkInterrupted(cancelable);

            for (int x = 0; x < width; x++) {
               int value = pixels.getValueAt(x, y);
               cp.setForce(x, y, table.getCharForBrightness(value));
            }

            progressMonitor.worked(1);
         }
      }

      return cp;
   }
}

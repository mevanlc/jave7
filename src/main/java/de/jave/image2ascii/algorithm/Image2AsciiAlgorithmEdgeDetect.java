package de.jave.image2ascii.algorithm;

import de.jave.image.IValueRaster;
import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.monochrome.GMonochromeImage;
import de.jave.image.monochrome.algorithm.EdgeReduction;
import de.jave.image2ascii.AbstractImage2AsciiAlgorithm;
import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.algorithm.dialog.Image2AsciiAlgorithmEdgeDetectOptionsPanel;
import de.jave.jave.filter.Filter;
import de.jave.jave.filter.FilterMode;
import de.jave.jave.pixelplate.PixelPlate;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.Icon;
import net.dizzy.commons.core.model.IChangeableModel;
import net.dizzy.commons.core.progress.ICancelable;
import net.dizzy.commons.core.progress.IProgressMonitor;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.component.IDisposableComponentContainer;

public class Image2AsciiAlgorithmEdgeDetect extends AbstractImage2AsciiAlgorithm {
   private final AlgorithmEdgeDetectOptionsModel optionsModel = new AlgorithmEdgeDetectOptionsModel();
   private final Filter filter;

   public Image2AsciiAlgorithmEdgeDetect(Filter filter) {
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
   public void setSpecialChars(String specialCharacters) {
   }

   @Override
   public void setGreyscaleTable(AsciiGreyscaleTable greyscaleTable) {
   }

   @Override
   public String getName() {
      return "Edge Detection";
   }

   @Override
   public IDisposableComponentContainer createAdjustmentComponent() {
      return new Image2AsciiAlgorithmEdgeDetectOptionsPanel(this.optionsModel);
   }

   private boolean isHires() {
      return this.optionsModel.isHires();
   }

   @Override
   public Dimension getImageSizeForSettings(Dimension originalImageSize, int textWidth, double shapeFactor) {
      return super.getScaledImageSize(originalImageSize, textWidth, shapeFactor, this.getHorizontalPixelsPerChar(), this.getVerticalPixelsPerChar());
   }

   private int getVerticalPixelsPerChar() {
      return this.isHires() ? 4 : 1;
   }

   private int getHorizontalPixelsPerChar() {
      return this.isHires() ? 3 : 1;
   }

   @Override
   public boolean isMonochromeImageRequired() {
      return false;
   }

   @Override
   public CharacterPlate convert(IValueRaster raster, IProgressMonitor progressMonitor, ICancelable cancelable) throws InterruptedException {
      Ensure.ensureArgumentInstanceOf(raster, GGreyscaleImage.class);
      progressMonitor.beginTask("Converting...", 9);
      GGreyscaleImage originalImage = (GGreyscaleImage)raster;
      GGreyscaleImage laplaceFilteredImage = originalImage.filterLaplace();
      progressMonitor.worked(1);
      laplaceFilteredImage.invert();
      progressMonitor.worked(1);
      laplaceFilteredImage.applyHysteresisThreshold(160, 210, cancelable);
      GMonochromeImage thresholdedImage = laplaceFilteredImage.getThresholdedInstance();
      progressMonitor.worked(1);
      new EdgeReduction().perform(thresholdedImage);
      progressMonitor.worked(1);
      GMonochromeImage monochromeImage = (GMonochromeImage)thresholdedImage.getClone();
      progressMonitor.worked(1);
      monochromeImage.edgeDespecle();
      progressMonitor.worked(1);
      int width = monochromeImage.getWidth();
      int height = monochromeImage.getHeight();
      return this.isHires()
         ? this.convertHires(progressMonitor, monochromeImage, width, height)
         : this.convertLowRes(progressMonitor, monochromeImage, width, height);
   }

   private CharacterPlate convertLowRes(IProgressMonitor progressMonitor, GMonochromeImage monochromeImage, int width, int height) {
      int[][] ps = monochromeImage.getPixels();
      char[][] ch = new char[height][width];
      progressMonitor.worked(1);

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            if (ps[x][y] < 128) {
               ch[y][x] = '#';
            } else {
               ch[y][x] = ' ';
            }
         }
      }

      progressMonitor.worked(1);
      this.filter.filter(new CharacterPlate(ch), FilterMode.SOFT);
      progressMonitor.worked(1);
      return new CharacterPlate(ch);
   }

   private CharacterPlate convertHires(IProgressMonitor progressMonitor, GMonochromeImage monochromeImage, int width, int height) {
      PixelPlate plate = new PixelPlate(new Rectangle(width / 3, height / 4), this.filter);
      plate.setMode(PixelPlateMode.PIXEL);
      int normalizingFactor = monochromeImage.getNormalizingFactor();
      double threshold = 128.0 / (double)normalizingFactor;

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            if ((double)monochromeImage.get(x, y) < threshold) {
               plate.set(x, y);
            }
         }
      }

      progressMonitor.worked(1);
      CharacterPlate converted = plate.convert();
      progressMonitor.worked(1);
      char[][] ch = converted.getContent();

      for (int y = 0; y < ch.length; y++) {
         for (int xx = 0; xx < ch[0].length; xx++) {
            if (ch[y][xx] == 0) {
               ch[y][xx] = ' ';
            }
         }
      }

      progressMonitor.worked(1);
      return new CharacterPlate(ch);
   }
}

package de.jave.image2ascii.algorithm;

import de.jave.image.IValueRaster;
import de.jave.image2ascii.AbstractImage2AsciiAlgorithm;
import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.Image2AsciiIcons;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.model.IChangeableModel;
import net.dizzy.commons.core.progress.ICancelable;
import net.dizzy.commons.core.progress.IProgressMonitor;
import net.dizzy.commons.swing.component.IDisposableComponentContainer;

public class Image2AsciiAlgorithm3d extends AbstractImage2AsciiAlgorithm {
   @Override
   public IChangeableModel getOptionsModel() {
      return new AbstractChangeableModel() {
      };
   }

   @Override
   public Icon getIcon() {
      return Image2AsciiIcons.ALGORITHM_3D_ICON;
   }

   @Override
   public String getName() {
      return "3d";
   }

   @Override
   public void setGreyscaleTable(AsciiGreyscaleTable greyscaleTable) {
   }

   @Override
   public void setSpecialChars(String specialCharacters) {
   }

   @Override
   public IDisposableComponentContainer createAdjustmentComponent() {
      return new IDisposableComponentContainer() {
         @Override
         public void dispose() {
         }

         @Override
         public JComponent getContent() {
            return new JPanel();
         }
      };
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
      int height = raster.getHeight();
      progressMonitor.beginTask("Converting...", height);
      CharacterPlate plate = new CharacterPlate(width + height + 1, height + 1);
      this.fillPlateWithUnderscores(plate, raster.getWidth(), raster.getHeight());

      for (int x = raster.getWidth() - 1; x >= 0; x--) {
         for (int y = 0; y < raster.getHeight(); y++) {
            if (raster.getValueAt(x, y) == 0) {
               this.setPixel(plate, x, y);
            }
         }

         progressMonitor.worked(1);
      }

      return plate;
   }

   private void setPixel(CharacterPlate plate, int x, int y) {
      plate.paste("\\//", x + y, y + 1);
      plate.paste("/\\\\", x + y, y);
   }

   private void fillPlateWithUnderscores(CharacterPlate plate, int width, int height) {
      for (int y = 0; y < height + 1; y++) {
         for (int x = 0; x < width; x++) {
            plate.set(x + y, y, '_');
         }
      }
   }
}

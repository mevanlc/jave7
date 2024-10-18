package de.jave.image2ascii.algorithm;

import de.jave.braille.BrailleDisplay;
import de.jave.braille.table.BrailleTables;
import de.jave.braille.table.IBrailleTable;
import de.jave.image.IValueRaster;
import de.jave.image2ascii.AbstractImage2AsciiAlgorithm;
import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.Image2AsciiIcons;
import de.jave.image2ascii.algorithm.dialog.Image2AsciiAlgorrithmBrailleOptionsPanel;
import de.jave.lib.CharacterPlate;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import net.disy.commons.core.model.IChangeableModel;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.progress.ProgressUtilities;
import net.disy.commons.swing.component.IDisposableComponentContainer;

public class Image2AsciiAlgorithmBraille extends AbstractImage2AsciiAlgorithm {
   private final AlgorithmBrailleOptionsModel optionsModel = new AlgorithmBrailleOptionsModel();
   private BrailleDisplay brailleDisplay;

   @Override
   public IChangeableModel getOptionsModel() {
      return this.optionsModel;
   }

   @Override
   public Icon getIcon() {
      return Image2AsciiIcons.ALGORITHM_BRAILLE;
   }

   @Override
   public void setGreyscaleTable(AsciiGreyscaleTable greyscaleTable) {
   }

   @Override
   public void setSpecialChars(String specialCharacters) {
   }

   public void setBrailleDisplay(BrailleDisplay brailleDisplay) {
      this.brailleDisplay = brailleDisplay;
   }

   @Override
   public IDisposableComponentContainer createAdjustmentComponent() {
      return new Image2AsciiAlgorrithmBrailleOptionsPanel(this.optionsModel);
   }

   @Override
   public String getName() {
      return "Braille";
   }

   @Override
   public Dimension getImageSizeForSettings(Dimension originalImageSize, int textWidth, double shapeFactor) {
      return super.getScaledImageSize(originalImageSize, textWidth, shapeFactor, 2, 4);
   }

   @Override
   public boolean isMonochromeImageRequired() {
      return true;
   }

   @Override
   public CharacterPlate convert(IValueRaster raster, IProgressMonitor progressMonitor, ICancelable cancelable) throws InterruptedException {
      IBrailleTable mode = BrailleTables.getByName(this.optionsModel.getBrailleTableName());
      int width = raster.getWidth() / 2;
      int height = raster.getHeight() / 4;
      progressMonitor.beginTask("Converting...", height);
      CharacterPlate result = new CharacterPlate(width, height);

      for (int y = 0; y < height; y++) {
         Thread.yield();

         for (int x = 0; x < width; x++) {
            Thread.yield();
            ProgressUtilities.checkInterrupted(cancelable);
            int d1 = 1 - raster.getValueAt(x * 2, y * 4);
            int d2 = 1 - raster.getValueAt(x * 2, y * 4 + 1);
            int d3 = 1 - raster.getValueAt(x * 2, y * 4 + 2);
            int d4 = 1 - raster.getValueAt(x * 2 + 1, y * 4);
            int d5 = 1 - raster.getValueAt(x * 2 + 1, y * 4 + 1);
            int d6 = 1 - raster.getValueAt(x * 2 + 1, y * 4 + 2);
            int d7 = 1 - raster.getValueAt(x * 2, y * 4 + 3);
            int d8 = 1 - raster.getValueAt(x * 2 + 1, y * 4 + 3);
            int key = d1 + 2 * d2 + 4 * d3 + 8 * d4 + 16 * d5 + 32 * d6 + 64 * d7 + 128 * d8;

            char value;
            for (value = mode.getCharacterForBraillePattern(key); isNotDisplayable(value); value = mode.getCharacterForBraillePattern(key)) {
               if (d8 == 1) {
                  d8 = 0;
                  key -= 128;
               } else if (d7 == 1) {
                  d7 = 0;
                  key -= 64;
               } else if (d6 == 1) {
                  d6 = 0;
                  key -= 32;
               } else if (d5 == 1) {
                  d5 = 0;
                  key -= 16;
               } else if (d4 == 1) {
                  d4 = 0;
                  key -= 8;
               } else if (d3 == 1) {
                  d3 = 0;
                  key -= 4;
               } else if (d2 == 1) {
                  d2 = 0;
                  key -= 2;
               } else {
                  if (d1 != 1) {
                     throw new RuntimeException("Fatal Error!");
                  }

                  d1 = 0;
                  key--;
               }
            }

            result.setForce(x, y, value);
         }

         progressMonitor.worked(1);
      }

      ProgressUtilities.checkInterrupted(cancelable);
      String text = result.toString();
      if (this.brailleDisplay == null) {
         this.brailleDisplay = new BrailleDisplay(text);
         JFrame f = new JFrame("Braille Display");
         f.getContentPane().setLayout(new BorderLayout());
         JScrollPane scrolly = new JScrollPane(this.brailleDisplay);
         f.getContentPane().add(scrolly);
         f.pack();
         f.setVisible(true);
      } else {
         this.brailleDisplay.setText(text);
      }

      this.brailleDisplay.setMode(mode);
      return result;
   }

   public static final boolean isNotDisplayable(char ch) {
      return ch < ' ' || ch == 127 || ch == 128 || ch >= 130 && ch <= 140 || ch == 142 || ch >= 145 && ch <= 156 || ch == 158 || ch == 149;
   }
}

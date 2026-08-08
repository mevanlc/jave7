package de.jave.jave.algorithm;

import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.jave.JaveSelection;
import de.jave.lib.CharacterPlate;
import de.jave.lib.area.BooleanArea;
import java.util.Random;
import net.dizzy.commons.core.util.Ensure;

public class Brightness extends JaveOptionsAlgorithm {
   private final AsciiGreyscaleTableConfiguration greyscaleTableConfiguration;
   private BrightnessOptions options;

   public Brightness(AsciiGreyscaleTableConfiguration greyscaleTableConfiguration) {
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      this.greyscaleTableConfiguration = greyscaleTableConfiguration;
   }

   @Override
   public void setOptions(JaveAlgorithmOptions options) {
      this.options = (BrightnessOptions)options;
   }

   @Override
   public JaveAlgorithmOptions getOptions() {
      if (this.options == null) {
         this.options = new BrightnessOptions(this.greyscaleTableConfiguration);
      }

      return this.options;
   }

   @Override
   public String getUndoRedoName() {
      return "brightness";
   }

   @Override
   public String getMenuItemLabel() {
      return "Brightness...";
   }

   @Override
   public String getOptionsDialogTitle() {
      return "Brightness Tool";
   }

   @Override
   public JaveSelection apply(JaveSelection sel) {
      double brighten = this.options.getFactor();
      int algorithm = this.options.getAlgorithm();
      AsciiGreyscaleTable greyscaleTable = this.options.getGreyscaleTable();
      char fillChar = this.options.getChar();
      if (brighten != 0.0 && greyscaleTable != null) {
         int width = sel.getWidth();
         int height = sel.getHeight();
         CharacterPlate sourcePlate = sel.getContent();
         BooleanArea sourceMask = sel.getMask();
         CharacterPlate result = new CharacterPlate(width, height);
         switch (algorithm) {
            case 0:
               for (int y = 0; y < height; y++) {
                  for (int xxx = 0; xxx < width; xxx++) {
                     if (sourceMask == null || sourceMask.isSet(xxx, y)) {
                        int ch = sourcePlate.glyphAt(xxx, y);
                        int brightness = greyscaleTable.getBrightnessForChar(ch);
                        brightness += (int)(brighten * 255.0);
                        result.set(xxx, y, greyscaleTable.getCharForBrightness(brightness));
                     }
                  }
               }
               break;
            case 1:
               for (int y = 0; y < height; y++) {
                  for (int xx = 0; xx < width; xx++) {
                     if (sourceMask == null || sourceMask.isSet(xx, y)) {
                        int ch = sourcePlate.glyphAt(xx, y);
                        int[] brightness = greyscaleTable.getBrightness4ForChar(ch);

                        for (int i = 0; i < 4; i++) {
                           int newBrightness = brightness[i] + (int)(brighten * 255.0);
                           if (newBrightness > 255) {
                              newBrightness = 255;
                           } else if (newBrightness < 0) {
                              newBrightness = 0;
                           }

                           brightness[i] = (char)newBrightness;
                        }

                        result.set(xx, y, greyscaleTable.getCharForBrightness(brightness));
                     }
                  }
               }
               break;
            case 2:
               Random r = new Random((long) width * height);

               for (int y = 0; y < height; y++) {
                  for (int x = 0; x < width; x++) {
                     if (sourceMask == null || sourceMask.isSet(x, y)) {
                        double random = r.nextDouble();
                        if (brighten > 0.0 && random > brighten) {
                           result.set(x, y, sourcePlate.glyphAt(x, y));
                        } else if (brighten < 0.0) {
                           if (random > -brighten) {
                              result.set(x, y, sourcePlate.glyphAt(x, y));
                           } else {
                              result.set(x, y, fillChar);
                           }
                        }
                     }
                  }
               }
               break;
            default:
               throw new IllegalArgumentException("No such algorithm id in Brightness!");
         }

         sel.setContent(result);
         sel.setMask(sourceMask);
         return sel;
      } else {
         return sel;
      }
   }
}

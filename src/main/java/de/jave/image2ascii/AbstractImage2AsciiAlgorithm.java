package de.jave.image2ascii;

import java.awt.Dimension;
import net.disy.commons.swing.component.IDisposableComponentContainer;

public abstract class AbstractImage2AsciiAlgorithm implements IImage2AsciiAlgorithm {
   public abstract void setSpecialChars(String var1);

   public abstract void setGreyscaleTable(AsciiGreyscaleTable var1);

   public abstract IDisposableComponentContainer createAdjustmentComponent();

   protected final Dimension getScaledImageSize(
      Dimension originalSize, int resultWidth, double shapeFactor, int horizontalPixelsPerCharacter, int verticalPixelsPerCharacter
   ) {
      double dimFactor = 0.52 * shapeFactor;
      int newWidth = resultWidth;
      int newHeight = (int)((double)resultWidth / (double)originalSize.width * (double)originalSize.height * dimFactor);
      if (newHeight < 1) {
         newHeight = 1;
      }

      if (resultWidth < 1) {
         newWidth = 1;
      }

      newHeight *= verticalPixelsPerCharacter;
      newWidth *= horizontalPixelsPerCharacter;
      return new Dimension(newWidth, newHeight);
   }
}

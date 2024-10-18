package de.jave.image2ascii;

import de.jave.core.collections.LruCache;
import java.awt.Font;

public class DynamicalGreyScaleTableProvider {
   private static final LruCache tablesByFont = new LruCache(10);

   public static synchronized AsciiGreyscaleTable getGreyScaleTable(Font font) {
      if (!tablesByFont.containsKey(font)) {
         tablesByFont.put(font, DynamicalGreyScaleTableCreator.createGreyscaleTable(font));
      }

      return (AsciiGreyscaleTable)tablesByFont.get(font);
   }
}

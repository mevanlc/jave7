package net.jmge.gif.encode;

import java.io.IOException;
import java.io.OutputStream;
import net.disy.commons.core.util.Ensure;
import net.jmge.gif.GifColorTable;

public class ColorTableEncoder {
   private final GifColorTable colorTable;

   public ColorTableEncoder(GifColorTable colorTable) {
      Ensure.ensureArgumentNotNull(colorTable);
      this.colorTable = colorTable;
   }

   public void encode(OutputStream os) throws IOException {
      int depth = this.colorTable.getDepth();
      int palette_size = 1 << depth;
      int[] colors = this.colorTable.getColors();

      for (int i = 0; i < palette_size; i++) {
         os.write(colors[i] >> 16 & 0xFF);
         os.write(colors[i] >> 8 & 0xFF);
         os.write(colors[i] & 0xFF);
      }
   }
}

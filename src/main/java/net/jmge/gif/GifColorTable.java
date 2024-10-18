package net.jmge.gif;

public class GifColorTable {
   private final int[] theColors = new int[256];
   private int colorDepth;
   private int transparentIndex = -1;
   private int ciCount = 0;
   private final ReverseColorMap ciLookup = new ReverseColorMap();

   public int getDepth() {
      return this.colorDepth;
   }

   public int getTransparent() {
      return this.transparentIndex;
   }

   private void setTransparent(int color_index) {
      this.transparentIndex = color_index;
   }

   public void processPixels(Gif89Frame gf) throws TooManyColorsException {
      this.filterPixels(gf);
   }

   public void closePixelProcessing() {
      this.colorDepth = this.computeColorDepth(this.ciCount);
   }

   private void filterPixels(Gif89Frame dgf) throws TooManyColorsException {
      int[] argb_pixels = dgf.getPixelSource();
      byte[] ci_pixels = dgf.getPixelSink();
      int npixels = argb_pixels.length;

      for (int i = 0; i < npixels; i++) {
         int argb = argb_pixels[i];
         if (argb >>> 24 < 128) {
            if (this.transparentIndex == -1) {
               this.transparentIndex = this.ciCount;
            } else if (argb != this.theColors[this.transparentIndex]) {
               ci_pixels[i] = (byte)this.transparentIndex;
               continue;
            }
         }

         int color_index = this.ciLookup.getPaletteIndex(argb & 16777215);
         if (color_index == -1) {
            if (this.ciCount == 256) {
               throw new TooManyColorsException("can't encode as GIF (> 256 colors)");
            }

            this.theColors[this.ciCount] = argb;
            this.ciLookup.put(argb & 16777215, this.ciCount);
            ci_pixels[i] = (byte)this.ciCount;
            this.ciCount++;
         } else {
            ci_pixels[i] = (byte)color_index;
         }
      }
   }

   private int computeColorDepth(int colorcount) {
      if (colorcount <= 2) {
         return 1;
      } else if (colorcount <= 4) {
         return 2;
      } else {
         return colorcount <= 16 ? 4 : 8;
      }
   }

   public int[] getColors() {
      return this.theColors;
   }
}

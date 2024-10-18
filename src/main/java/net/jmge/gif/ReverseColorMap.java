package net.jmge.gif;

public class ReverseColorMap {
   private static final int HCAPACITY = 2053;
   private final ColorRecord[] hTable = new ColorRecord[2053];

   public int getPaletteIndex(int rgb) {
      int itable = rgb % this.hTable.length;

      ColorRecord rec;
      while ((rec = this.hTable[itable]) != null && rec.rgb != rgb) {
         itable = ++itable % this.hTable.length;
      }

      return rec != null ? rec.ipalette : -1;
   }

   public void put(int rgb, int ipalette) {
      int itable = rgb % this.hTable.length;

      while (this.hTable[itable] != null) {
         itable = ++itable % this.hTable.length;
      }

      this.hTable[itable] = new ColorRecord(rgb, ipalette);
   }
}

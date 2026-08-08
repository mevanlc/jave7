package de.jave.lib.cell;

public record Cell(int glyph) {
   public Cell {
      if (!GlyphEncoding.isCodePoint(glyph) && !GlyphEncoding.isCluster(glyph)) {
         throw new IllegalArgumentException("Invalid PHASE1 glyph encoding: " + glyph);
      }
   }

   public String text() {
      if (this.isCluster()) {
         return ClusterTable.INSTANCE.textOf(this.glyph);
      }
      return new String(new int[]{this.glyph}, 0, 1);
   }

   public int columns() {
      if (this.isCluster()) {
         return ClusterTable.INSTANCE.columnsOf(this.glyph);
      }
      return 1;
   }

   public boolean isCluster() {
      return GlyphEncoding.isCluster(this.glyph);
   }
}

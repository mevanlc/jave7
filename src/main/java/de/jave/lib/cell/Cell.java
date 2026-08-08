package de.jave.lib.cell;

import java.text.Normalizer;
import java.util.List;

public record Cell(int glyph) {
   public Cell {
      if (!GlyphEncoding.isCodePoint(glyph) && !GlyphEncoding.isCluster(glyph)) {
         throw new IllegalArgumentException("Invalid PHASE1 glyph encoding: " + glyph);
      }
   }

   public static Cell fromText(String text) {
      String normalized = Normalizer.normalize(text, Normalizer.Form.NFC);
      List<String> graphemes = GraphemeSplitter.split(normalized);
      if (graphemes.size() != 1) {
         throw new IllegalArgumentException("Cell text must contain exactly one grapheme: " + text);
      }

      String grapheme = graphemes.get(0);
      int glyph = grapheme.codePointCount(0, grapheme.length()) == 1
         ? grapheme.codePointAt(0)
         : ClusterTable.INSTANCE.intern(grapheme);
      return new Cell(glyph);
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

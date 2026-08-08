package de.jave.lib.cell;

/** Encoding used by the glyph plane. */
public final class GlyphEncoding {
   public static final int FIRST_CLUSTER_HANDLE = -1;
   public static final int LAST_CLUSTER_HANDLE = -0x3FFF_FFFF;
   public static final int FIRST_RESERVED = -0x4000_0000;
   public static final int CONTINUATION = Integer.MIN_VALUE;

   private GlyphEncoding() {
   }

   public static boolean isCodePoint(int glyph) {
      return Character.isValidCodePoint(glyph)
         && (glyph < Character.MIN_SURROGATE || glyph > Character.MAX_SURROGATE);
   }

   public static boolean isCluster(int glyph) {
      return glyph <= FIRST_CLUSTER_HANDLE && glyph >= LAST_CLUSTER_HANDLE;
   }

   public static boolean isReserved(int glyph) {
      return glyph <= FIRST_RESERVED;
   }

   public static int handleToIndex(int handle) {
      if (!isCluster(handle)) {
         throw new IllegalArgumentException("Not a cluster handle: " + handle);
      }
      return -handle - 1;
   }

   public static int indexToHandle(int index) {
      if (index < 0 || index >= -FIRST_RESERVED - 1) {
         throw new IllegalArgumentException("Cluster-table index enters the reserved glyph range: " + index);
      }
      return -index - 1;
   }
}

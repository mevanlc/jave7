package de.jave.lib.cell;

import org.junit.Assert;
import org.junit.Test;

public class GlyphEncodingTest {
   @Test
   public void partitionsCodePointsClustersAndReservedGlyphs() {
      Assert.assertTrue(GlyphEncoding.isCodePoint(0x10FFFF));
      Assert.assertFalse(GlyphEncoding.isCodePoint(0x11_0000));
      Assert.assertTrue(GlyphEncoding.isCluster(-1));
      Assert.assertTrue(GlyphEncoding.isCluster(-0x3FFF_FFFF));
      Assert.assertFalse(GlyphEncoding.isCluster(GlyphEncoding.FIRST_RESERVED));
      Assert.assertTrue(GlyphEncoding.isReserved(GlyphEncoding.FIRST_RESERVED));
      Assert.assertTrue(GlyphEncoding.isReserved(GlyphEncoding.CONTINUATION));
   }

   @Test
   public void convertsBetweenDenseHandlesAndIndexes() {
      Assert.assertEquals(-1, GlyphEncoding.indexToHandle(0));
      Assert.assertEquals(-2, GlyphEncoding.indexToHandle(1));
      Assert.assertEquals(0, GlyphEncoding.handleToIndex(-1));
      Assert.assertEquals(1, GlyphEncoding.handleToIndex(-2));
      Assert.assertEquals(-0x3FFF_FFFF, GlyphEncoding.indexToHandle(0x3FFF_FFFE));
   }

   @Test(expected = IllegalArgumentException.class)
   public void refusesToAllocateAHandleInReservedSpace() {
      GlyphEncoding.indexToHandle(0x3FFF_FFFF);
   }

   @Test(expected = IllegalArgumentException.class)
   public void refusesToInterpretAReservedGlyphAsAHandle() {
      GlyphEncoding.handleToIndex(GlyphEncoding.FIRST_RESERVED);
   }
}

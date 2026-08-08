package de.jave.lib.cell;

import org.junit.Assert;
import org.junit.Test;

public class CellTest {
   @Test
   public void exposesCodePointTextAndColumns() {
      Cell cell = new Cell(0x1F600);

      Assert.assertEquals("\uD83D\uDE00", cell.text());
      Assert.assertEquals(1, cell.columns());
      Assert.assertFalse(cell.isCluster());
   }

   @Test
   public void exposesClusterTextAndColumns() {
      String text = "x\u20D7";
      Cell cell = new Cell(ClusterTable.INSTANCE.intern(text));

      Assert.assertEquals(text, cell.text());
      Assert.assertEquals(1, cell.columns());
      Assert.assertTrue(cell.isCluster());
   }

   @Test(expected = IllegalArgumentException.class)
   public void rejectsReservedGlyphs() {
      new Cell(GlyphEncoding.CONTINUATION);
   }
}

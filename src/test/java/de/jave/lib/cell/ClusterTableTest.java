package de.jave.lib.cell;

import org.junit.Assert;
import org.junit.Test;

public class ClusterTableTest {
   @Test
   public void internsDistinctClustersAndDeduplicatesText() {
      ClusterTable table = new ClusterTable();

      int first = table.intern("a\u20D7");
      int duplicate = table.intern("a\u20D7");
      int second = table.intern("e\u20DD");

      Assert.assertEquals(-1, first);
      Assert.assertEquals(first, duplicate);
      Assert.assertEquals(-2, second);
      Assert.assertEquals("a\u20D7", table.textOf(first));
      Assert.assertEquals(1, table.columnsOf(first));
      Assert.assertEquals(2, table.size());
      Assert.assertEquals(8, table.internedTextBytes());
   }

   @Test(expected = IllegalArgumentException.class)
   public void rejectsUnknownHandles() {
      new ClusterTable().textOf(-1);
   }
}

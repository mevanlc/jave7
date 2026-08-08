package de.jave.lib.cell;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class GraphemeSplitterTest {
   @Test
   public void splitsThePgaTaxonomyIntoPerceivedCharacters() {
      assertOneCluster("e\u0301");
      assertOneCluster("e\u20DD");
      assertOneCluster("e\u0301\u0327\u20D7");
      assertOneCluster("\u0915\u093E");
      assertOneCluster("\u2764\uFE0F");
      assertOneCluster("\uD83D\uDC4D\uD83C\uDFFB");
      assertOneCluster("\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66");
      assertOneCluster("\uD83C\uDDFA\uD83C\uDDF8");
      assertOneCluster("\u1100\u1161\u11A8");
   }

   @Test
   public void preservesBoundariesBetweenOrdinaryCharacters() {
      Assert.assertEquals(List.of("A", "\uD83D\uDE00", "B"), GraphemeSplitter.split("A\uD83D\uDE00B"));
   }

   private static void assertOneCluster(String text) {
      Assert.assertEquals(List.of(text), GraphemeSplitter.split(text));
   }
}

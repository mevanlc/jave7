package de.jave.jave.algorithm.compress;

import de.jave.lib.CharacterPlate;
import org.junit.Assert;
import org.junit.Test;

public class AsciiPackerTest {
   @Test
   public void legacyAlgorithmsRemainByteIdentical() {
      Assert.assertEquals("B3 1 abc", AsciiPacker.encode(new int[][]{{'a', 'b', 'c'}}));
      int[][] repeated = new int[1][41];
      java.util.Arrays.fill(repeated[0], 'a');
      Assert.assertEquals("A41 1 %41a", AsciiPacker.encode(repeated));
   }

   @Test
   public void algorithmCRoundTripsCodePointsClustersAndPercent() {
      CharacterPlate content = new CharacterPlate("A\uD83D\uDE00e\u20DD%");

      String encoded = AsciiPacker.encode(content);
      int[][] decoded = AsciiPacker.decode(encoded);

      Assert.assertEquals("C4 1 A\uD83D\uDE00%{2:e\u20DD}%%", encoded);
      Assert.assertEquals(content, new CharacterPlate(decoded));
   }

   @Test
   public void allSpaceBEncodedContentKeepsDimensionsWhenPayloadIsTrimmed() {
      int[][] content = new int[5][5];
      for (int y = 0; y < content.length; y++) {
         for (int x = 0; x < content[y].length; x++) {
            content[y][x] = ' ';
         }
      }

      String encoded = AsciiPacker.encode(content);
      int[][] decoded = AsciiPacker.decode(encoded);

      Assert.assertEquals("B5 5", encoded);
      Assert.assertEquals(5, decoded.length);
      Assert.assertEquals(5, decoded[0].length);
      for (int y = 0; y < decoded.length; y++) {
         for (int x = 0; x < decoded[y].length; x++) {
            Assert.assertEquals(' ', decoded[y][x]);
         }
      }
   }

   @Test
   public void missingTrailingBPayloadIsReadAsSpaces() {
      int[][] decoded = AsciiPacker.decode("B3 2");

      Assert.assertEquals(2, decoded.length);
      Assert.assertEquals(3, decoded[0].length);
      Assert.assertEquals(' ', decoded[0][0]);
      Assert.assertEquals(' ', decoded[1][2]);
   }
}

package de.jave.jave.algorithm.compress;

import org.junit.Assert;
import org.junit.Test;

public class AsciiPackerTest {
   @Test
   public void allSpaceBEncodedContentKeepsDimensionsWhenPayloadIsTrimmed() {
      char[][] content = new char[5][5];
      for (int y = 0; y < content.length; y++) {
         for (int x = 0; x < content[y].length; x++) {
            content[y][x] = ' ';
         }
      }

      String encoded = AsciiPacker.encode(content);
      char[][] decoded = AsciiPacker.decode(encoded);

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
      char[][] decoded = AsciiPacker.decode("B3 2");

      Assert.assertEquals(2, decoded.length);
      Assert.assertEquals(3, decoded[0].length);
      Assert.assertEquals(' ', decoded[0][0]);
      Assert.assertEquals(' ', decoded[1][2]);
   }
}

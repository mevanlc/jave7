package de.jave.lib;

import de.jave.jave.JaveSelection;
import de.jave.jave.algorithm.Rotate180StaticAction;
import de.jave.lib.cell.GlyphEncoding;
import java.text.Normalizer;
import org.junit.Assert;
import org.junit.Test;

public class CharacterPlateCellModelTest {
   private static final String[] PGA_CORPUS = {
      "e\u0301",
      "e\u20DD",
      "e\u0301\u0327\u20D7",
      "\u0915\u093E",
      "\u2764\uFE0F",
      "\uD83D\uDC4D\uD83C\uDFFB",
      "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66",
      "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F",
      "\uD83C\uDDFA\uD83C\uDDF8",
      "\u1100\u1161\u11A8"
   };

   @Test
   public void pgaCorpusOccupiesOneCellPerPerceivedCharacter() {
      for (String example : PGA_CORPUS) {
         CharacterPlate plate = new CharacterPlate(example + "X");

         Assert.assertEquals(example, 2, plate.getWidth());
         Assert.assertEquals(Normalizer.normalize(example, Normalizer.Form.NFC), plate.textAt(0, 0));
         Assert.assertEquals("X", plate.textAt(1, 0));
      }
   }

   @Test
   public void supplementaryCodePointUsesOneDirectGlyphCell() {
      CharacterPlate plate = new CharacterPlate("A\uD83D\uDE00B");

      Assert.assertEquals(3, plate.getWidth());
      Assert.assertEquals(0x1F600, plate.glyphAt(1, 0));
      Assert.assertTrue(GlyphEncoding.isCodePoint(plate.glyphAt(1, 0)));
   }

   @Test
   public void textGridTextRoundTripProducesNfcText() {
      String text = "e\u0301 and e\u20DD\n\uD83D\uDE00 \uD83C\uDDFA\uD83C\uDDF8";

      Assert.assertEquals(Normalizer.normalize(text, Normalizer.Form.NFC), new CharacterPlate(text).asString());
   }

   @Test
   public void movementAlgorithmsKeepClusterCellsAtomic() {
      JaveSelection selection = new JaveSelection(new CharacterPlate("Ae\u20DDB"));

      Rotate180StaticAction.applyTo(selection);

      Assert.assertEquals("Be\u20DDA", selection.getContent().asString());
      Assert.assertTrue(selection.getContent().cellAt(1, 0).isCluster());
   }
}

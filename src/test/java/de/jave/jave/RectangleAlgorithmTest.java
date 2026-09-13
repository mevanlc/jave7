package de.jave.jave;

import de.jave.jave.algorithm.rectangle.RectangleStyle;
import de.jave.jave.rectangle.RectangleStyleObjectUi;
import de.jave.lib.CharacterPlate;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class RectangleAlgorithmTest {
   @Test
   public void everyStyleHasEightCharactersAndItsOwnLabel() {
      Set<String> labels = new HashSet<>();

      for (RectangleStyle style : RectangleStyle.values()) {
         Assert.assertEquals(
            "characters of " + style, RectangleAlgorithm.CHARACTERS, RectangleAlgorithm.getCharsForStyle(style).length
         );
         Assert.assertTrue("duplicate label for " + style, labels.add(new RectangleStyleObjectUi().getLabel(style)));
      }
   }

   @Test
   public void theUnicodeStylesDrawLightRoundedBoldAndDoubleBoxes() {
      Assert.assertArrayEquals(
         new String[]{"┌──┐", "│  │", "└──┘"}, drawBox(RectangleStyle.UNICODE_REGULAR)
      );
      Assert.assertArrayEquals(
         new String[]{"╭──╮", "│  │", "╰──╯"}, drawBox(RectangleStyle.UNICODE_ROUNDED)
      );
      Assert.assertArrayEquals(
         new String[]{"┏━━┓", "┃  ┃", "┗━━┛"}, drawBox(RectangleStyle.UNICODE_BOLD)
      );
      Assert.assertArrayEquals(
         new String[]{"╔══╗", "║  ║", "╚══╝"}, drawBox(RectangleStyle.UNICODE_DOUBLE)
      );
   }

   @Test
   public void aDrawnRectangleIsRecognizedAsItsOwnStyle() {
      for (RectangleStyle style : RectangleStyle.values()) {
         if (style != RectangleStyle.UNDERSCORE && style != RectangleStyle.CHARACTERS) {
            CharacterPlate plate = new CharacterPlate(6, 5);
            RectangleAlgorithm.drawRectangle(plate, new Rectangle(0, 0, 6, 5), style);

            Assert.assertEquals(style, RectangleAlgorithm.getRectangleStyle(plate.glyphPlane()));
         }
      }
   }

   @Test
   public void roundedSettingsScreenIsRecognizedAsARoundedTextbox() throws IOException {
      try (InputStream input = getClass().getResourceAsStream("rounded-settings-box.txt")) {
         Assert.assertNotNull("missing rounded settings screen fixture", input);
         String[] rows = new String(input.readAllBytes(), StandardCharsets.UTF_8).lines().toArray(String[]::new);
         CharacterPlate plate = new CharacterPlate(rows);

         Assert.assertEquals(RectangleStyle.UNICODE_ROUNDED, RectangleAlgorithm.getRectangleStyle(plate.glyphPlane()));

         Selection selection = new Selection();
         selection.set(new Point(0, 0), plate);
         Assert.assertTrue(selection.isTextbox());
         Assert.assertEquals(RectangleStyle.UNICODE_ROUNDED, selection.getTextboxStyle());
         Assert.assertArrayEquals(rows, selection.getContent().toStringArray());
      }
   }

   private static String[] drawBox(RectangleStyle style) {
      CharacterPlate plate = new CharacterPlate(4, 3);
      RectangleAlgorithm.drawRectangle(plate, new Rectangle(0, 0, 4, 3), style);
      return plate.toStringArray();
   }
}

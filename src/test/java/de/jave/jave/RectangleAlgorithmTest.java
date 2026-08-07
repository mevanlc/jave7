package de.jave.jave;

import de.jave.jave.algorithm.rectangle.RectangleStyle;
import de.jave.jave.rectangle.RectangleStyleObjectUi;
import de.jave.lib.CharacterPlate;
import java.awt.Rectangle;
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
   public void theUnicodeStylesDrawLightBoldAndDoubleBoxes() {
      Assert.assertArrayEquals(
         new String[]{"┌──┐", "│  │", "└──┘"}, drawBox(RectangleStyle.UNICODE_REGULAR)
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

            Assert.assertEquals(style, RectangleAlgorithm.getRectangleStyle(plate.getContent()));
         }
      }
   }

   private static String[] drawBox(RectangleStyle style) {
      CharacterPlate plate = new CharacterPlate(4, 3);
      RectangleAlgorithm.drawRectangle(plate, new Rectangle(0, 0, 4, 3), style);
      return plate.toStringArray();
   }
}

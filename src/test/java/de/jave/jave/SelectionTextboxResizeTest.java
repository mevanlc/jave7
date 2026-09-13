package de.jave.jave;

import de.jave.lib.CharacterPlate;
import java.awt.Point;
import org.junit.Assert;
import org.junit.Test;

public class SelectionTextboxResizeTest {
   private static final String[] DIVIDED_BOX = new String[]{
      "┏━━━━━┓",
      "┃     ┃",
      "┠─────┨",
      "┃     ┃",
      "┗━━━━━┛"
   };
   private static final String[] ROUNDED_DIVIDED_BOX = new String[]{
      "╭────────╮",
      "│        │",
      "│ hello  │",
      "│────────│",
      "│ all ok │",
      "│        │",
      "╰────────╯"
   };

   @Test
   public void growingEastwardsExtendsAnInternalHorizontalLine() {
      Selection selection = selectionOf(DIVIDED_BOX);

      selection.resizeTextboxE(2);

      Assert.assertArrayEquals(new String[]{
         "┏━━━━━━━┓",
         "┃       ┃",
         "┠───────┨",
         "┃       ┃",
         "┗━━━━━━━┛"
      }, selection.getContent().toStringArray());
   }

   @Test
   public void growingWestwardsExtendsAnInternalHorizontalLine() {
      Selection selection = selectionOf(DIVIDED_BOX);

      selection.resizeTextboxW(-2);

      Assert.assertArrayEquals(new String[]{
         "┏━━━━━━━┓",
         "┃       ┃",
         "┠───────┨",
         "┃       ┃",
         "┗━━━━━━━┛"
      }, selection.getContent().toStringArray());
   }

   @Test
   public void growingSouthwardsExtendsAnInternalVerticalLine() {
      Selection selection = selectionOf(new String[]{
         "┏━┳━┓",
         "┃ ┃ ┃",
         "┗━┻━┛"
      });

      selection.resizeTextboxS(2);

      Assert.assertArrayEquals(new String[]{
         "┏━┳━┓",
         "┃ ┃ ┃",
         "┃ ┃ ┃",
         "┃ ┃ ┃",
         "┗━┻━┛"
      }, selection.getContent().toStringArray());
   }

   @Test
   public void growingNorthwardsExtendsAnInternalVerticalLine() {
      Selection selection = selectionOf(new String[]{
         "┏━┳━┓",
         "┃ ┃ ┃",
         "┗━┻━┛"
      });

      selection.resizeTextboxN(-2);

      Assert.assertArrayEquals(new String[]{
         "┏━┳━┓",
         "┃ ┃ ┃",
         "┃ ┃ ┃",
         "┃ ┃ ┃",
         "┗━┻━┛"
      }, selection.getContent().toStringArray());
   }

   @Test
   public void growingExtendsAnAsciiDivider() {
      Selection selection = selectionOf(new String[]{
         "+---+",
         "|   |",
         "+---+",
         "|   |",
         "+---+"
      });

      selection.resizeTextboxE(2);

      Assert.assertArrayEquals(new String[]{
         "+-----+",
         "|     |",
         "+-----+",
         "|     |",
         "+-----+"
      }, selection.getContent().toStringArray());
   }

   @Test
   public void growingLeavesTextRowsAlone() {
      Selection selection = selectionOf(new String[]{
         "┌────────┐",
         "│ hello  │",
         "│ all ok │",
         "└────────┘"
      });

      selection.resizeTextboxE(2);

      Assert.assertArrayEquals(new String[]{
         "┌──────────┐",
         "│ hello    │",
         "│ all ok   │",
         "└──────────┘"
      }, selection.getContent().toStringArray());
   }

   @Test
   public void shrinkingAndGrowingAgainRestoresTheClippedText() {
      Selection selection = selectionOf(new String[]{
         "┌────────┐",
         "│ hello  │",
         "└────────┘"
      });

      selection.resizeTextboxE(-4);
      selection.resizeTextboxE(4);

      Assert.assertArrayEquals(new String[]{
         "┌────────┐",
         "│ hello  │",
         "└────────┘"
      }, selection.getContent().toStringArray());
   }

   @Test
   public void growingARoundedBoxEastwardsPreservesTextAndExtendsItsDivider() {
      Selection selection = selectionOf(ROUNDED_DIVIDED_BOX);

      selection.resizeTextboxE(2);

      Assert.assertArrayEquals(new String[]{
         "╭──────────╮",
         "│          │",
         "│ hello    │",
         "│──────────│",
         "│ all ok   │",
         "│          │",
         "╰──────────╯"
      }, selection.getContent().toStringArray());
   }

   @Test
   public void growingARoundedBoxWestwardsPreservesTextAndExtendsItsDivider() {
      Selection selection = selectionOf(ROUNDED_DIVIDED_BOX);

      selection.resizeTextboxW(-2);

      Assert.assertArrayEquals(new String[]{
         "╭──────────╮",
         "│          │",
         "│   hello  │",
         "│──────────│",
         "│   all ok │",
         "│          │",
         "╰──────────╯"
      }, selection.getContent().toStringArray());
   }

   @Test
   public void growingARoundedBoxNorthwardsPreservesItsContents() {
      Selection selection = selectionOf(ROUNDED_DIVIDED_BOX);

      selection.resizeTextboxN(-2);

      Assert.assertArrayEquals(new String[]{
         "╭────────╮",
         "│        │",
         "│        │",
         "│        │",
         "│ hello  │",
         "│────────│",
         "│ all ok │",
         "│        │",
         "╰────────╯"
      }, selection.getContent().toStringArray());
   }

   @Test
   public void growingARoundedBoxSouthwardsPreservesItsContents() {
      Selection selection = selectionOf(ROUNDED_DIVIDED_BOX);

      selection.resizeTextboxS(2);

      Assert.assertArrayEquals(new String[]{
         "╭────────╮",
         "│        │",
         "│ hello  │",
         "│────────│",
         "│ all ok │",
         "│        │",
         "│        │",
         "│        │",
         "╰────────╯"
      }, selection.getContent().toStringArray());
   }

   @Test
   public void shrinkingAndGrowingARoundedBoxRestoresClippedContentsInEveryDirection() {
      Selection selection = selectionOf(ROUNDED_DIVIDED_BOX);

      selection.resizeTextboxE(-4);
      selection.resizeTextboxE(4);
      Assert.assertArrayEquals(ROUNDED_DIVIDED_BOX, selection.getContent().toStringArray());

      selection.resizeTextboxW(4);
      selection.resizeTextboxW(-4);
      Assert.assertArrayEquals(ROUNDED_DIVIDED_BOX, selection.getContent().toStringArray());

      selection.resizeTextboxN(2);
      selection.resizeTextboxN(-2);
      Assert.assertArrayEquals(ROUNDED_DIVIDED_BOX, selection.getContent().toStringArray());

      selection.resizeTextboxS(-2);
      selection.resizeTextboxS(2);
      Assert.assertArrayEquals(ROUNDED_DIVIDED_BOX, selection.getContent().toStringArray());
   }

   private static Selection selectionOf(String[] rows) {
      Selection selection = new Selection();
      selection.set(new Point(0, 0), new CharacterPlate(rows));

      Assert.assertTrue("expected the box to be recognized as a textbox", selection.isTextbox());
      return selection;
   }
}

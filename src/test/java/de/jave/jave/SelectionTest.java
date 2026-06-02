package de.jave.jave;

import de.jave.awt.clipboard.JaveClipboardSelection;
import de.jave.lib.CharacterPlate;
import java.awt.Point;
import org.junit.Assert;
import org.junit.Test;

public class SelectionTest {
   @Test
   public void restoringZeroHeightSelectionClearsSelection() {
      Selection selection = new Selection();

      selection.set(new Point(2, 3), new char[0][0], null);

      Assert.assertFalse(selection.hasSelection());
      Assert.assertNull(selection.getContent());
      Assert.assertNull(selection.getRegion());
   }

   @Test
   public void restoringZeroWidthSelectionClearsSelection() {
      Selection selection = new Selection();

      selection.set(new Point(2, 3), new char[1][0], null);

      Assert.assertFalse(selection.hasSelection());
      Assert.assertNull(selection.getContent());
      Assert.assertNull(selection.getRegion());
   }

   @Test
   public void settingZeroSizedCharacterPlateClearsSelection() {
      Selection selection = new Selection();

      selection.set(new Point(2, 3), new CharacterPlate(""));

      Assert.assertFalse(selection.hasSelection());
      Assert.assertNull(selection.getContent());
      Assert.assertNull(selection.getRegion());
   }

   @Test
   public void settingZeroSizedClipboardSelectionClearsSelection() {
      Selection selection = new Selection();

      selection.set(new Point(2, 3), new JaveClipboardSelection(""));

      Assert.assertFalse(selection.hasSelection());
      Assert.assertNull(selection.getContent());
      Assert.assertNull(selection.getRegion());
   }
}

package net.dizzy.commons.swing.fontchooser.view;

import java.awt.Font;
import java.util.Arrays;

import javax.swing.JList;
import javax.swing.JScrollPane;

import net.dizzy.commons.swing.fontchooser.util.FontUtilities;
import org.junit.Assert;
import org.junit.Test;

public class FontChooserDialogTest {
   @Test
   public void fixedWidthFontListContainsOnlyFixedWidthFonts() {
      String[] families = FontChooserDialog.availableFontFamilies(true);

      Assert.assertTrue("expected at least one fixed-width font", families.length > 0);
      Assert.assertTrue(
         Arrays.stream(families).allMatch(name -> FontUtilities.isFixedWidth(new Font(name, Font.PLAIN, 12)))
      );
   }

   @Test
   public void mapsAwtStylesToChooserIndexes() {
      Assert.assertEquals(0, FontChooserDialog.styleIndex(Font.PLAIN));
      Assert.assertEquals(1, FontChooserDialog.styleIndex(Font.BOLD));
      Assert.assertEquals(2, FontChooserDialog.styleIndex(Font.ITALIC));
      Assert.assertEquals(3, FontChooserDialog.styleIndex(Font.BOLD | Font.ITALIC));
   }

   @Test
   public void fontFamilyViewportKeepsItsTwelveRowHeightAfterSelectionChanges() {
      JList<String> familyList = new JList<>(new String[]{"Monaco", "Monospaced"});
      JScrollPane scrollPane = FontChooserDialog.createFamilyScrollPane(familyList);
      int expectedHeight = familyList.getFixedCellHeight() * 12;

      Assert.assertEquals(expectedHeight, scrollPane.getViewport().getPreferredSize().height);
      Assert.assertEquals(scrollPane.getPreferredSize(), scrollPane.getMinimumSize());

      familyList.setSelectedIndex(0);
      familyList.setSelectedIndex(1);

      Assert.assertEquals(expectedHeight, scrollPane.getViewport().getPreferredSize().height);
      Assert.assertEquals(scrollPane.getPreferredSize(), scrollPane.getMinimumSize());
   }
}

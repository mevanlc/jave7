package de.jave.jave.menu;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.junit.Assert;
import org.junit.Test;

public class MenuSearchModelTest {
   private static MenuEntry entry(String id, String name) {
      Action action = MenuHub.action(name, parent -> {});
      return new MenuEntry(new MenuCommand(id, action), new JMenuItem(action), List.of(new JMenu("Parent")));
   }

   @Test
   public void allWordsMatchOnlyTheBasenameAndKeepMenuOrder() {
      MenuEntry first = entry("first", "Rotate dynamic left");
      MenuEntry second = entry("second", "Rotate static left");
      MenuEntry duplicate = entry("third", "Rotate dynamic left");
      List<MenuEntry> entries = List.of(first, second, duplicate);
      Assert.assertEquals(List.of(first, duplicate), MenuSearchModel.match(entries, "  LEFT   DYNAMIC ", List.of()));
      Assert.assertEquals(List.of(), MenuSearchModel.match(entries, "parent", List.of()));
      Assert.assertEquals(entries, MenuSearchModel.match(entries, "rotate", List.of()));
   }

   @Test
   public void blankQueryResolvesHistoryAgainstCurrentLabelsAndAvailableTargets() {
      MenuEntry changed = entry("undo", "Undo draw rectangle");
      MenuEntry copy = entry("copy", "Copy");
      Assert.assertEquals(List.of(copy, changed), MenuSearchModel.match(List.of(changed, copy), " \t ", List.of("missing", "copy", "undo")));
      Assert.assertEquals(List.of(), MenuSearchModel.match(List.of(changed), "", List.of()));
   }

   @Test
   public void disabledCommandsStayInResults() {
      MenuEntry disabled = entry("copy", "Copy");
      disabled.command().action().setEnabled(false);
      Assert.assertEquals(List.of(disabled), MenuSearchModel.match(List.of(disabled), "copy", List.of()));
   }

   @Test
   public void overflowReplacesLastRowAndCountsExactlyTheHiddenResults() {
      List<MenuEntry> entries = new ArrayList<>();
      for (int i = 0; i < 13; i++) {
         entries.add(entry("item." + i, "Item " + i));
      }
      Assert.assertEquals(12, MenuSearchModel.page(entries.subList(0, 12), 12).entries().size());
      Assert.assertEquals(0, MenuSearchModel.page(entries.subList(0, 12), 12).omitted());
      Assert.assertEquals(11, MenuSearchModel.page(entries, 12).entries().size());
      Assert.assertEquals(2, MenuSearchModel.page(entries, 12).omitted());
      Assert.assertEquals(11, MenuSearchModel.page(entries, 3).omitted());
      Assert.assertEquals(13, MenuSearchModel.page(entries, 1).omitted());
   }

   @Test
   public void focusCycleIncludesTheFieldInBothDirectionsAndHandlesNoResults() {
      Assert.assertEquals(0, MenuSearchModel.nextFocus(-1, 1, 3));
      Assert.assertEquals(1, MenuSearchModel.nextFocus(0, 1, 3));
      Assert.assertEquals(-1, MenuSearchModel.nextFocus(2, 1, 3));
      Assert.assertEquals(2, MenuSearchModel.nextFocus(-1, -1, 3));
      Assert.assertEquals(-1, MenuSearchModel.nextFocus(0, -1, 3));
      Assert.assertEquals(-1, MenuSearchModel.nextFocus(-1, 1, 0));
      Assert.assertEquals(-1, MenuSearchModel.nextFocus(-1, -1, 0));
   }

   @Test
   public void endElisionUsesFontWidthAndPreservesGraphemeClusters() {
      var graphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
      try {
         var metrics = graphics.getFontMetrics(new Font(Font.DIALOG, Font.PLAIN, 14));
         String prefix = "e\u0301 👩‍💻 ";
         int width = metrics.stringWidth(prefix + "…");
         Assert.assertEquals(prefix + "…", MenuSearchModel.elide(prefix + "WWWWWWWWWW", metrics, width));
         Assert.assertEquals("Copy", MenuSearchModel.elide("Copy", metrics, metrics.stringWidth("Copy")));
      } finally {
         graphics.dispose();
      }
   }

   @Test
   public void previewsShiftLeftOfResultsAndStayOnScreen() {
      Rectangle reserved = new Rectangle(600, 30, 320, 300);
      Rectangle screen = new Rectangle(0, 25, 1000, 800);
      Rectangle result = MenuLocationPreview.position(new Rectangle(650, 30, 180, 200), reserved, screen);
      Assert.assertEquals(414, result.x);
      Assert.assertFalse(result.intersects(reserved));
      Assert.assertTrue(screen.contains(result));
   }
}

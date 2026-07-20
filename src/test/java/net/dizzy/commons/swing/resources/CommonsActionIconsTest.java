package net.dizzy.commons.swing.resources;

import static org.junit.Assert.assertTrue;

import javax.swing.Icon;
import net.dizzy.commons.swing.fontchooser.resources.DizzyCommonsSwingFontChooserIcons;
import org.junit.Test;

/**
 * Smoke test that the edit/font action icons resolve to real bundled art rather than the empty
 * fallback. A missing resource would leave a zero-width icon, so any positive size confirms the
 * gif was found and decoded (at whatever icon-size preference the test host happens to have).
 */
public class CommonsActionIconsTest {
   @Test
   public void editActionIconsLoadRealArt() {
      Icon[] icons = {
         DizzyCommonsSwingIconResources.CUT,
         DizzyCommonsSwingIconResources.COPY,
         DizzyCommonsSwingIconResources.PASTE,
         DizzyCommonsSwingIconResources.UNDO_MODERN,
         DizzyCommonsSwingIconResources.REDO_MODERN,
      };
      for (Icon icon : icons) {
         assertTrue("edit icon should load real 16px+ art", icon.getIconWidth() >= 16 && icon.getIconHeight() >= 16);
      }
   }

   @Test
   public void fontChooserIconLoadsRealArt() {
      Icon font = DizzyCommonsSwingFontChooserIcons.FONT_ICON;
      assertTrue("font icon should load real 16px+ art", font.getIconWidth() >= 16 && font.getIconHeight() >= 16);
   }
}

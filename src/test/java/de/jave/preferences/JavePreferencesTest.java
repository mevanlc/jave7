package de.jave.preferences;

import de.jave.jave.actions.preferences.DisplayPreferencesPanel;
import de.jave.jave.preferences.GlyphEmulation;
import de.jave.jave.preferences.PlatePreferences;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.prefs.Preferences;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.junit.Assert;
import org.junit.Test;

public class JavePreferencesTest {
   @Test
   public void glyphEmulationDefaultsToJaveAndSavesOnlyWhenDisplayPreferencesAreAccepted() throws Exception {
      Preferences preferencesRoot = Preferences.userRoot();
      Preferences node = preferencesRoot.node("JavE-test-" + UUID.randomUUID());
      try {
         SwingUtilities.invokeAndWait(() -> {
            PlatePreferences preferences = new PlatePreferences(new JavePreferences(node));
            Assert.assertEquals(GlyphEmulation.JAVE, preferences.getGlyphEmulation());
            AtomicInteger changes = new AtomicInteger();
            preferences.getGlyphEmulationModel().addChangeListener(changes::incrementAndGet);
            DisplayPreferencesPanel canceled = new DisplayPreferencesPanel(preferences);
            glyphCombo(canceled).setSelectedItem(GlyphEmulation.KITTY);
            canceled.revert();
            Assert.assertEquals(GlyphEmulation.JAVE, preferences.getGlyphEmulation());
            Assert.assertEquals(0, changes.get());
            DisplayPreferencesPanel accepted = new DisplayPreferencesPanel(preferences);
            glyphCombo(accepted).setSelectedItem(GlyphEmulation.KITTY);
            accepted.savePreferences();
            preferences.flush();
            Assert.assertEquals(1, changes.get());
            PlatePreferences restored = new PlatePreferences(new JavePreferences(node));
            Assert.assertEquals(GlyphEmulation.KITTY, restored.getGlyphEmulation());
            Assert.assertEquals(GlyphEmulation.KITTY, glyphCombo(new DisplayPreferencesPanel(restored)).getSelectedItem());
            restored.setGlyphEmulation(GlyphEmulation.JAVE);
            Assert.assertEquals(GlyphEmulation.JAVE, new PlatePreferences(new JavePreferences(node)).getGlyphEmulation());
         });
      } finally {
         node.removeNode();
         preferencesRoot.flush();
      }
   }

   private static JComboBox<?> glyphCombo(DisplayPreferencesPanel panel) {
      for (var component : panel.getContent().getComponents()) {
         if (component instanceof JLabel label && label.getText().equals("Glyph emulation:")) {
            return (JComboBox<?>)label.getLabelFor();
         }
      }
      throw new AssertionError("Missing Glyph emulation dropdown");
   }

   @Test
   public void layersPanelVisibilityIsRestoredByANewPreferencesInstance() throws Exception {
      Preferences preferencesRoot = Preferences.userRoot();
      Preferences preferencesNode = preferencesRoot.node("JavE-test-" + UUID.randomUUID());

      try {
         JavePreferences preferences = new JavePreferences(preferencesNode);
         preferences.setLayersPanelShownByDefault(true);

         JavePreferences restoredPreferences = new JavePreferences(preferencesNode);

         Assert.assertTrue(restoredPreferences.isLayersPanelShownByDefault());

         restoredPreferences.setLayersPanelShownByDefault(false);

         Assert.assertFalse(new JavePreferences(preferencesNode).isLayersPanelShownByDefault());
      } finally {
         preferencesNode.removeNode();
         preferencesRoot.flush();
      }
   }
}

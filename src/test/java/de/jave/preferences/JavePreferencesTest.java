package de.jave.preferences;

import java.util.UUID;
import java.util.prefs.Preferences;
import org.junit.Assert;
import org.junit.Test;

public class JavePreferencesTest {
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

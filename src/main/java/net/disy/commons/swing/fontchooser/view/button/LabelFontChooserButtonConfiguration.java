package net.disy.commons.swing.fontchooser.view.button;

import net.disy.commons.swing.action.ActionConfiguration;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;

public class LabelFontChooserButtonConfiguration extends ActionConfiguration {
   public LabelFontChooserButtonConfiguration() {
      this(DisyCommonsSwingFontChooserMessages.getString("FontChooserButton.defaultLabel.text"));
   }

   public LabelFontChooserButtonConfiguration(String label) {
      super(label);
   }
}

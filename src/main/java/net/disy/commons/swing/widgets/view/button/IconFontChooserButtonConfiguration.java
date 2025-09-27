package net.disy.commons.swing.fontchooser.view.button;

import net.disy.commons.swing.action.ActionConfiguration;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserIcons;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;

public class IconFontChooserButtonConfiguration extends ActionConfiguration {
   public IconFontChooserButtonConfiguration() {
      super(null, DisyCommonsSwingFontChooserIcons.FONT_ICON, DisyCommonsSwingFontChooserMessages.getString("IconFontChooserButton.TooltipText"));
   }
}

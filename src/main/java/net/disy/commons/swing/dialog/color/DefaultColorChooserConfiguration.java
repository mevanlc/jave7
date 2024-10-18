package net.disy.commons.swing.dialog.color;

import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;

public class DefaultColorChooserConfiguration implements IColorChooserConfiguration {
   private String colorChooserDialogTitle;
   private boolean transparencyEnabled;

   public DefaultColorChooserConfiguration() {
      this(DisyCommonsSwingMessages.getString("ColorChooserButton.DefaultTitle"), false);
   }

   public DefaultColorChooserConfiguration(boolean transparencyEnabled) {
      this(DisyCommonsSwingMessages.getString("ColorChooserButton.DefaultTitle"), transparencyEnabled);
   }

   public DefaultColorChooserConfiguration(String colorChooserDialogTitle, boolean transparencyEnabled) {
      Ensure.ensureArgumentNotNull(colorChooserDialogTitle);
      this.colorChooserDialogTitle = colorChooserDialogTitle;
      this.transparencyEnabled = transparencyEnabled;
   }

   @Override
   public boolean isTransparencyEnabled() {
      return this.transparencyEnabled;
   }

   @Override
   public String getColorChooserDialogTitle() {
      return this.colorChooserDialogTitle;
   }

   public void setColorChooserDialogTitle(String colorChooserDialogTitle) {
      Ensure.ensureArgumentNotNull(colorChooserDialogTitle);
      this.colorChooserDialogTitle = colorChooserDialogTitle;
   }

   public void setTransparencyEnabled(boolean transparencyEnabled) {
      this.transparencyEnabled = transparencyEnabled;
   }
}

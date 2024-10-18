package net.disy.commons.swing.dialog.core;

import javax.swing.Icon;
import net.disy.commons.core.util.Ensure;

public class DialogHeaderPanelConfiguration implements IDialogHeaderPanelConfiguration {
   private final Icon icon;
   private final boolean visible;

   public static IDialogHeaderPanelConfiguration createVisibleWithoutIcon() {
      return new DialogHeaderPanelConfiguration(null, true);
   }

   public static IDialogHeaderPanelConfiguration createVisibleWithIcon(Icon icon) {
      Ensure.ensureArgumentNotNull(icon);
      return new DialogHeaderPanelConfiguration(icon, true);
   }

   public static IDialogHeaderPanelConfiguration createInvisible() {
      return new DialogHeaderPanelConfiguration(null, false);
   }

   private DialogHeaderPanelConfiguration(Icon icon, boolean visible) {
      this.icon = icon;
      this.visible = visible;
   }

   @Override
   public Icon getLargeDialogIcon() {
      return this.icon;
   }

   @Override
   public boolean isHeaderPanelVisible() {
      return this.visible;
   }
}

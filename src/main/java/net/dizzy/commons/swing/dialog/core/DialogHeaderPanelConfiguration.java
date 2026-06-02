package net.dizzy.commons.swing.dialog.core;

import javax.swing.Icon;

public class DialogHeaderPanelConfiguration implements IDialogHeaderPanelConfiguration {
   private final boolean visible;
   private final Icon icon;

   private DialogHeaderPanelConfiguration(boolean visible, Icon icon) {
      this.visible = visible;
      this.icon = icon;
   }

   public static DialogHeaderPanelConfiguration createInvisible() {
      return new DialogHeaderPanelConfiguration(false, null);
   }

   public static DialogHeaderPanelConfiguration createVisibleWithIcon(Icon icon) {
      return new DialogHeaderPanelConfiguration(true, icon);
   }

   @Override public boolean isVisible() { return visible; }
   @Override public Icon getIcon() { return icon; }
}

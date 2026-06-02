package net.dizzy.commons.swing.dialog.userdialog.buttons;

public final class DialogButtonConfigurationFactory {
   private DialogButtonConfigurationFactory() {
   }

   public static DialogButtonConfiguration createCloseOnly() {
      return new DialogButtonConfiguration(false, true, false);
   }

   public static DialogButtonConfiguration createOkOnly() {
      return new DialogButtonConfiguration(true, false, false);
   }

   public static DialogButtonConfiguration createOkCancel() {
      return new DialogButtonConfiguration(true, true, false);
   }

   public static DialogButtonConfiguration createYesCancel() {
      return new DialogButtonConfiguration(true, true, true);
   }

   public static final class DialogButtonConfiguration {
      private final boolean okVisible;
      private final boolean cancelVisible;
      private final boolean yesLabel;

      private DialogButtonConfiguration(boolean okVisible, boolean cancelVisible, boolean yesLabel) {
         this.okVisible = okVisible;
         this.cancelVisible = cancelVisible;
         this.yesLabel = yesLabel;
      }

      public boolean isOkVisible() { return okVisible; }
      public boolean isCancelVisible() { return cancelVisible; }
      public String getOkLabel() { return yesLabel ? "Yes" : "OK"; }
      public String getCancelLabel() { return okVisible ? "Cancel" : "Close"; }
   }
}

package net.disy.commons.swing.dialog.userdialog.buttons;

import net.disy.commons.swing.action.ActionConfiguration;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogMessages;

public class DialogButtonConfigurationFactory {
   public static IDialogButtonConfiguration createOkOnly() {
      return createOkOnly(DialogButtonConfiguration.DEFAULT_OK_CONFIG);
   }

   public static IDialogButtonConfiguration createOkOnly(String okText) {
      return createOkOnly(new ActionConfiguration(okText));
   }

   public static IDialogButtonConfiguration createCloseOnly() {
      return createOkOnly(DisyCommonsSwingDialogMessages.CLOSE);
   }

   public static IDialogButtonConfiguration createOkCancelWithCancelText(String cancelText) {
      return createOkCancelWithTexts(DisyCommonsSwingDialogMessages.OK, cancelText);
   }

   public static IDialogButtonConfiguration createOkCancelWithOkText(String okText) {
      return createOkCancelWithTexts(okText, DisyCommonsSwingDialogMessages.CANCEL);
   }

   public static IDialogButtonConfiguration createOkCancelWithTexts(String okText, String cancelText) {
      return new DialogButtonConfiguration(okText, cancelText);
   }

   public static IDialogButtonConfiguration createOkCancel() {
      return new DialogButtonConfiguration();
   }

   private static DialogButtonConfiguration createOkOnly(ActionConfiguration config) {
      return new DialogButtonConfiguration(config, null);
   }

   public static IDialogButtonConfiguration createCancelOnly() {
      return new DialogButtonConfiguration(null, DialogButtonConfiguration.DEFAULT_CANCEL_CONFIG);
   }

   public static IDialogButtonConfiguration createNone() {
      return new DialogButtonConfiguration(null, (IActionConfiguration)null);
   }

   public static IDialogButtonConfiguration createYesCancel() {
      return createOkCancelWithTexts(DisyCommonsSwingDialogMessages.YES, DisyCommonsSwingDialogMessages.CANCEL);
   }

   public static IDialogButtonConfiguration createYesNo() {
      return createOkCancelWithTexts(DisyCommonsSwingDialogMessages.YES, DisyCommonsSwingDialogMessages.NO);
   }
}

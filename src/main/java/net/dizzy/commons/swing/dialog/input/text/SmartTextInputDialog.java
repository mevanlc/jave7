package net.dizzy.commons.swing.dialog.input.text;

import java.awt.Component;

import javax.swing.JOptionPane;

public final class SmartTextInputDialog {
   private SmartTextInputDialog() {
   }

   public static ITextInputDialogResult showTextInputDialog(Component parent, ITextInputDialogConfiguration configuration, String initialText) {
      String value = (String) JOptionPane.showInputDialog(parent, configuration.getMessage(), configuration.getTitle(), JOptionPane.PLAIN_MESSAGE, null, null, initialText);
      return new Result(value == null, value);
   }

   private static class Result implements ITextInputDialogResult {
      private final boolean canceled;
      private final String text;
      Result(boolean canceled, String text) { this.canceled = canceled; this.text = text; }
      @Override public boolean isCanceled() { return canceled; }
      @Override public String getText() { return text; }
   }
}

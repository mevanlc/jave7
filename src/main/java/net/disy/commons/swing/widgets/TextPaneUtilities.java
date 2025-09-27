package net.disy.commons.swing.text;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

public class TextPaneUtilities {
   public static void setTextAndScrollToFirstLine(final JTextPane textPane, String text) {
      textPane.setText(text);
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            textPane.setCaretPosition(0);
         }
      });
   }
}

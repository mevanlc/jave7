package net.disy.commons.swing.text;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TextAreaUtilities {
   public static void setTextAndScrollToFirstLine(final JTextArea textArea, String text) {
      textArea.setText(text);
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            textArea.setCaretPosition(0);
         }
      });
   }
}

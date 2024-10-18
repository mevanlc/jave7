package de.jave.gui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@Deprecated
public class FilenameTextField extends JTextField {
   public FilenameTextField(String text, int columns) {
      super(text, columns);
      this.init();
      this.setText(text);
   }

   private void init() {
      this.setDocument(new PlainDocument() {
         @Override
         public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            StringBuffer insert = new StringBuffer();

            for (int i = 0; i < str.length(); i++) {
               char ch = str.charAt(i);
               if (FilenameTextField.isValidFileNameCharacter(ch)) {
                  insert.append(ch);
               } else {
                  FilenameTextField.this.getToolkit().beep();
               }
            }

            super.insertString(offs, insert.toString(), a);
         }
      });
   }

   public static final boolean isValidFileNameCharacter(char ch) {
      return "\t/\\:*?\"<>| ;+".indexOf(ch) == -1;
   }

   public static boolean isValidFileName(String name) {
      for (int i = 0; i < name.length(); i++) {
         char c = name.charAt(i);
         if (!isValidFileNameCharacter(c)) {
            return false;
         }
      }

      return true;
   }
}

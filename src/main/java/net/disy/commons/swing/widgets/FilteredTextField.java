package net.disy.commons.swing.textfield;

import java.awt.Toolkit;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import net.disy.commons.core.string.StringFilter;

public class FilteredTextField extends JTextField {
   public FilteredTextField(StringFilter filter) {
      this.setFilter(filter);
   }

   public FilteredTextField(StringFilter filter, String text) {
      super(text);
      this.setFilter(filter);
   }

   public FilteredTextField(StringFilter filter, int cols) {
      super(cols);
      this.setFilter(filter);
   }

   public FilteredTextField(StringFilter filter, String text, int cols) {
      super(text, cols);
      this.setFilter(filter);
   }

   public FilteredTextField(StringFilter filter, FilteredTextField.FilteredDocument doc, String text, int cols) {
      super(doc, text, cols);
      this.setFilter(filter);
   }

   public void setFilter(StringFilter filter) {
      ((FilteredTextField.FilteredDocument)this.getDocument()).setFilter(filter);
   }

   @Override
   protected Document createDefaultModel() {
      return new FilteredTextField.FilteredDocument();
   }

   public static class FilteredDocument extends PlainDocument {
      private StringFilter filter = null;

      public void setFilter(StringFilter filter) {
         this.filter = filter;

         try {
            if (filter != null && !filter.acceptFilterText(this.getText(0, this.getLength()))) {
               Toolkit.getDefaultToolkit().beep();
               super.remove(0, this.getLength());
            }
         } catch (BadLocationException var3) {
            var3.printStackTrace();
         }
      }

      @Override
      public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
         if (this.filter != null && str != null) {
            try {
               String text = this.getText(0, this.getLength());
               String newText = text.substring(0, offs) + str + text.substring(offs);
               if (this.filter.acceptFilterText(newText)) {
                  super.insertString(offs, str, a);
               } else {
                  Toolkit.getDefaultToolkit().beep();
               }
            } catch (BadLocationException var6) {
               var6.printStackTrace();
            }
         } else {
            super.insertString(offs, str, a);
         }
      }

      @Override
      public void remove(int offs, int len) throws BadLocationException {
         if (this.filter != null && len != 0) {
            try {
               String text = this.getText(0, this.getLength());
               String newText = text.substring(0, offs) + text.substring(offs + len);
               if (this.filter.acceptFilterText(newText)) {
                  super.remove(offs, len);
               } else {
                  Toolkit.getDefaultToolkit().beep();
               }
            } catch (BadLocationException var5) {
               var5.printStackTrace();
            }
         } else {
            super.remove(offs, len);
         }
      }
   }
}

package net.disy.commons.swing.textfield;

import net.disy.commons.core.string.StringFilter;

@Deprecated
public class IntegerField extends FilteredTextField {
   public IntegerField() {
      this(0);
   }

   public IntegerField(int columnCount) {
      super(new IntegerField.IntegerFilter(), columnCount);
      this.setHorizontalAlignment(4);
   }

   public IntegerField(int value, int columnCount) {
      super(new IntegerField.IntegerFilter(), String.valueOf(value), columnCount);
      this.setHorizontalAlignment(4);
   }

   public int getInt() {
      String text = this.getText();
      return isEmpty(text) ? 0 : Integer.parseInt(text);
   }

   private static boolean isEmpty(String text) {
      return text.isEmpty() || text.equals("-");
   }

   public void setInt(int value) {
      this.setText(String.valueOf(value));
   }

   public static class IntegerFilter implements StringFilter {
      @Override
      public boolean acceptFilterText(String text) {
         try {
            if (!IntegerField.isEmpty(text)) {
               Integer.valueOf(text);
            }

            return true;
         } catch (NumberFormatException var3) {
            return false;
         }
      }
   }
}

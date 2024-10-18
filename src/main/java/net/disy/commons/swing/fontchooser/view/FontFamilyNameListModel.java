package net.disy.commons.swing.fontchooser.view;

import javax.swing.AbstractListModel;

public class FontFamilyNameListModel extends AbstractListModel {
   private String[] fontFamilyNames = new String[0];

   @Override
   public int getSize() {
      return this.fontFamilyNames.length;
   }

   @Override
   public Object getElementAt(int index) {
      return this.fontFamilyNames[index];
   }

   public void setFontNames(String[] fontFamilyNames) {
      this.fontFamilyNames = fontFamilyNames;
      this.fireContentsChanged(this, 0, this.getSize());
   }

   public int indexOf(String fontFamilyName) {
      for (int i = 0; i < this.fontFamilyNames.length; i++) {
         if (this.fontFamilyNames[i].equals(fontFamilyName)) {
            return i;
         }
      }

      return -1;
   }
}

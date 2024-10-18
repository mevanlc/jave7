package net.disy.commons.swing.fontchooser.view;

import net.disy.commons.core.text.font.FontStyle;
import net.disy.commons.core.text.font.IFontStyleVisitor;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;

public class FontStyleUI {
   public String getName(FontStyle style) {
      FontStyleUI.FontStyleNameVisitor visitor = new FontStyleUI.FontStyleNameVisitor();
      style.accept(visitor);
      return visitor.getName();
   }

   private final class FontStyleNameVisitor implements IFontStyleVisitor {
      private String name;

      private FontStyleNameVisitor() {
      }

      @Override
      public void visitPlainFontStyle(FontStyle fontStyle) {
         this.name = DisyCommonsSwingFontChooserMessages.getString("FontStyleUI.plain");
      }

      @Override
      public void visitBoldFontStyle(FontStyle fontStyle) {
         this.name = DisyCommonsSwingFontChooserMessages.getString("FontStyleUI.bold");
      }

      @Override
      public void visitItalicFontStyle(FontStyle fontStyle) {
         this.name = DisyCommonsSwingFontChooserMessages.getString("FontStyleUI.italic");
      }

      @Override
      public void visitBoldItalicFontStyle(FontStyle fontStyle) {
         this.name = DisyCommonsSwingFontChooserMessages.getString("FontStyleUI.boldItalic");
      }

      public String getName() {
         return this.name;
      }
   }
}

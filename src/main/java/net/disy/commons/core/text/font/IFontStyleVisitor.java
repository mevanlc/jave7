package net.disy.commons.core.text.font;

public interface IFontStyleVisitor {
   void visitPlainFontStyle(FontStyle var1);

   void visitBoldFontStyle(FontStyle var1);

   void visitItalicFontStyle(FontStyle var1);

   void visitBoldItalicFontStyle(FontStyle var1);
}

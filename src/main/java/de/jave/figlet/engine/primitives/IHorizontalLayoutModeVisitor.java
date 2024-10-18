package de.jave.figlet.engine.primitives;

public interface IHorizontalLayoutModeVisitor {
   void visitSmushing(HorizontalLayoutMode var1);

   void visitKerning(HorizontalLayoutMode var1);

   void visitFullWidth(HorizontalLayoutMode var1);

   void visitSpacedKerning(HorizontalLayoutMode var1);

   void visitSupersmushing(HorizontalLayoutMode var1);

   void visitReverseSupersmushing(HorizontalLayoutMode var1);

   void visitFixedWidth(HorizontalLayoutMode var1);
}

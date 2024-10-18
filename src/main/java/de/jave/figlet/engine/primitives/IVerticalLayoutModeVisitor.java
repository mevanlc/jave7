package de.jave.figlet.engine.primitives;

public interface IVerticalLayoutModeVisitor {
   void visitSmushing(VerticalLayoutMode var1);

   void visitVerticalFitting(VerticalLayoutMode var1);

   void visitFullHeight(VerticalLayoutMode var1);

   void visitSpacedVerticalFitting(VerticalLayoutMode var1);

   void visitSupersmushing(VerticalLayoutMode var1);

   void visitReverseSupersmushing(VerticalLayoutMode var1);
}

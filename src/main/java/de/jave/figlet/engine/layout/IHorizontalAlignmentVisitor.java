package de.jave.figlet.engine.layout;

public interface IHorizontalAlignmentVisitor {
   void visitLeftAlignment(HorizontalAlignment var1);

   void visitRightAlignment(HorizontalAlignment var1);

   void visitCenterAlignment(HorizontalAlignment var1);
}

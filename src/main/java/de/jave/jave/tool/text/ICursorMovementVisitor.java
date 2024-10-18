package de.jave.jave.tool.text;

public interface ICursorMovementVisitor {
   void visitNormal(CursorMovement var1);

   void visitDirected(CursorMovement var1);

   void visitTrackFollowing(CursorMovement var1);

   void visitNone(CursorMovement var1);
}

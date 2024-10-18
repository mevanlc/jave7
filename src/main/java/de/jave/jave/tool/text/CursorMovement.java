package de.jave.jave.tool.text;

public enum CursorMovement {
   NORMAL {
      @Override
      public void accept(ICursorMovementVisitor visitor) {
         visitor.visitNormal(this);
      }
   },
   DIRECTED {
      @Override
      public void accept(ICursorMovementVisitor visitor) {
         visitor.visitDirected(this);
      }
   },
   TRACK_FOLLOWING {
      @Override
      public void accept(ICursorMovementVisitor visitor) {
         visitor.visitTrackFollowing(this);
      }
   },
   NONE {
      @Override
      public void accept(ICursorMovementVisitor visitor) {
         visitor.visitNone(this);
      }
   };

   private CursorMovement() {
   }

   public abstract void accept(ICursorMovementVisitor var1);
}

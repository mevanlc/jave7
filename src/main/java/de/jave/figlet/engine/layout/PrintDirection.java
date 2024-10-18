package de.jave.figlet.engine.layout;

public enum PrintDirection {
   RIGHT_TO_LEFT {
      @Override
      public void accept(IPrintDirectionVisitor visitor) {
         visitor.visitLeftDirection(this);
      }
   },
   LEFT_TO_RIGHT {
      @Override
      public void accept(IPrintDirectionVisitor visitor) {
         visitor.visitRightDirection(this);
      }
   };

   private PrintDirection() {
   }

   public abstract void accept(IPrintDirectionVisitor var1);
}

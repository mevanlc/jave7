package de.jave.figlet.engine.layout;

public enum HorizontalAlignment {
   LEFT {
      @Override
      public void accept(IHorizontalAlignmentVisitor visitor) {
         visitor.visitLeftAlignment(this);
      }
   },
   CENTER {
      @Override
      public void accept(IHorizontalAlignmentVisitor visitor) {
         visitor.visitCenterAlignment(this);
      }
   },
   RIGHT {
      @Override
      public void accept(IHorizontalAlignmentVisitor visitor) {
         visitor.visitRightAlignment(this);
      }
   };

   private HorizontalAlignment() {
   }

   public abstract void accept(IHorizontalAlignmentVisitor var1);
}

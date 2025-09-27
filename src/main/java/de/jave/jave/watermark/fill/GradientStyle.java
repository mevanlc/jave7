package de.jave.jave.algorithm.fill;

public enum GradientStyle {
   LINEAR {
      @Override
      public void accept(IGradientStyleVisitor visitor) {
         visitor.visitLinear(this);
      }
   },
   SUNBURST {
      @Override
      public void accept(IGradientStyleVisitor visitor) {
         visitor.visitSunburst(this);
      }
   },
   RADIAL {
      @Override
      public void accept(IGradientStyleVisitor visitor) {
         visitor.visitRadial(this);
      }
   };

   private GradientStyle() {
   }

   public abstract void accept(IGradientStyleVisitor var1);
}

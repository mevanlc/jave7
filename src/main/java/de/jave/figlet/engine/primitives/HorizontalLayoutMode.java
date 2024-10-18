package de.jave.figlet.engine.primitives;

public enum HorizontalLayoutMode {
   FIXED_WIDTH {
      @Override
      public void accept(IHorizontalLayoutModeVisitor visitor) {
         visitor.visitFixedWidth(this);
      }
   },
   FULL_WIDTH {
      @Override
      public void accept(IHorizontalLayoutModeVisitor visitor) {
         visitor.visitFullWidth(this);
      }
   },
   SPACED_KERNING {
      @Override
      public void accept(IHorizontalLayoutModeVisitor visitor) {
         visitor.visitSpacedKerning(this);
      }
   },
   KERNING {
      @Override
      public void accept(IHorizontalLayoutModeVisitor visitor) {
         visitor.visitKerning(this);
      }
   },
   SMUSHING {
      @Override
      public void accept(IHorizontalLayoutModeVisitor visitor) {
         visitor.visitSmushing(this);
      }
   },
   SUPERSMUSHING {
      @Override
      public void accept(IHorizontalLayoutModeVisitor visitor) {
         visitor.visitSupersmushing(this);
      }
   },
   REVERSE_SUPERSMUSHING {
      @Override
      public void accept(IHorizontalLayoutModeVisitor visitor) {
         visitor.visitReverseSupersmushing(this);
      }
   };

   private HorizontalLayoutMode() {
   }

   public abstract void accept(IHorizontalLayoutModeVisitor var1);

   public HorizontalLayoutMode intersect(HorizontalLayoutMode other) {
      if (other == this) {
         return this;
      } else if (this == FULL_WIDTH || other == FULL_WIDTH) {
         return FULL_WIDTH;
      } else {
         return this != KERNING && other != KERNING ? SMUSHING : KERNING;
      }
   }
}

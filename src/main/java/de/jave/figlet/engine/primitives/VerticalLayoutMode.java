package de.jave.figlet.engine.primitives;

public enum VerticalLayoutMode {
   FULL_HEIGHT {
      @Override
      public void accept(IVerticalLayoutModeVisitor visitor) {
         visitor.visitFullHeight(this);
      }
   },
   SPACED_VERTICAL_FITTING {
      @Override
      public void accept(IVerticalLayoutModeVisitor visitor) {
         visitor.visitSpacedVerticalFitting(this);
      }
   },
   VERTICAL_FITTING {
      @Override
      public void accept(IVerticalLayoutModeVisitor visitor) {
         visitor.visitVerticalFitting(this);
      }
   },
   SMUSHING {
      @Override
      public void accept(IVerticalLayoutModeVisitor visitor) {
         visitor.visitSmushing(this);
      }
   },
   SUPERSMUSHING {
      @Override
      public void accept(IVerticalLayoutModeVisitor visitor) {
         visitor.visitSupersmushing(this);
      }
   },
   REVERSE_SUPERSMUSHING {
      @Override
      public void accept(IVerticalLayoutModeVisitor visitor) {
         visitor.visitReverseSupersmushing(this);
      }
   };

   private VerticalLayoutMode() {
   }

   public abstract void accept(IVerticalLayoutModeVisitor var1);

   public VerticalLayoutMode intersect(VerticalLayoutMode other) {
      if (other == this) {
         return this;
      } else if (this == FULL_HEIGHT || other == FULL_HEIGHT) {
         return FULL_HEIGHT;
      } else {
         return this != VERTICAL_FITTING && other != VERTICAL_FITTING ? SMUSHING : VERTICAL_FITTING;
      }
   }
}

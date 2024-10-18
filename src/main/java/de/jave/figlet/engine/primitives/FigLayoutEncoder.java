package de.jave.figlet.engine.primitives;

import de.jave.figlet.engine.layout.PrintDirection;

public class FigLayoutEncoder {
   private static final int VERTICAL_FITTING_FLAG = 8192;
   private static final int VERTICAL_SMUSHING_FLAG = 16384;
   private static final int HORIZONTAL_FITTING_FLAG = 64;
   private static final int HORIZONTAL_SMUSHING_FLAG = 128;

   public int getEncodedLayoutValue(FigLayout layout) {
      return this.getHorizontalLayoutModeBitMask(layout.getHorizontalLayoutMode())
         | layout.getHorizontalSmushingRules().getBitMask()
         | this.getVerticalLayoutModeBitMask(layout.getVerticalLayoutMode())
         | layout.getVerticalSmushingRules().getBitMask();
   }

   private int getVerticalLayoutModeBitMask(VerticalLayoutMode mode) {
      final FigLayoutEncoder.IntHolder intHolder = new FigLayoutEncoder.IntHolder();
      mode.accept(new IVerticalLayoutModeVisitor() {
         @Override
         public void visitSmushing(VerticalLayoutMode mode) {
            intHolder.value = 16384;
         }

         @Override
         public void visitVerticalFitting(VerticalLayoutMode mode) {
            intHolder.value = 8192;
         }

         @Override
         public void visitFullHeight(VerticalLayoutMode mode) {
            intHolder.value = 0;
         }

         @Override
         public void visitSpacedVerticalFitting(VerticalLayoutMode mode) {
            intHolder.value = 0;
         }

         @Override
         public void visitSupersmushing(VerticalLayoutMode mode) {
            intHolder.value = 16384;
         }

         @Override
         public void visitReverseSupersmushing(VerticalLayoutMode mode) {
            intHolder.value = 16384;
         }
      });
      return intHolder.value;
   }

   private int getHorizontalLayoutModeBitMask(HorizontalLayoutMode mode) {
      final FigLayoutEncoder.IntHolder intHolder = new FigLayoutEncoder.IntHolder();
      mode.accept(new IHorizontalLayoutModeVisitor() {
         @Override
         public void visitSmushing(HorizontalLayoutMode mode) {
            intHolder.value = 128;
         }

         @Override
         public void visitKerning(HorizontalLayoutMode mode) {
            intHolder.value = 64;
         }

         @Override
         public void visitFullWidth(HorizontalLayoutMode mode) {
            intHolder.value = 0;
         }

         @Override
         public void visitSpacedKerning(HorizontalLayoutMode mode) {
            intHolder.value = 64;
         }

         @Override
         public void visitSupersmushing(HorizontalLayoutMode mode) {
            intHolder.value = 128;
         }

         @Override
         public void visitReverseSupersmushing(HorizontalLayoutMode mode) {
            intHolder.value = 128;
         }

         @Override
         public void visitFixedWidth(HorizontalLayoutMode mode) {
            intHolder.value = 0;
         }
      });
      return intHolder.value;
   }

   public VerticalLayoutMode getVerticalLayoutMode(int layout) {
      if (isSet(layout, 16384)) {
         return VerticalLayoutMode.SMUSHING;
      } else {
         return isSet(layout, 8192) ? VerticalLayoutMode.VERTICAL_FITTING : VerticalLayoutMode.FULL_HEIGHT;
      }
   }

   private static boolean isSet(int layout, int flag) {
      return (layout & flag) == flag;
   }

   public HorizontalLayoutMode getHorizontalLayoutMode(int layout) {
      if (isSet(layout, 128)) {
         return HorizontalLayoutMode.SMUSHING;
      } else {
         return isSet(layout, 64) ? HorizontalLayoutMode.KERNING : HorizontalLayoutMode.FULL_WIDTH;
      }
   }

   private FigLayout getLayout(int layout) {
      HorizontalLayoutMode horizontalMode = this.getHorizontalLayoutMode(layout);
      VerticalLayoutMode verticalMode = this.getVerticalLayoutMode(layout);
      HorizontalSmushingRules horizontalRules = new HorizontalSmushingRules(layout);
      VerticalSmushingRules verticalRules = new VerticalSmushingRules(layout);
      return new FigLayout(horizontalMode, horizontalRules, verticalMode, verticalRules);
   }

   public FigLayout createFromOldLayoutValue(int oldLayout) {
      return this.createFromCombinedLayoutValues(oldLayout, -1);
   }

   public FigLayout createFromNewLayoutValue(int newLayout) {
      return this.getLayout(newLayout);
   }

   public FigLayout createFromCombinedLayoutValues(int oldLayout, int newLayout) {
      int layout = 0;
      if (newLayout == -1) {
         if (oldLayout == -1) {
            layout = 0;
         } else if (oldLayout == 0) {
            layout = 64;
         } else {
            layout = oldLayout | 128;
         }
      } else {
         layout = newLayout;
      }

      return this.getLayout(layout);
   }

   public PrintDirection getPrintDirection(int i) {
      if (i == 0) {
         return PrintDirection.LEFT_TO_RIGHT;
      } else {
         return i == 1 ? PrintDirection.RIGHT_TO_LEFT : null;
      }
   }

   private final class IntHolder {
      public int value;

      private IntHolder() {
      }
   }
}

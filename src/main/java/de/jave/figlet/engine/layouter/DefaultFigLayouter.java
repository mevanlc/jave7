package de.jave.figlet.engine.layouter;

import de.jave.figlet.engine.layout.HorizontalAlignment;
import de.jave.figlet.engine.layout.IHorizontalAlignmentVisitor;
import de.jave.figlet.engine.layout.PrintDirection;
import de.jave.figlet.engine.layout.VerticalAlignment;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.primitives.FigFragment;
import de.jave.figlet.engine.primitives.FigLayout;
import de.jave.figlet.engine.primitives.HorizontalLayoutMode;
import de.jave.text.QuickString;

public class DefaultFigLayouter implements IFigLayouter {
   private int maxLineLength = -1;

   public DefaultFigLayouter(int maxLineLength) {
      this.maxLineLength = maxLineLength;
   }

   private static void ensureMinimumWidth(final FigFragment fragment, final int newWidth) {
      if (newWidth > fragment.getWidth()) {
         fragment.getHorizontalAlignment().accept(new IHorizontalAlignmentVisitor() {
            @Override
            public void visitLeftAlignment(HorizontalAlignment horizontalAlignment) {
               fragment.addColumnsRight(newWidth - fragment.getWidth());
            }

            @Override
            public void visitRightAlignment(HorizontalAlignment horizontalAlignment) {
               fragment.addColumnsLeft(newWidth - fragment.getWidth());
            }

            @Override
            public void visitCenterAlignment(HorizontalAlignment horizontalAlignment) {
               fragment.addColumnsLeft((newWidth - fragment.getWidth()) / 2);
               fragment.addColumnsRight(newWidth - fragment.getWidth());
            }
         });
      }
   }

   private static void ensureMinimumHeight(FigFragment fragment, int newHeight) {
      if (newHeight > fragment.getHeight()) {
         if (fragment.getVerticalAlignment() == VerticalAlignment.TOP) {
            fragment.addRowsBottom(newHeight - fragment.getHeight());
         } else if (fragment.getVerticalAlignment() == VerticalAlignment.BOTTOM) {
            fragment.addRowsTop(newHeight - fragment.getHeight());
         } else {
            fragment.addRowsTop((newHeight - fragment.getHeight()) / 2);
            fragment.addRowsBottom(newHeight - fragment.getHeight());
         }
      }
   }

   private static void ensureUnderLength(FigFragment fragment, int newUnderLength) {
      if (fragment.getUnderLength() < newUnderLength) {
         fragment.addRowsBottom(newUnderLength - fragment.getUnderLength());
      }
   }

   @Override
   public FigFragment createFragment(FigFont font, FigLayout layout, int characterCode) {
      FigFragment fragment = new FigFragment(font.getFIGCharacter(characterCode), layout, font.getUnderLength(), this.maxLineLength);
      if (layout.getHorizontalLayoutMode() == HorizontalLayoutMode.FIXED_WIDTH) {
         ensureMinimumWidth(fragment, font.getOptions().getActualMaxLineWidth());
      }

      return fragment;
   }

   @Override
   public FigFragment appendHorizontal(FigFragment fragment1, FigFragment fragment2) {
      return this.addHorizontal(fragment1, fragment2);
   }

   @Override
   public FigFragment appendVertical(FigFragment fragment, FigFragment lineFragment) {
      return addVertical(fragment, lineFragment);
   }

   private static FigFragment addVertical(FigFragment a, FigFragment b) {
      if (b == null) {
         return a;
      } else if (a.isEmpty()) {
         return b;
      } else if (b.isEmpty()) {
         return a;
      } else {
         int shallWidth = max(b.getWidth(), a.getWidth());
         ensureMinimumWidth(a, shallWidth);
         ensureMinimumWidth(b, shallWidth);
         int width = a.getWidth();
         FigLayout[] intersectedLayouts = new FigLayout[width];

         for (int i = 0; i < width; i++) {
            intersectedLayouts[i] = a.getBottomLayout(i).intersect(b.getTopLayout(i));
         }

         int maxi = 10;
         if (maxi > a.getHeight()) {
            maxi = a.getHeight();
         }

         if (maxi > b.getHeight()) {
            maxi = b.getHeight();
         }

         QuickString[] aa = new QuickString[a.getWidth()];

         for (int i = 0; i < a.getWidth(); i++) {
            aa[i] = new QuickString(maxi * 2);

            for (int j = 0; j < maxi; j++) {
               aa[i].append(a.getLines()[a.getHeight() - maxi + j].charAt(i));
            }
         }

         QuickString[] bb = new QuickString[b.getWidth()];

         for (int i = 0; i < b.getWidth(); i++) {
            bb[i] = new QuickString(maxi * 2);

            for (int j = 0; j < maxi; j++) {
               bb[i].append(b.getLines()[j].charAt(i));
            }
         }

         int min = Integer.MAX_VALUE;

         for (int i = 0; i < bb.length; i++) {
            int depth = intersectedLayouts[i].getMaximalVerticalSmushingDepth(aa[i], bb[i]);
            if (depth < min) {
               min = depth;
            }

            if (min == 0) {
               break;
            }
         }

         QuickString[] ss = new QuickString[bb.length];

         for (int i = 0; i < bb.length; i++) {
            ss[i] = intersectedLayouts[i].smushVertical(aa[i], bb[i], min);
         }

         QuickString[] s = new QuickString[a.getHeight() + b.getHeight() - min];
         System.arraycopy(a.getLines(), 0, s, 0, a.getHeight() - maxi);

         for (int i = 0; i < maxi + maxi - min; i++) {
            s[i + a.getHeight() - maxi] = new QuickString();

            for (int j = 0; j < ss.length; j++) {
               s[i + a.getHeight() - maxi].append(ss[j].charAt(i));
            }
         }

         System.arraycopy(b.getLines(), maxi, s, s.length - b.getHeight() + maxi, b.getHeight() - maxi);
         int top = a.getHeight() - min / 2;
         if (top < 0) {
            top = 0;
         }

         int bottom = s.length - top;
         FigLayout[] newLeftLayout = new FigLayout[s.length];
         FigLayout[] newRightLayout = new FigLayout[s.length];
         System.arraycopy(a.getLayoutLeft(), 0, newLeftLayout, 0, top);
         System.arraycopy(a.getLayoutRight(), 0, newRightLayout, 0, top);
         System.arraycopy(b.getLayoutLeft(), b.getHeight() - bottom, newLeftLayout, top, bottom);
         System.arraycopy(b.getLayoutRight(), b.getHeight() - bottom, newRightLayout, top, bottom);
         FigFragment result = new FigFragment(
            s,
            newLeftLayout,
            newRightLayout,
            s.length,
            s[0].length(),
            a.getLayoutTop(),
            b.getLayoutBottom(),
            a.getUnderLength(),
            b.getMaxLineLength(),
            a.getPrintDirection()
         );
         result.setHorizontalAlignment(b.getHorizontalAlignment());
         return result;
      }
   }

   private static int max(int a, int b) {
      return a > b ? a : b;
   }

   private FigFragment addHorizontal(FigFragment one, FigFragment other) {
      if (other == null) {
         return one;
      } else {
         FigFragment a = (FigFragment)one.clone();
         FigFragment b = (FigFragment)other.clone();
         if (a.getHeight() == 0 || a.getWidth() == 0) {
            return b;
         } else if (b.getHeight() != 0 && b.getWidth() != 0) {
            if (a.getPrintDirection() == PrintDirection.RIGHT_TO_LEFT && b.getPrintDirection() == PrintDirection.RIGHT_TO_LEFT) {
               FigFragment t = a;
               a = b;
               b = t;
            }

            if (a.getWidth() == 1 && a.getLines()[0].firstChar() == ' ') {
               a.getLines()[0] = new QuickString('\u007f');
            }

            if (b.getWidth() == 1 && b.getLines()[0].firstChar() == ' ') {
               b.getLines()[0] = new QuickString('\u007f');
            }

            if (b.getVerticalAlignment() == VerticalAlignment.BOTTOM) {
               ensureUnderLength(a, b.getUnderLength());
               ensureUnderLength(b, a.getUnderLength());
            }

            ensureMinimumHeight(a, b.getHeight());
            ensureMinimumHeight(b, a.getHeight());
            int h = a.getHeight();
            FigLayout[] layoutInter = new FigLayout[h];

            for (int i = 0; i < h; i++) {
               layoutInter[i] = a.getLayoutRight()[i].intersect(b.getLayoutLeft()[i]);
            }

            int smushingWidth = Integer.MAX_VALUE;

            for (int i = 0; i < h; i++) {
               int depth = layoutInter[i].getMaximalHorizontalSmushingDepth(a.getLines()[i], b.getLines()[i]);
               if (depth < smushingWidth) {
                  smushingWidth = depth;
               }
            }

            QuickString[] s = new QuickString[h];

            for (int ix = 0; ix < h; ix++) {
               s[ix] = layoutInter[ix].smushHorizontal(a.getLines()[ix], b.getLines()[ix], smushingWidth);
            }

            FigFragment result = new FigFragment(
               s,
               a.getLayoutLeft(),
               b.getLayoutRight(),
               s.length,
               s[0].length(),
               new FigLayout[s[0].length()],
               new FigLayout[s[0].length()],
               b.getUnderLength(),
               b.getMaxLineLength(),
               a.getPrintDirection()
            );
            int resultCutX = a.getWidth() - smushingWidth / 2;
            if (resultCutX < 0) {
               resultCutX = 0;
            }

            if (resultCutX >= result.getWidth()) {
               resultCutX = result.getWidth() - 1;
            }

            for (int ix = 0; ix < resultCutX; ix++) {
               int index = Math.min(ix, a.getWidth() - 1);
               result.setTopLayout(ix, a.getTopLayout(index));
               result.setBottomLayout(ix, a.getBottomLayout(index));
            }

            for (int ix = resultCutX; ix < result.getWidth(); ix++) {
               int index = Math.max(0, b.getWidth() - result.getWidth() + resultCutX);
               result.setTopLayout(ix, b.getTopLayout(index));
               result.setBottomLayout(ix, b.getBottomLayout(index));
            }

            return result;
         } else {
            return a;
         }
      }
   }
}

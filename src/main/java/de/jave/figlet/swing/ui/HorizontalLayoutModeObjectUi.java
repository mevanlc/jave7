package de.jave.figlet.swing.ui;

import de.jave.figlet.engine.primitives.HorizontalLayoutMode;
import de.jave.figlet.engine.primitives.IHorizontalLayoutModeVisitor;
import javax.swing.Icon;
import net.disy.commons.swing.ui.AbstractObjectUi;

public class HorizontalLayoutModeObjectUi extends AbstractObjectUi<HorizontalLayoutMode> {
   public Icon getIcon(HorizontalLayoutMode mode) {
      final IconHolder iconHolder = new IconHolder();
      mode.accept(new IHorizontalLayoutModeVisitor() {
         @Override
         public void visitSmushing(HorizontalLayoutMode mode) {
            iconHolder.value = FigletIcons.H_SMUSHING;
         }

         @Override
         public void visitKerning(HorizontalLayoutMode mode) {
            iconHolder.value = FigletIcons.H_KERNING;
         }

         @Override
         public void visitFullWidth(HorizontalLayoutMode mode) {
            iconHolder.value = FigletIcons.H_FULL_WIDTH;
         }

         @Override
         public void visitSpacedKerning(HorizontalLayoutMode mode) {
            iconHolder.value = FigletIcons.H_SPACED_KERNING;
         }

         @Override
         public void visitSupersmushing(HorizontalLayoutMode mode) {
            iconHolder.value = FigletIcons.H_SUPERSMUSHING;
         }

         @Override
         public void visitReverseSupersmushing(HorizontalLayoutMode mode) {
            iconHolder.value = FigletIcons.H_REVERSE_SUPERSMUSHING;
         }

         @Override
         public void visitFixedWidth(HorizontalLayoutMode mode) {
            iconHolder.value = FigletIcons.H_FIXED_WIDTH;
         }
      });
      return iconHolder.value;
   }

   public String getLabel(HorizontalLayoutMode mode) {
      final StringHolder stringHolder = new StringHolder();
      mode.accept(new IHorizontalLayoutModeVisitor() {
         @Override
         public void visitSmushing(HorizontalLayoutMode mode) {
            stringHolder.value = "Smushing";
         }

         @Override
         public void visitKerning(HorizontalLayoutMode mode) {
            stringHolder.value = "Kerning";
         }

         @Override
         public void visitFullWidth(HorizontalLayoutMode mode) {
            stringHolder.value = "Full Width";
         }

         @Override
         public void visitSpacedKerning(HorizontalLayoutMode mode) {
            stringHolder.value = "Spaced Kerning";
         }

         @Override
         public void visitSupersmushing(HorizontalLayoutMode mode) {
            stringHolder.value = "Supersmushing";
         }

         @Override
         public void visitReverseSupersmushing(HorizontalLayoutMode mode) {
            stringHolder.value = "Reverse Supersmushing";
         }

         @Override
         public void visitFixedWidth(HorizontalLayoutMode mode) {
            stringHolder.value = "Fixed Width";
         }
      });
      return stringHolder.value;
   }

   public String getToolTipText(HorizontalLayoutMode mode) {
      final StringHolder stringHolder = new StringHolder();
      mode.accept(new IHorizontalLayoutModeVisitor() {
         @Override
         public void visitSmushing(HorizontalLayoutMode mode) {
            stringHolder.value = "Move FIGcharacters one step closer after they touch, so that they partially occupy the same space";
         }

         @Override
         public void visitKerning(HorizontalLayoutMode mode) {
            stringHolder.value = "Move FIGcharacters closer together until they touch";
         }

         @Override
         public void visitFullWidth(HorizontalLayoutMode mode) {
            stringHolder.value = "Represent each FIGcharacter occupying the full width of its arrangement of sub-characters as designed";
         }

         @Override
         public void visitSpacedKerning(HorizontalLayoutMode mode) {
            stringHolder.value = "Move FIGcharacters closer together until the sub-characters are separated by at least one space";
         }

         @Override
         public void visitSupersmushing(HorizontalLayoutMode mode) {
            stringHolder.value = "Move FIGcharacters closer together until the later character overlaps the previous one for a certain number of characters";
         }

         @Override
         public void visitReverseSupersmushing(HorizontalLayoutMode mode) {
            stringHolder.value = "Move FIGcharacters closer together until the previous character overlaps the later one for a certain number of characters";
         }

         @Override
         public void visitFixedWidth(HorizontalLayoutMode mode) {
            stringHolder.value = "Represent each FIGcharacter occupying the maximum width of all characters in the font";
         }
      });
      return stringHolder.value;
   }
}

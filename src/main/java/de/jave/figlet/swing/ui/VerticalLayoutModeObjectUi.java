package de.jave.figlet.swing.ui;

import de.jave.figlet.engine.primitives.IVerticalLayoutModeVisitor;
import de.jave.figlet.engine.primitives.VerticalLayoutMode;
import javax.swing.Icon;
import net.disy.commons.swing.ui.AbstractObjectUi;

public class VerticalLayoutModeObjectUi extends AbstractObjectUi<VerticalLayoutMode> {
   public Icon getIcon(VerticalLayoutMode mode) {
      final IconHolder iconHolder = new IconHolder();
      mode.accept(new IVerticalLayoutModeVisitor() {
         @Override
         public void visitSmushing(VerticalLayoutMode mode) {
            iconHolder.value = FigletIcons.V_SMUSHING;
         }

         @Override
         public void visitVerticalFitting(VerticalLayoutMode mode) {
            iconHolder.value = FigletIcons.V_FITTING;
         }

         @Override
         public void visitFullHeight(VerticalLayoutMode mode) {
            iconHolder.value = FigletIcons.V_FULL_HEIGHT;
         }

         @Override
         public void visitSpacedVerticalFitting(VerticalLayoutMode mode) {
            iconHolder.value = FigletIcons.V_SPACED_FITTING;
         }

         @Override
         public void visitSupersmushing(VerticalLayoutMode mode) {
            iconHolder.value = FigletIcons.V_SUPERSMUSHING;
         }

         @Override
         public void visitReverseSupersmushing(VerticalLayoutMode mode) {
            iconHolder.value = FigletIcons.V_REVERSE_SUPERSMUSHING;
         }
      });
      return iconHolder.value;
   }

   public String getLabel(VerticalLayoutMode mode) {
      final StringHolder stringHolder = new StringHolder();
      mode.accept(new IVerticalLayoutModeVisitor() {
         @Override
         public void visitSmushing(VerticalLayoutMode mode) {
            stringHolder.value = "Smushing";
         }

         @Override
         public void visitVerticalFitting(VerticalLayoutMode mode) {
            stringHolder.value = "Vertical Fitting";
         }

         @Override
         public void visitFullHeight(VerticalLayoutMode mode) {
            stringHolder.value = "Full Height";
         }

         @Override
         public void visitSpacedVerticalFitting(VerticalLayoutMode mode) {
            stringHolder.value = "Spaced Vertical Fitting";
         }

         @Override
         public void visitSupersmushing(VerticalLayoutMode mode) {
            stringHolder.value = "Supersmushing";
         }

         @Override
         public void visitReverseSupersmushing(VerticalLayoutMode mode) {
            stringHolder.value = "Reverse Supersmushing";
         }
      });
      return stringHolder.value;
   }

   public String getToolTipText(VerticalLayoutMode mode) {
      final StringHolder stringHolder = new StringHolder();
      mode.accept(new IVerticalLayoutModeVisitor() {
         @Override
         public void visitSmushing(VerticalLayoutMode mode) {
            stringHolder.value = "Move FIGcharacters one step closer after they touch, so that they partially occupy the same space";
         }

         @Override
         public void visitVerticalFitting(VerticalLayoutMode mode) {
            stringHolder.value = "Moves FIGcharacters closer together until they touch";
         }

         @Override
         public void visitFullHeight(VerticalLayoutMode mode) {
            stringHolder.value = "Represents each FIGcharacter occupying the full height of its arrangement of sub-characters as designed";
         }

         @Override
         public void visitSpacedVerticalFitting(VerticalLayoutMode mode) {
            stringHolder.value = "Move FIGcharacters closer together until the sub-characters are separated by at least one space";
         }

         @Override
         public void visitSupersmushing(VerticalLayoutMode mode) {
            stringHolder.value = "Move FIGcharacters closer together until the later line overlaps the previous one for a certain number of characters";
         }

         @Override
         public void visitReverseSupersmushing(VerticalLayoutMode mode) {
            stringHolder.value = "Move FIGcharacters closer together until the previous line overlaps the later one for a certain number of characters";
         }
      });
      return stringHolder.value;
   }
}

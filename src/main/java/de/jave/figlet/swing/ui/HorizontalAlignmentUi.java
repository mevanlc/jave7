package de.jave.figlet.swing.ui;

import de.jave.figlet.engine.layout.HorizontalAlignment;
import de.jave.figlet.engine.layout.IHorizontalAlignmentVisitor;
import javax.swing.Icon;

public class HorizontalAlignmentUi {
   public Icon getIcon(HorizontalAlignment alignment) {
      final IconHolder iconHolder = new IconHolder();
      alignment.accept(new IHorizontalAlignmentVisitor() {
         @Override
         public void visitLeftAlignment(HorizontalAlignment horizontalAlignment) {
            iconHolder.value = FigletIcons.H_ALIGN_LEFT;
         }

         @Override
         public void visitRightAlignment(HorizontalAlignment horizontalAlignment) {
            iconHolder.value = FigletIcons.H_ALIGN_RIGHT;
         }

         @Override
         public void visitCenterAlignment(HorizontalAlignment horizontalAlignment) {
            iconHolder.value = FigletIcons.H_ALIGN_CENTER;
         }
      });
      return iconHolder.value;
   }
}

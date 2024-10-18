package de.jave.figlet.swing.ui;

import de.jave.figlet.engine.layout.IPrintDirectionVisitor;
import de.jave.figlet.engine.layout.PrintDirection;
import javax.swing.Icon;

public class PrintDirectionUi {
   public Icon getIcon(PrintDirection direction) {
      final IconHolder iconHolder = new IconHolder();
      direction.accept(new IPrintDirectionVisitor() {
         @Override
         public void visitLeftDirection(PrintDirection direction) {
            iconHolder.value = FigletIcons.PRINT_DIRECTION_LEFT;
         }

         @Override
         public void visitRightDirection(PrintDirection direction) {
            iconHolder.value = FigletIcons.PRINT_DIRECTION_RIGHT;
         }
      });
      return iconHolder.value;
   }

   public String getTooltipText(PrintDirection direction) {
      final StringHolder stringHolder = new StringHolder();
      direction.accept(new IPrintDirectionVisitor() {
         @Override
         public void visitLeftDirection(PrintDirection direction) {
            stringHolder.value = "Printdirection right to left";
         }

         @Override
         public void visitRightDirection(PrintDirection direction) {
            stringHolder.value = "Printdirection left to right";
         }
      });
      return stringHolder.value;
   }
}

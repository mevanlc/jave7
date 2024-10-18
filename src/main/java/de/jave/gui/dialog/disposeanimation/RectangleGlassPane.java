package de.jave.gui.dialog.disposeanimation;

import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import net.disy.commons.swing.color.SwingColors;

public class RectangleGlassPane extends JComponent {
   private Rectangle rectangle;

   public void setRectangle(Rectangle rectangle) {
      this.rectangle = rectangle;
      this.revalidate();
      this.repaint();
   }

   @Override
   protected void paintComponent(Graphics graphics) {
      if (this.rectangle != null) {
         graphics.setColor(SwingColors.getControlDkShadowColor());
         graphics.drawRect(this.rectangle.x, this.rectangle.y, this.rectangle.width - 1, this.rectangle.height - 1);
      }
   }
}

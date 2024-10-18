package de.jave.jave;

import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.Icon;

public class PanTool extends Tool {
   private int panDX = 0;
   private int panDY = 0;
   private Point location1;

   public PanTool(JaveMainPanel plate, JavEApplication application, Filter filter) {
      super(plate, application, filter);
   }

   @Override
   public String getName() {
      return "Pan";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_PAN_ICON;
   }

   @Override
   public void takeToHand() {
      this.setCursor(Cursor.getPredefinedCursor(12));
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      this.location1 = location;
      this.panDX = 0;
      this.panDY = 0;
      this.showStatus("[0,0]");
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      this.panDX = 0;
      this.panDY = 0;
      this.showStatus("");
      this.getPlate().saveCurrentState("pan");
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      if (location != null && this.location1 != null) {
         int dx = location.x - this.location1.x;
         int dy = location.y - this.location1.y;
         StringBuffer s = new StringBuffer("[");
         if (dx > 0) {
            s.append('+');
         }

         s.append(dx);
         s.append(',');
         if (dy > 0) {
            s.append('+');
         }

         s.append(dy);
         s.append(']');
         this.showStatus(s.toString());
         this.getPlate().pan(dx - this.panDX, dy - this.panDY);
         this.panDX = dx;
         this.panDY = dy;
      } else {
         this.showStatus("");
      }
   }
}

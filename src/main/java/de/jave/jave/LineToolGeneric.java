package de.jave.jave;

import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.rendering.PixelPlateRenderer;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import net.dizzy.commons.swing.mousecursor.CursorId;
import net.dizzy.commons.swing.mousecursor.CursorProvider;

public class LineToolGeneric extends GenericTool {
   private Point clickPoint;
   private Point dragPoint;

   public LineToolGeneric(JaveMainPanel mainPanel, JavEApplication application, Filter filter) {
      super(mainPanel, application, filter);
   }

   @Override
   public String getName() {
      return "Line Generic";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_LINE_GENERIC_ICON;
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (location != null) {
         this.clickPoint = point;
         this.dragPoint = point;
         this.markPlate = this.createMarkPlate(location);
         this.pixelPlateModel.configure(this.markPlate);
         this.markPlate.setCharacter(this.getMouseChar());
         this.repaintCursor();
      }
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      this.dragPoint = point;
      this.repaintCursor();
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      if (this.clickPoint != null && this.dragPoint != null) {
         if (this.markPlate != null) {
            this.setMixMode(this.isMix());
            LocatedCharacterPlate result = this.createResult();
            result.pasteInto(this.getPlate().getContent());
            this.saveCurrentState("draw line");
            this.repaintAll();
            this.markPlate = null;
            this.clickPoint = null;
            this.dragPoint = null;
         }
      }
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      if (code == 27 && this.clickPoint != null) {
         this.clickPoint = null;
         this.dragPoint = null;
         this.markPlate = null;
         this.repaintCursor();
      }
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.clickPoint != null) {
         g.setColor(colorScheme.getColorToolHelping());
         g.drawRect(this.clickPoint.x - 2, this.clickPoint.y - 2, 5, 5);
         if (this.dragPoint != null) {
            g.drawRect(this.dragPoint.x - 2, this.dragPoint.y - 2, 5, 5);
            g.drawLine(this.clickPoint.x, this.clickPoint.y, this.dragPoint.x, this.dragPoint.y);
            LocatedCharacterPlate result = this.createResult();
            PixelPlateRenderer.paint(g, this.getPlate(), colorScheme, result, plateOrigin);
         }
      }
   }

   private LocatedCharacterPlate createResult() {
      this.markPlate.clear();
      Point2d p1 = this.getPlate().getRealLocationForScreenPoint(this.clickPoint);
      Point2d p2 = this.getPlate().getRealLocationForScreenPoint(this.dragPoint);
      this.markPlate.drawLine(p1, p2);
      return this.markPlate.convert();
   }

   @Override
   public void takeToHand() {
      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
      this.clickPoint = null;
      this.dragPoint = null;
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
      this.clickPoint = null;
      this.dragPoint = null;
   }
}

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
import net.disy.commons.swing.mousecursor.CursorId;
import net.disy.commons.swing.mousecursor.CursorProvider;

public class FreehandToolGeneric extends GenericTool {
   private Point point1;
   private Point cursorPosition;

   public FreehandToolGeneric(JaveMainPanel mainPanel, JavEApplication application, Filter filter) {
      super(mainPanel, application, filter);
   }

   @Override
   public String getName() {
      return "Freehand Generic";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_FREEHAND_GENERIC_ICON;
   }

   @Override
   public void takeToHand() {
      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (location != null) {
         this.point1 = point;
         this.markPlate = this.createMarkPlate(location);
         pixelPlateOptionsPanel.configure(this.markPlate);
         this.markPlate.setCharacter(this.getMouseChar());
         Point2d p1 = this.getPlate().getRealLocationForScreenPoint(this.point1);
         this.markPlate.drawLine(p1, p1);
         this.repaintCursor();
      }
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      if (this.markPlate != null) {
         this.setMixMode(this.isMix());
         LocatedCharacterPlate result = this.markPlate.convert();
         result.pasteInto(this.getPlate().getContent());
         this.point1 = null;
         this.cursorPosition = null;
         this.markPlate = null;
         this.saveCurrentState("freehand");
         this.repaintAll();
      }
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      if (code == 27 && this.point1 != null) {
         this.point1 = null;
         this.markPlate = null;
         this.repaintCursor();
      }
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      if (this.markPlate != null && location != null) {
         Point2d p1 = this.getPlate().getRealLocationForScreenPoint(point);
         Point2d p2 = this.getPlate().getRealLocationForScreenPoint(this.point1);
         if (p1.getX() != p2.getX() || p1.getY() != p2.getY()) {
            this.markPlate.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
         }

         this.point1 = point;
         this.cursorPosition = point;
         this.repaintCursor();
         this.showCoordinates(location);
      }
   }

   @Override
   public void mouseExited(Point point, Point location, MouseEvent evt) {
      this.cursorPosition = null;
      this.repaintCursor();
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.cursorPosition != null && this.isFeltpenMode()) {
         int radius = (int)(this.getFeltpenPreviewDiameter() * (double)this.getPlate().getCharacterMetrics().getWidth() / 2.0);
         g.setColor(colorScheme.getColorTool());
         g.drawOval(this.cursorPosition.x - radius, this.cursorPosition.y - radius, 2 * radius, 2 * radius);
      }

      if (this.markPlate != null) {
         LocatedCharacterPlate result = this.markPlate.convert();
         PixelPlateRenderer.paint(g, this.getPlate(), colorScheme, result, plateOrigin);
      }
   }
}

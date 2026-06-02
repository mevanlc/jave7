package de.jave.jave;

import de.jave.jave.algorithm.fill.FillAlgorithm;
import de.jave.jave.algorithm.fill.FillMatchMode;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.pixelplate.PixelPlateOptionsView;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.rendering.PixelPlateRenderer;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.dizzy.commons.swing.mousecursor.CursorId;
import net.dizzy.commons.swing.mousecursor.CursorProvider;

public class EllipseToolGeneric extends GenericTool {
   private Point point1;
   private Point point2;
   private JCheckBox cbFill;

   public EllipseToolGeneric(JaveMainPanel mainPanel, JavEApplication application, Filter filter) {
      super(mainPanel, application, filter);
   }

   @Override
   public String getName() {
      return "Ellipse Generic";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_ELLIPSE_GENERIC_ICON;
   }

   @Override
   protected IInlineToolOptions buildInlineOptions() {
      this.cbFill = new JCheckBox("Fill", false);
      JComponent view = new PixelPlateOptionsView(this.pixelPlateModel, this.getMixCharactersModel()).getContent();
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(view, BorderLayout.CENTER);
      panel.add(this.cbFill, BorderLayout.SOUTH);
      return () -> panel;
   }

   @Override
   public void takeToHand() {
      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.markPlate != null && this.point1 != null && this.point2 != null) {
         g.setColor(colorScheme.getColorToolHelping());
         g.drawOval(this.point1.x - 1, this.point1.y - 1, 2, 2);
         LocatedCharacterPlate result = this.createResult();
         PixelPlateRenderer.paint(g, this.getPlate(), colorScheme, result, plateOrigin);
      }
   }

   private LocatedCharacterPlate createResult() {
      Point point3 = getQuadraticPointFor(this.point1, this.point2, shiftDown);
      Point2d origin = this.getPlate().getRealLocationForScreenPoint(this.point1);
      Point2d rp = this.getPlate().getRealLocationForScreenPoint(point3);
      double rX = Math.abs(rp.getX() - origin.getX());
      double rY = Math.abs(rp.getY() - origin.getY());
      boolean fill = this.cbFill.isSelected();
      this.markPlate.clear();
      if (fill && !this.isLineMode()) {
         this.markPlate.fillEllipse(origin.getX(), origin.getY(), rX, rY);
      } else {
         this.markPlate.drawEllipse(origin.getX(), origin.getY(), rX, rY);
      }

      LocatedCharacterPlate result = this.markPlate.convert();
      if (fill && this.isLineMode() && rX >= 1.0 && rY >= 1.0) {
         FillAlgorithm.fillSolid(
            result, (int)origin.getX() - this.markPlate.getOriginX(), (int)origin.getY() - this.markPlate.getOriginY(), ' ', FillMatchMode.EQUAL_CHARACTER
         );
      }

      return result;
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (location != null) {
         this.point1 = point;
         this.point2 = point;
         this.markPlate = this.createMarkPlate(location);
         this.pixelPlateModel.configure(this.markPlate);
         this.markPlate.setCharacter(this.getMouseChar());
         this.repaintCursor();
      }
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      if (this.markPlate != null) {
         this.setMixMode(this.isMix());
         LocatedCharacterPlate result = this.createResult();
         result.pasteInto(this.getPlate().getContent());
         this.point1 = null;
         this.point2 = null;
         this.markPlate = null;
         this.saveCurrentState("draw ellipse");
         this.repaintAll();
      }
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      if (this.point1 != null && this.point2 != null && this.markPlate != null) {
         if (!point.equals(this.point2)) {
            this.point2 = point;
            this.repaintCursor();
         }
      }
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      if (this.markPlate != null) {
         if (code == 27 && this.markPlate != null) {
            this.markPlate = null;
            this.point1 = null;
            this.point2 = null;
            this.repaintCursor();
         }
      }
   }
}

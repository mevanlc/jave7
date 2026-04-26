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
import net.disy.commons.swing.mousecursor.CursorId;
import net.disy.commons.swing.mousecursor.CursorProvider;

public class RectangleToolGeneric extends GenericTool {
   private Point clickPoint;
   private Point dragPoint;
   private JCheckBox cbFill;

   public RectangleToolGeneric(JaveMainPanel mainPanel, JavEApplication application, Filter filter) {
      super(mainPanel, application, filter);
   }

   @Override
   public String getName() {
      return "Rectangle Generic";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_RECTANGLE_GENERIC_ICON;
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
      if (this.clickPoint != null && this.dragPoint != null && this.markPlate != null) {
         this.dragPoint = point;
         this.repaintCursor();
      }
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      if (this.clickPoint != null && this.dragPoint != null) {
         if (this.markPlate != null) {
            this.setMixMode(this.isMix());
            LocatedCharacterPlate result = this.createResult();
            result.pasteInto(this.getPlate().getContent());
            this.saveCurrentState("rectangle");
            this.repaintAll();
            this.markPlate = null;
            this.clickPoint = null;
            this.dragPoint = null;
         }
      }
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      if (this.markPlate != null) {
         if (code == 27 && this.clickPoint != null) {
            this.clickPoint = null;
            this.dragPoint = null;
            this.markPlate = null;
            this.repaintCursor();
         }
      }
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.clickPoint != null) {
         g.setColor(colorScheme.getColorToolHelping());
         g.drawRect(this.clickPoint.x - 2, this.clickPoint.y - 2, 5, 5);
         if (this.dragPoint != null) {
            g.drawRect(this.dragPoint.x - 2, this.dragPoint.y - 2, 5, 5);
            LocatedCharacterPlate result = this.createResult();
            PixelPlateRenderer.paint(g, this.getPlate(), colorScheme, result, plateOrigin);
         }
      }
   }

   private LocatedCharacterPlate createResult() {
      Point point3 = getQuadraticPointFor(this.clickPoint, this.dragPoint, shiftDown);
      Point2d p1 = this.getPlate().getRealLocationForScreenPoint(this.clickPoint);
      Point2d p2 = this.getPlate().getRealLocationForScreenPoint(point3);
      this.markPlate.clear();
      boolean fill = this.cbFill.isSelected();
      if (fill && !this.isLineMode()) {
         this.markPlate.fillRectangle(p1, p2);
      } else {
         this.markPlate.drawRectangle(p1, p2);
      }

      LocatedCharacterPlate result = this.markPlate.convert();
      if (fill && this.isLineMode() && Math.abs(p2.getX() - p1.getX()) >= 1.0 && Math.abs(p2.getY() - p1.getY()) >= 1.0) {
         FillAlgorithm.fillSolid(
            result,
            (int)((p1.getX() + p2.getX()) / 2.0) - this.markPlate.getOriginX(),
            (int)((p1.getY() + p2.getY()) / 2.0) - this.markPlate.getOriginY(),
            ' ',
            FillMatchMode.EQUAL_CHARACTER
         );
      }

      return result;
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

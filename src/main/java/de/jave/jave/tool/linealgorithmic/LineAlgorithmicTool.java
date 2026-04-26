package de.jave.jave.tool.linealgorithmic;

import de.jave.jave.JavEApplication;
import de.jave.jave.LineAlgorithm;
import de.jave.jave.Point2d;
import de.jave.jave.Tool;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.rendering.PixelPlateRenderer;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import net.disy.commons.swing.mousecursor.CursorId;
import net.disy.commons.swing.mousecursor.CursorProvider;

public class LineAlgorithmicTool extends Tool {
   private Point point1;
   private Point point2;
   private final LineAlgorithmicOptions options = new LineAlgorithmicOptions();
   private IInlineToolOptions inlineOptions;

   public LineAlgorithmicTool(JaveMainPanel plate, JavEApplication application, Filter filter) {
      super(plate, application, filter);
   }

   @Override
   public String getName() {
      return "Line Algorithmic";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_LINE_ALGORITHMIC_ICON;
   }

   @Override
   public IInlineToolOptions getInlineOptionsPanel() {
      if (this.inlineOptions == null) {
         this.inlineOptions = new LineAlgorithmicOptionsPanel(this.options, this.getMixCharactersModel(), this.getMouseCharacterModel());
      }
      return this.inlineOptions;
   }

   @Override
   public void takeToHand() {
      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
      shiftDown = false;
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (location != null) {
         this.point1 = point;
         this.point2 = point;
         this.markPlate = this.createMarkPlate(location);
         this.markPlate.setMode(PixelPlateMode.CHAR);
         this.markPlate.setCharacter(this.getMouseChar());
         this.repaintCursor();
      }
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      if (location == null) {
         this.point2 = null;
         this.repaintCursor();
      } else if (!point.equals(this.point2)) {
         this.point2 = point;
         shiftDown = evt.isShiftDown();
         this.repaintCursor();
      }
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      if (this.point1 != null && point != null && location != null) {
         if (this.markPlate != null) {
            this.setMixMode(this.isMix());
            LocatedCharacterPlate result = this.markPlate.convert();
            result.pasteInto(this.getPlate().getContent());
            this.markPlate = null;
            this.saveCurrentState("draw line");
            this.repaintAll();
         }
      }
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      if (this.markPlate != null) {
         if (code == 27) {
            this.point1 = null;
            this.point2 = null;
            this.markPlate = null;
            this.repaintCursor();
         } else if (code == 38 && this.point1 != null) {
            this.point1.y--;
            this.repaintCursor();
         } else if (code == 40 && this.point1 != null) {
            this.point1.y++;
            this.repaintCursor();
         } else if (code == 37 && this.point1 != null) {
            this.point1.x--;
            this.repaintCursor();
         } else if (code == 39 && this.point1 != null) {
            this.point1.x++;
            this.repaintCursor();
         }
      }
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.markPlate != null) {
         g.setColor(colorScheme.getColorToolHelping());
         g.drawLine(this.point1.x, this.point1.y, this.point2.x, this.point2.y);
         AlgorithmicLineStyle style = this.options.getStyle();
         this.markPlate.clear();
         Point2d pp1 = this.getPlate().getRealLocationForScreenPoint(this.point1);
         Point2d pp2 = this.getPlate().getRealLocationForScreenPoint(this.point2);
         pp1.translate(-0.5, -0.5);
         pp2.translate(-0.5, -0.5);
         if (style != AlgorithmicLineStyle.CHARACTERS) {
            LineAlgorithm.drawLine(this.markPlate, pp1.getX(), pp1.getY(), pp2.getX(), pp2.getY(), style, shiftDown);
         } else {
            LineAlgorithm.drawLineBresenham(
               this.markPlate,
               (int)Math.round(pp1.getX()),
               (int)Math.round(pp1.getY()),
               (int)Math.round(pp2.getX()),
               (int)Math.round(pp2.getY()),
               this.getMouseChar()
            );
         }

         LocatedCharacterPlate result = this.markPlate.convert();
         PixelPlateRenderer.paint(g, this.getPlate(), colorScheme, result, plateOrigin);
      }
   }
}

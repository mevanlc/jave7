package de.jave.jave.tool.ellipsealgorithmic;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.gfx.GfxTools;
import de.jave.jave.EllipseAlgorithm;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.JaveMessages;
import de.jave.jave.Tool;
import de.jave.jave.algorithm.freehandalgorithmic.FreehandAlgorithm;
import de.jave.jave.algorithm.freehandalgorithmic.FreehandAlgorithmicMode;
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
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.mousecursor.CursorId;
import net.disy.commons.swing.mousecursor.CursorProvider;

public class EllipseAlgorithmicTool extends Tool {
   private final EllipseAlgorithmicOptions options = new EllipseAlgorithmicOptions();
   private final Filter filter;
   private Point clickLocation;
   private Point dragLocation;
   private IInlineToolOptions inlineOptions;

   public EllipseAlgorithmicTool(JaveMainPanel plate, JavEApplication application, Filter filter) {
      super(plate, application, filter);
      Ensure.ensureArgumentNotNull(filter);
      this.filter = filter;
   }

   @Override
   public String getName() {
      return JaveMessages.Tool_EllipseAlgorithmic_Name;
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_ELLISPE_ALGORITHMIC_ICON;
   }

   @Override
   public IInlineToolOptions getInlineOptionsPanel() {
      if (this.inlineOptions == null) {
         this.inlineOptions = new EllipseAlgorithmicOptionsPanel(this.options, this.getMixCharactersModel(), this.getMouseCharacterModel());
      }
      return this.inlineOptions;
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
      if (this.markPlate != null && this.clickLocation != null && this.dragLocation != null) {
         EllipseAlgorithmicTool.EllipseParameters parameters = this.createParameters();
         g.setColor(colorScheme.getColorToolHelping());
         CharacterMetrics characterMetrics = this.getPlate().getCharacterMetrics();
         int width = characterMetrics.getWidth();
         int height = characterMetrics.getHeight();
         g.drawOval(parameters.p1.x + width / 2, parameters.p1.y + height / 2, parameters.w - width, parameters.h - height);
         g.drawOval(parameters.pO.x + width / 2 - 1, parameters.pO.y + height / 2 - 1, 2, 2);
         this.showStatus(
            "("
               + parameters.r.x
               + ","
               + parameters.r.y
               + ") -> ("
               + (parameters.r.x + parameters.r.width - 1)
               + ","
               + (parameters.r.y + parameters.r.height - 1)
               + ")"
               + " = ("
               + parameters.width
               + ","
               + parameters.height
               + ")"
         );
         LocatedCharacterPlate result = this.createResult(parameters);
         PixelPlateRenderer.paint(g, this.getPlate(), colorScheme, result, plateOrigin);
         g.setColor(colorScheme.getColorToolHelping());
         GfxTools.drawBrokenRectangle(g, parameters.p1.x, parameters.p1.y, parameters.w, parameters.h);
         g.setFont(JaveGlobalRessources.FONT_SMALL);
         g.drawString(parameters.width + "x" + parameters.height, parameters.p1.x + 2, parameters.p1.y + 10);
      }
   }

   private LocatedCharacterPlate createResult(EllipseAlgorithmicTool.EllipseParameters parameters) {
      this.markPlate.clear();
      AlgorithmicEllipseStyle style = this.options.getStyle();
      LocatedCharacterPlate result;
      if (style == AlgorithmicEllipseStyle.CHARACTERS) {
         EllipseAlgorithm.drawEllipse(this.markPlate, parameters.oX, parameters.oY, parameters.radiusX, parameters.radiusY, this.getMouseChar());
         result = this.markPlate.convert();
      } else {
         EllipseAlgorithm.drawEllipse(this.markPlate, parameters.oX, parameters.oY, parameters.radiusX, parameters.radiusY, '#');
         result = new FreehandAlgorithm(this.filter).convertMarksToFreehandLine(this.markPlate, FreehandAlgorithmicMode.LINES_ROUNDED);
      }

      return result;
   }

   private EllipseAlgorithmicTool.EllipseParameters createParameters() {
      Point location3 = getQuadraticLocation(this.clickLocation, this.dragLocation, shiftDown);
      int oX = this.clickLocation.x;
      int oY = this.clickLocation.y;
      int radiusX = Math.abs(location3.x - this.clickLocation.x);
      int radiusY = Math.abs(location3.y - this.clickLocation.y);
      int width = radiusX * 2 + 1;
      int height = radiusY * 2 + 1;
      EllipseAlgorithmicTool.EllipseParameters parameters = new EllipseAlgorithmicTool.EllipseParameters();
      parameters.r = getRectangleFor(this.clickLocation, location3);
      parameters.w = width * this.getPlate().getCharWidth() + 1;
      parameters.h = height * this.getPlate().getCharHeight() + 1;
      parameters.oX = oX;
      parameters.oY = oY;
      parameters.pO = this.getScreenPointFor(oX, oY);
      parameters.p1 = this.getScreenPointFor(oX - radiusX, oY - radiusY);
      parameters.width = width;
      parameters.height = height;
      parameters.radiusX = radiusX;
      parameters.radiusY = radiusY;
      return parameters;
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (location != null) {
         this.clickLocation = location;
         this.dragLocation = location;
         this.markPlate = this.createMarkPlate(location);
         this.markPlate.setMode(PixelPlateMode.CHAR);
         this.repaintCursor();
      }
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      if (this.markPlate != null) {
         this.setMixMode(this.isMix());
         EllipseAlgorithmicTool.EllipseParameters parameters = this.createParameters();
         LocatedCharacterPlate result = this.createResult(parameters);
         result.pasteInto(this.getPlate().getContent());
         this.markPlate = null;
         this.saveCurrentState(JaveMessages.Tool_EllipseAlgorithmic_UndoName);
         this.repaintAll();
      }
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      if (location == null) {
         if (this.dragLocation != null) {
            this.dragLocation = null;
            this.repaintCursor();
         }
      } else if (!location.equals(this.dragLocation)) {
         this.dragLocation = location;
         this.repaintCursor();
      }
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      if (code == 27) {
         this.clickLocation = null;
         this.dragLocation = null;
         this.repaintCursor();
      }
   }

   private static final class EllipseParameters {
      public Rectangle r;
      public int w;
      public int h;
      public Point pO;
      public Point p1;
      public int oY;
      public int oX;
      public int width;
      public int height;
      public int radiusX;
      public int radiusY;

      private EllipseParameters() {
      }
   }
}

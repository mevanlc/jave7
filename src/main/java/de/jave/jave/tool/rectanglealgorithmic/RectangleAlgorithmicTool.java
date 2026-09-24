package de.jave.jave.tool.rectanglealgorithmic;

import de.jave.gfx.GfxTools;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.MergeCharactersPanel;
import de.jave.jave.RectangleAlgorithm;
import de.jave.jave.Tool;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.rectangle.RectangleStylePanel;
import de.jave.jave.rendering.PixelPlateRenderer;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.mousecursor.CursorId;
import net.dizzy.commons.swing.mousecursor.CursorProvider;

public class RectangleAlgorithmicTool extends Tool {
   private Point clickLocation;
   private Point dragLocation;
   private RectangleStylePanel rectangleStylePanel;
   private IInlineToolOptions inlineOptions;

   public RectangleAlgorithmicTool(JaveMainPanel plate, JavEApplication application, Filter filter) {
      super(plate, application, filter);
   }

   @Override
   public String getName() {
      return "Rectangle Algorithmic";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_RECTANGLE_ALGORITHMIC_ICON;
   }

   @Override
   public IInlineToolOptions getInlineOptionsPanel() {
      if (this.inlineOptions == null) {
         this.rectangleStylePanel = new RectangleStylePanel(this.getMouseCharacterModel());
         this.rectangleStylePanel.addItemListener(this);
         JPanel panel = new JPanel(new GridDialogLayout(1, false));
         panel.add(this.rectangleStylePanel.getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
         panel.add(new MergeCharactersPanel(this.getMixCharactersModel()).getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
         this.inlineOptions = () -> panel;
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
      if (this.clickLocation != null && this.dragLocation != null) {
         if (this.markPlate != null) {
            Point location3 = getQuadraticLocation(this.clickLocation, this.dragLocation, shiftDown);
            Rectangle r = getRectangleFor(this.clickLocation, location3);
            this.showStatus("(" + r.x + "," + r.y + ") -> (" + (r.x + r.width - 1) + "," + (r.y + r.height - 1) + ")" + " = (" + r.width + "," + r.height + ")");
            this.markPlate.clear();
            char[] chars = this.rectangleStylePanel.getCurrentChars();
            if (this.rectangleStylePanel.isUnderLineStyle()) {
               r.y--;
               r.height++;
            }

            RectangleAlgorithm.drawRectangle(this.markPlate, r, chars);
            LocatedCharacterPlate result = this.markPlate.convert();
            PixelPlateRenderer.paint(g, this.getPlate(), colorScheme, result, plateOrigin);
            Point p = this.getScreenPointFor(r.x, r.y);
            g.setColor(colorScheme.getColorToolHelping());
            GfxTools.drawBrokenRectangle(g, p.x, p.y, r.width * this.getPlate().getCharWidth(), r.height * this.getPlate().getCharHeight());
            if (r.width - 2 > 0 && r.height - 2 > 0) {
               GfxTools.drawBrokenRectangle(
                  g,
                  p.x + this.getPlate().getCharWidth(),
                  p.y + this.getPlate().getCharHeight(),
                  (r.width - 2) * this.getPlate().getCharWidth(),
                  (r.height - 2) * this.getPlate().getCharHeight()
               );
            }

            g.setFont(JaveGlobalRessources.FONT_SMALL);
            g.drawString(r.width + "x" + r.height, p.x + 2, p.y - 2);
         }
      }
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
      if (this.clickLocation != null && location != null) {
         if (this.markPlate != null) {
            this.setMixMode(this.isMix());
            LocatedCharacterPlate result = this.markPlate.convert();
            result.pasteInto(this.getPlate().getContent());
            this.getPlate().saveCurrentState("draw rectangle");
            this.getPlate().repaint();
            this.markPlate = null;
         }

         this.showStatus("(" + location.x + "," + location.y + ")");
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
         this.markPlate = null;
         this.repaintCursor();
      }
   }
}

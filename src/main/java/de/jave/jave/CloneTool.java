package de.jave.jave;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.rendering.GlyphRenderer;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JPanel;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.util.GuiUtilities;

public class CloneTool extends EraserTool {
   private int dx;
   private int dy;
   private Point cloneLocation;
   private boolean positionLocked = false;
   private IInlineToolOptions cloneInlineOptions;

   public CloneTool(JaveMainPanel plate, JavEApplication application, Filter filter) {
      super(plate, application, filter);
   }

   @Override
   public IInlineToolOptions getInlineOptionsPanel() {
      if (this.cloneInlineOptions == null) {
         JPanel panel = new JPanel(new GridDialogLayout(1, false));
         panel.add(this.buildEraserOptionsContent());
         panel.add(new MergeCharactersPanel(this.getMixCharactersModel()).getContent());
         this.cloneInlineOptions = new EraserOptionsPanel(panel);
      }
      return this.cloneInlineOptions;
   }

   @Override
   public String getName() {
      return "Clone Tool";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_CLONE_BRUSH_ICON;
   }

   @Override
   public String getUndoRedoActionName() {
      return "clone";
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      int[][] brush = this.getBrush();
      if (this.cursorLocation != null) {
         g.setColor(colorScheme.getColorTool());
         this.paintBrushBorder(g, brush, this.cursorLocation.x, this.cursorLocation.y);
         g.setColor(colorScheme.getColorToolHelping());
         int x0 = 0;
         int y0 = 0;
         if (!this.positionLocked && this.cloneLocation != null) {
            x0 = this.cloneLocation.x;
            y0 = this.cloneLocation.y;
         } else {
            if (!this.positionLocked) {
               return;
            }

            x0 = this.cursorLocation.x - this.dx;
            y0 = this.cursorLocation.y - this.dy;
         }

         this.paintBrushBorder(g, brush, x0, y0);
         g.setColor(colorScheme.getColorTool());
         int h = brush.length;
         int w = brush[0].length;
         int cx = (w - 1) / 2;
         int cy = (h - 1) / 2;
         Point p0 = this.getScreenPointFor(this.cursorLocation.x - cx, this.cursorLocation.y - cy);
         g.setColor(colorScheme.getColorToolDarker());
         CharacterMetrics characterMetrics = this.getPlate().getCharacterMetrics();
         int characterWidth = characterMetrics.getWidth();
         int ascent = characterMetrics.getAscent();
         int characterHeight = characterMetrics.getHeight();

         for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
               int pixel = brush[y][x];
               if (pixel > 0) {
                  int xx = x0 - cx + x;
                  int yy = y0 - cy + y;
                  if (this.getPlate().isInside(xx, yy)) {
                     GlyphRenderer.drawCell(g, this.getPlate().getChar(xx, yy), p0.x, x, p0.y + y * characterHeight + ascent, characterWidth);
                  }
               }
            }
         }
      }
   }

   @Override
   protected void paint(int x0, int y0) {
      if (this.cursorLocation != null && this.cloneLocation != null) {
         int[][] brush = this.getBrush();
         int h = brush.length;
         int w = brush[0].length;
         int cx = (w - 1) / 2;
         int cy = (h - 1) / 2;
         this.setMixMode(this.isMix());

         for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
               int pixel = brush[y][x];
               if (pixel > 0) {
                  int xx = x0 - cx + x;
                  int yy = y0 - cy + y;
                  if (this.getPlate().isInside(xx - this.dx, yy - this.dy)) {
                     this.getPlate().setChar(xx, yy, this.getPlate().getChar(xx - this.dx, yy - this.dy));
                  }
               }
            }
         }

         this.getPlate().repaint(50L);
      }
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (!evt.isShiftDown() && !evt.isMetaDown()) {
         this.cursorLocation = location;
         if (this.cloneLocation == null) {
            this.showExplanationDialog(GuiUtilities.getWindowFor(evt));
         } else {
            if (!this.positionLocked) {
               this.positionLocked = true;
               this.dx = this.cursorLocation.x - this.cloneLocation.x;
               this.dy = this.cursorLocation.y - this.cloneLocation.y;
            }

            this.paint(location);
         }
      } else {
         this.positionLocked = false;
         this.cloneLocation = location;
         this.dx = 0;
         this.dy = 0;
         this.repaintCursor();
      }
   }

   private void showExplanationDialog(Component parent) {
      MessageDialogFactory.showMessageDialog(
         parent,
         new Message(
            "JavE - Clone Tool",
            "You must specify a clone source by right-clicking\nor clicking on the image with the shift key down\nbefore you can use the clone brush.",
            MessageType.INFORMATION
         )
      );
   }
}

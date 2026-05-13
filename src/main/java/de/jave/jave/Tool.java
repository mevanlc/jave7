package de.jave.jave;

import de.jave.jave.filter.Filter;
import de.jave.jave.pixelplate.PixelPlate;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.plate.MouseCharacterModel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import de.jave.lib.Toolbox;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.mousecursor.CursorId;
import net.disy.commons.swing.mousecursor.CursorProvider;

public abstract class Tool implements ItemListener, ActionListener {
   protected JaveMainPanel mainPanel;
   protected JavEApplication application;
   protected static boolean altDown = false;
   protected static boolean shiftDown = false;
   protected static boolean controlDown = false;
   protected static boolean metaDown = false;
   protected static boolean insert = false;
   protected static final double CORRECT_FACTOR = 1.98;
   protected PixelPlate markPlate;
   private final Filter filter;
   protected Cursor cursor;

   protected final void requestPlateFocus() {
      Plate plate = this.getPlate();
      if (plate != null) {
         plate.requestFocus();
      }
   }

   protected final PlateDocument getDocument() {
      Plate plate = this.getPlate();
      return plate == null ? null : plate.getDocument();
   }

   public Point getCursorLocation() {
      PlateDocument document = this.getDocument();
      return document != null ? document.getCursorLocation() : null;
   }

   protected final Plate getPlate() {
      IDocumentEditor activeEditor = this.getEditor();
      return activeEditor == null ? null : activeEditor.getPlate();
   }

   protected final IDocumentEditor getEditor() {
      return this.mainPanel.getActiveEditorModel().getActiveEditor();
   }

   public void cursorUp(int lines) {
   }

   public void cursorDown(int lines) {
   }

   public static boolean isInsert() {
      return insert;
   }

   public void setInsert(boolean what) {
      insert = what;
   }

   public boolean containsScreenPoint(Point point) {
      return false;
   }

   public boolean containsLocation(Point location) {
      return false;
   }

   public Tool(JaveMainPanel mainPanel, JavEApplication application, Filter filter) {
      Ensure.ensureArgumentNotNull(filter);
      this.mainPanel = mainPanel;
      this.application = application;
      this.filter = filter;
   }

   protected final PixelPlate createMarkPlate(Point location) {
      Rectangle documentBounds = new Rectangle(this.getDocument().getSize());
      int initialOriginX = Math.max(location.x - 20, documentBounds.x);
      int initialOriginY = Math.max(location.y - 20, documentBounds.y);
      int width = Math.min(documentBounds.x, initialOriginX + 40);
      int height = Math.min(documentBounds.y, initialOriginY + 40);
      Rectangle initialBounds = new Rectangle(initialOriginX, initialOriginY, width, height);
      return new PixelPlate(initialBounds, documentBounds, this.filter);
   }

   public void prepareForSave() {
   }

   public boolean isActiveTool() {
      return this.mainPanel.getCurrentTool() == this;
   }

   public static final void setMetaDown(boolean what) {
      metaDown = what;
   }

   protected Rectangle getAutoSelectRegion(int x, int y) {
      boolean[][] marks = new boolean[this.mainPanel.getDocumentSize().width][this.mainPanel.getDocumentSize().height];
      marks[x][y] = true;
      Rectangle region = new Rectangle(x, y, 0, 0);
      this.crawl(x + 1, y, marks, region);
      this.crawl(x - 1, y, marks, region);
      this.crawl(x, y + 1, marks, region);
      this.crawl(x, y - 1, marks, region);
      region.width++;
      region.height++;
      return region;
   }

   protected void crawl(int x, int y, boolean[][] marks, Rectangle region) {
      if (x >= 0 && y >= 0 && x < this.mainPanel.getDocumentSize().width && y < this.mainPanel.getDocumentSize().height && !marks[x][y]) {
         marks[x][y] = true;
         if (this.getPlate().getContent().get(x, y) != ' ') {
            if (x < region.x) {
               region.x--;
               region.width++;
            } else if (y < region.y) {
               region.y--;
               region.height++;
            } else if (x > region.x + region.width) {
               region.width++;
            } else if (y > region.y + region.height) {
               region.height++;
            }

            this.crawl(x + 1, y, marks, region);
            this.crawl(x - 1, y, marks, region);
            this.crawl(x, y + 1, marks, region);
            this.crawl(x, y - 1, marks, region);
         }
      }
   }

   public void shiftReleased() {
      if (shiftDown) {
         shiftDown = false;
         if (this.markPlate != null) {
            this.repaintCursor();
         }
      }
   }

   public void shiftPressed() {
      if (!shiftDown) {
         shiftDown = true;
         if (this.markPlate != null) {
            this.repaintCursor();
         }
      }
   }

   public void altReleased() {
      altDown = false;
   }

   public void altPressed() {
      altDown = true;
   }

   public final void controlReleased() {
      controlDown = false;
   }

   public final void controlPressed() {
      controlDown = true;
   }

   public static final Point getQuadraticLocation(Point location1, Point location2, boolean isShiftDown) {
      Point location3 = new Point(location2.x, location2.y);
      if (isShiftDown) {
         int dx = location2.x - location1.x;
         int dy = location2.y - location1.y;
         if (Toolbox.abs(dx) > Toolbox.abs((int)Math.round((double)dy * 1.98))) {
            location3.y = location1.y + Toolbox.abs((int)Math.round((double)dx / 1.98)) * sgn(dy);
         } else {
            location3.x = location1.x + Toolbox.abs((int)Math.round((double)dy * 1.98)) * sgn(dx);
         }
      }

      return location3;
   }

   public static final Point getQuadraticPointFor(Point point1, Point point2, boolean isShiftDown) {
      if (!isShiftDown) {
         return point2;
      } else {
         Point point3 = new Point(point2.x, point2.y);
         int dx = point2.x - point1.x;
         int dy = point2.y - point1.y;
         if (Toolbox.abs(dx) > Toolbox.abs(dy)) {
            point3.y = point1.y + Toolbox.abs(dx) * sgn(dy);
         } else {
            point3.x = point1.x + Toolbox.abs(dy) * sgn(dx);
         }

         return point3;
      }
   }

   public void checkSize() {
   }

   public static final int sgn(int x) {
      return x < 0 ? -1 : 1;
   }

   @Override
   public void itemStateChanged(ItemEvent evt) {
      this.mainPanel.requestFocus();
   }

   @Override
   public void actionPerformed(ActionEvent evt) {
      this.mainPanel.requestFocus();
   }

   public void beep() {
      Toolkit.getDefaultToolkit().beep();
   }

   public abstract Icon getIcon();

   public abstract String getName();

   public void setCursor(Cursor c) {
      this.cursor = c;
      Plate plate = this.mainPanel.getPlate();
      if (plate != null) {
         plate.setCursor(this.cursor);
      }
   }

   public final Cursor getCursor() {
      return this.cursor != null ? this.cursor : CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION);
   }

   public void showCoordinates(Point p) {
      this.mainPanel.getPlate().showCoordinates(p);
   }

   public void mouseEntered(Point point, Point location, MouseEvent evt) {
   }

   public void mouseExited(Point point, Point location, MouseEvent evt) {
   }

   public void mouseMoved(Point point, Point location, MouseEvent evt) {
   }

   public void mouseDragged(Point point, Point location, MouseEvent evt) {
   }

   public void mousePressed(Point point, Point location, MouseEvent evt) {
   }

   public void mouseReleased(Point point, Point location, MouseEvent evt) {
   }

   public void mouseClicked(Point point, Point location, MouseEvent evt) {
   }

   public final void setMixMode(boolean what) {
      Plate plate = this.mainPanel.getPlate();
      if (plate != null) {
         plate.setMix(what);
      }
   }

   public void keyReleased(KeyEvent evt) {
   }

   public void keyPressed(int code, KeyEvent evt) {
   }

   public void keyTyped(char ch, KeyEvent evt) {
   }

   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
   }

   public void repaintCursor() {
      this.mainPanel.repaintCursor();
   }

   public void repaintAll() {
      this.mainPanel.repaint();
   }

   public void saveCurrentState(String shortDescription) {
      this.mainPanel.saveCurrentState(shortDescription);
   }

   @Deprecated
   public char getMouseChar() {
      return this.getMouseCharacterModel().getCharacter(this.isMouseRightButton());
   }

   protected final MouseCharacterModel getMouseCharacterModel() {
      return this.mainPanel.getMouseCharacterModel();
   }

   protected final BooleanModel getMixCharactersModel() {
      return this.mainPanel.getMixCharactersModel();
   }

   protected final boolean isMix() {
      return this.getMixCharactersModel().getValue();
   }

   public boolean isMouseRightButton() {
      return this.mainPanel.getPlate().isMouseRightButton();
   }

   public abstract void putAside(boolean var1);

   public abstract void takeToHand();

   public void reset() {
      this.putAside(false);
      this.takeToHand();
   }

   /**
    * Returns this tool's inline options panel for rendering in the tool
    * selector bar. Default returns {@code null}, meaning the tool has no
    * inline panel and the host renders the fallback. Tools migrated to
    * the inline-options contract override this.
    */
   public IInlineToolOptions getInlineOptionsPanel() {
      return null;
   }

   protected void showStatus(String text) {
      this.mainPanel.showStatus(text);
   }

   protected Point getScreenPointFor(Point location) {
      return this.mainPanel.getPlate().getScreenPointFor(location);
   }

   protected Point getScreenPointFor(int x, int y) {
      return this.mainPanel.getPlate().getScreenPointFor(x, y);
   }

   protected Point getLocationForScreenPoint(Point point) {
      return this.mainPanel.getPlate().getLocationForScreenPoint(point);
   }

   public static Rectangle getRectangleFor(Point location1, Point location2) {
      return new Rectangle(
         location1.x < location2.x ? location1.x : location2.x,
         location1.y < location2.y ? location1.y : location2.y,
         1 + (location1.x < location2.x ? location2.x - location1.x : location1.x - location2.x),
         1 + (location1.y < location2.y ? location2.y - location1.y : location1.y - location2.y)
      );
   }
}

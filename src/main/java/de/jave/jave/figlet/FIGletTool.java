package de.jave.jave.figlet;

import de.jave.figlet.engine.FigConversionJobProcessor;
import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.processing.FigletJobFactory;
import de.jave.figlet.engine.processing.IFigletJob;
import de.jave.figlet.swing.ui.FigletIcons;
import de.jave.gfx.GfxTools;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.Tool;
import de.jave.jave.filter.Filter;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.CharacterPlate;
import de.jave.lib.job.IResultConsumer;
import de.jave.lib.job.NullWarningCollector;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import net.disy.commons.core.asynchronous.AsynchronousDroppingJobProcessor;
import net.disy.commons.core.exception.PrintStackTraceExceptionHandler;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;

public class FIGletTool extends Tool implements IResultConsumer {
   private final IFigDriver figDriver;
   private Point textOrigin = null;
   private Point location1;
   private StringBuffer keyboardBuffer;
   private Rectangle previewRegion;
   private CharacterPlate previewPlate;
   private AsynchronousDroppingJobProcessor<IFigletJob> processor;
   private FigFontModel fontModel;
   private FigletToolOptionsPanel panel;

   public FIGletTool(JaveMainPanel plate, JavEApplication application, IFigDriver figDriver, Filter filter) {
      super(plate, application, filter);
      Ensure.ensureArgumentNotNull(figDriver);
      this.figDriver = figDriver;
   }

   @Override
   public boolean containsScreenPoint(Point point) {
      return this.previewRegion != null && this.previewRegion.contains(point);
   }

   private void prevFont() {
      this.panel.selectPreviousFont();
   }

   private void nextFont() {
      this.panel.selectNextFont();
   }

   @Override
   protected JComponent createOptionsComponent() {
      FigConversionJobProcessor figThread = new FigConversionJobProcessor(this.figDriver, this, new NullWarningCollector());
      this.processor = new AsynchronousDroppingJobProcessor<>(figThread, new PrintStackTraceExceptionHandler());
      this.panel = new FigletToolOptionsPanel(this.figDriver, this.getMixCharactersModel());
      this.getMixCharactersModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FIGletTool.this.fontChanged();
         }
      });
      this.fontModel = this.panel.getFontModel();
      this.fontModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FIGletTool.this.fontChanged();
         }
      });
      this.fontChanged();
      return this.panel.getContent();
   }

   @Override
   public void putResult(Object o) {
      if (o == null) {
         this.previewPlate = null;
         this.repaintCursor();
      } else {
         this.previewPlate = new CharacterPlate((String)o);
         this.previewPlate.setMix(this.isMix());
         if (this.previewPlate.isEmpty(0, 0, 0, this.previewPlate.getHeight() - 1)) {
            this.previewPlate.removeColumnsLeft(1);
         }

         this.repaintCursor();
      }
   }

   private void fontChanged() {
      this.requestPlateFocus();
      this.repaintCursor();
      this.figletize();
   }

   private void figletize() {
      if (this.keyboardBuffer != null) {
         if (this.keyboardBuffer.length() == 0) {
            this.previewPlate = null;
            this.repaintCursor();
         } else {
            this.processor.startJob(FigletJobFactory.createJob(this.keyboardBuffer.toString(), this.fontModel.getFont()));
         }
      }
   }

   @Override
   public String getName() {
      return "FIGlet";
   }

   @Override
   public Icon getIcon() {
      return FigletIcons.FIGLET_ICON;
   }

   @Override
   public void takeToHand() {
      this.setCursor(Cursor.getPredefinedCursor(2));
      this.checkSize();
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
      this.pasteResult();
   }

   @Override
   public void checkSize() {
      if (this.textOrigin != null) {
         if (this.textOrigin.x >= this.getPlate().getDocumentWidth()) {
            this.textOrigin.x = this.getPlate().getDocumentWidth() - 1;
         }

         if (this.textOrigin.y >= this.getPlate().getDocumentHeight()) {
            this.textOrigin.y = this.getPlate().getDocumentHeight() - 1;
         }
      }
   }

   @Override
   public void cursorUp(int lines) {
      this.textOrigin.y -= lines;
      if (this.textOrigin.y < 0) {
         this.textOrigin.y = 0;
      }
   }

   @Override
   public void cursorDown(int lines) {
      this.textOrigin.y += lines;
      if (this.textOrigin.y >= this.getPlate().getDocumentHeight()) {
         this.textOrigin.y = this.getPlate().getDocumentHeight() - 1;
      }
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.textOrigin != null) {
         int cursorLocationX = this.textOrigin.x;
         if (this.previewPlate != null) {
            FigFont font = this.fontModel.getFont();
            int x = this.textOrigin.x;
            int y = this.textOrigin.y - (font.getHeight() - font.getUnderLength()) + 1;
            this.getPlate().paintPreview(g, this.previewPlate, colorScheme, 0, 0, x, y);
            cursorLocationX += this.previewPlate.getWidth();
            Point p = this.getPlate().getScreenPointFor(x, y);
            this.previewRegion = new Rectangle(
               p.x, p.y, this.previewPlate.getWidth() * this.getPlate().getCharWidth(), font.getHeight() * this.getPlate().getCharHeight()
            );
            g.setColor(colorScheme.getColorToolHelping());
            GfxTools.drawBrokenRectangle(g, this.previewRegion.x, this.previewRegion.y, this.previewRegion.width, this.previewRegion.height);
            g.setFont(JaveGlobalRessources.FONT_SMALL);
            String text = this.previewPlate.getWidth() + "x" + this.previewPlate.getHeight();
            g.drawString(text, this.previewRegion.x, this.previewRegion.y - 2);
            int width1 = g.getFontMetrics().stringWidth(text);
            width1 = (width1 / this.getPlate().getCharWidth() + 2) * this.getPlate().getCharWidth();
            text = this.keyboardBuffer.toString();
            g.setFont(this.getPlate().getFont());
            g.drawString(text, this.previewRegion.x + width1, this.previewRegion.y - 2);
            FontMetrics fm = g.getFontMetrics();
            int h = fm.getHeight();
            int w = fm.stringWidth(text);
            int xx = this.previewRegion.x + width1 + w + 3;
            int y1 = this.previewRegion.y - h + 4;
            int y2 = this.previewRegion.y - 2;
            g.drawLine(xx, y1, xx, y2);
            g.drawLine(xx - 2, y1 - 1, xx - 2, y1 - 1);
            g.drawLine(xx + 2, y1 - 1, xx + 2, y1 - 1);
            g.drawLine(xx - 2, y2 + 1, xx - 2, y2 + 1);
            g.drawLine(xx + 2, y2 + 1, xx + 2, y2 + 1);
         }

         g.setColor(colorScheme.getColorCursor());
         this.paintTextCursor(g, cursorLocationX, this.textOrigin.y);
      }
   }

   private void paintTextCursor(Graphics g, int x, int y) {
      FigFont font = this.fontModel.getFont();
      Point p = this.getScreenPointFor(x, y - (font.getHeight() - font.getUnderLength()) + 1);
      int x0 = p.x + 2;
      int y1 = p.y + this.getPlate().getCharHeight() * font.getHeight() - 2;
      int y0 = p.y + 2;
      int y2 = p.y + (font.getHeight() - font.getUnderLength()) * this.getPlate().getCharHeight();
      g.drawLine(x0, y0, x0, y1);
      g.drawLine(x0 - 1, y0 - 1, x0 - 4, y0 - 1);
      g.drawLine(x0 + 1, y0 - 1, x0 + 4, y0 - 1);
      g.drawLine(x0 - 1, y1 + 1, x0 - 4, y1 + 1);
      g.drawLine(x0 + 1, y1 + 1, x0 + 4, y1 + 1);
      if (font.getUnderLength() != 0) {
         g.drawLine(x0 - 3, y2, x0 + 3, y2);
      }
   }

   private void pasteResult() {
      if (this.previewPlate != null && this.textOrigin != null) {
         this.setMixMode(this.isMix());
         FigFont font = this.fontModel.getFont();
         this.previewPlate.pasteInto(this.getPlate().getContent(), this.textOrigin.x, this.textOrigin.y - (font.getHeight() - font.getUnderLength()) + 1);
         this.previewPlate = null;
         this.previewRegion = null;
         this.keyboardBuffer = new StringBuffer();
         this.saveCurrentState("FIGlet");
         this.repaintAll();
      }
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (location != null) {
         if (this.previewRegion != null && this.previewRegion.contains(point)) {
            this.location1 = location;
         } else {
            if (this.previewPlate != null) {
               this.pasteResult();
            }

            this.textOrigin = location;
            this.repaintCursor();
         }
      }
   }

   @Override
   public void prepareForSave() {
      if (this.previewPlate != null) {
         this.pasteResult();
      }
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      this.location1 = null;
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      if (this.location1 != null && location != null) {
         if (location.equals(this.location1)) {
            return;
         }

         this.textOrigin.x = this.textOrigin.x + (location.x - this.location1.x);
         this.textOrigin.y = this.textOrigin.y + (location.y - this.location1.y);
         this.location1 = location;
         this.repaintCursor();
      }
   }

   public void charEntered(char ch) {
      if (this.textOrigin != null && ch >= ' ' && ch <= 255 && (ch <= '~' || ch >= 145)) {
         if (this.keyboardBuffer == null) {
            this.keyboardBuffer = new StringBuffer();
         }

         this.keyboardBuffer.append(ch);
         this.figletize();
      }
   }

   protected void delete() {
   }

   protected void backSpace() {
      if (this.keyboardBuffer != null && this.keyboardBuffer.length() != 0) {
         this.keyboardBuffer.setLength(this.keyboardBuffer.length() - 1);
         this.figletize();
      }
   }

   protected void enter() {
      this.pasteResult();
      FigFont font = this.fontModel.getFont();
      this.textOrigin.y = this.textOrigin.y + font.getHeight();
      this.getPlate().ensureVisible(new Point(this.textOrigin.x, this.textOrigin.y + font.getUnderLength()));
      this.repaintCursor();
   }

   @Override
   public void mouseMoved(Point point, Point location, MouseEvent evt) {
      if (this.previewRegion != null) {
         if (this.previewRegion.contains(point)) {
            this.setCursor(Cursor.getPredefinedCursor(13));
         } else {
            this.setCursor(Cursor.getPredefinedCursor(2));
         }
      }
   }

   @Override
   public void keyTyped(char ch, KeyEvent evt) {
      this.charEntered(ch);
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      if (this.textOrigin != null) {
         switch (code) {
            case 8:
               this.backSpace();
               return;
            case 10:
               this.enter();
               return;
            case 27:
               if (this.previewPlate != null) {
                  this.previewPlate = null;
                  this.previewRegion = null;
                  this.keyboardBuffer = new StringBuffer();
                  this.repaintCursor();
               }

               return;
            case 37:
               this.moveCursorLeft();
               evt.consume();
               return;
            case 38:
               if (altDown) {
                  this.nextFont();
               } else {
                  this.moveCursorUp();
               }

               evt.consume();
               return;
            case 39:
               this.moveCursorRight();
               evt.consume();
               return;
            case 40:
               if (altDown) {
                  this.prevFont();
               } else {
                  this.moveCursorDown();
               }

               evt.consume();
               return;
            case 127:
               this.delete();
               return;
         }
      }
   }

   protected void moveCursorRight() {
      this.textOrigin.x++;
      this.repaintCursor();
   }

   protected void moveCursorLeft() {
      if (this.textOrigin.x >= -1) {
         this.textOrigin.x--;
         this.repaintCursor();
      }
   }

   protected void moveCursorNewline() {
   }

   protected void moveCursorRightDown() {
   }

   protected void moveCursorRightUp() {
   }

   protected void moveCursorLeftDown() {
   }

   protected void moveCursorLeftUp() {
   }

   protected void moveCursorDown() {
      this.textOrigin.y++;
      this.getPlate().ensureVisible(new Point(this.textOrigin.x, this.textOrigin.y + this.fontModel.getFont().getUnderLength()));
      this.repaintCursor();
   }

   protected void moveCursorPageDown() {
   }

   protected void moveCursorUp() {
      if (this.textOrigin.y >= 1) {
         this.textOrigin.y--;
         this.getPlate().ensureVisible(new Point(this.textOrigin.x, this.textOrigin.y - this.fontModel.getFont().getHeight()));
         this.repaintCursor();
      }
   }

   protected void moveCursorPageUp() {
   }

   protected void moveCursorEnd() {
   }

   protected void moveCursorPos1() {
   }

   protected void moveCursorCtrlEnd() {
   }

   protected void moveCursorCtrlPos1() {
   }
}

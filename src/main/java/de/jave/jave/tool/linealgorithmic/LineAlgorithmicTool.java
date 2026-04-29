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
import de.jave.preferences.SmartPreferences;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.mousecursor.CursorId;
import net.disy.commons.swing.mousecursor.CursorProvider;

public class LineAlgorithmicTool extends Tool {
   private static final String PREFERENCES_PATH = "tools/lineAlgorithmic";
   private static final double CARDINAL_TIP_RATIO = 2.0;

   private Point point1;
   private Point point2;
   private final LineAlgorithmicOptions options = new LineAlgorithmicOptions();
   private IInlineToolOptions inlineOptions;

   public LineAlgorithmicTool(JaveMainPanel plate, JavEApplication application, Filter filter) {
      super(plate, application, filter);
      final SmartPreferences preferences = new SmartPreferences(application.getApplicationPreferences().getSubPreferences(PREFERENCES_PATH));
      this.options.loadFrom(preferences);
      this.options.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            LineAlgorithmicTool.this.options.saveTo(preferences);
            preferences.flush();
         }
      });
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
            this.drawResult();
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
         } else if (isKey(code, KeyEvent.VK_1, KeyEvent.VK_NUMPAD1)) {
            this.options.setArrowheadSize(this.options.getArrowheadSize() - 1);
            this.repaintCursor();
         } else if (isKey(code, KeyEvent.VK_2, KeyEvent.VK_NUMPAD2)) {
            this.options.setArrowheadSize(this.options.getArrowheadSize() + 1);
            this.repaintCursor();
         } else if (isKey(code, KeyEvent.VK_3, KeyEvent.VK_NUMPAD3)) {
            this.options.setArrowheadAngle(this.options.getArrowheadAngle() - 1);
            this.repaintCursor();
         } else if (isKey(code, KeyEvent.VK_4, KeyEvent.VK_NUMPAD4)) {
            this.options.setArrowheadAngle(this.options.getArrowheadAngle() + 1);
            this.repaintCursor();
         } else if (code == KeyEvent.VK_A) {
            this.options.setArrowheadPlacement(this.options.getArrowheadPlacement().next());
            this.repaintCursor();
         } else if (code == KeyEvent.VK_C) {
            this.options.setCardinalTips(!this.options.isCardinalTips());
            this.repaintCursor();
         }
      }
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.markPlate != null && this.point1 != null && this.point2 != null) {
         g.setColor(colorScheme.getColorToolHelping());
         g.drawLine(this.point1.x, this.point1.y, this.point2.x, this.point2.y);
         this.drawResult();
         LocatedCharacterPlate result = this.markPlate.convert();
         PixelPlateRenderer.paint(g, this.getPlate(), colorScheme, result, plateOrigin);
      }
   }

   private void drawResult() {
      AlgorithmicLineStyle style = this.options.getStyle();
      this.markPlate.clear();
      Point2d pp1 = this.getPlate().getRealLocationForScreenPoint(this.point1);
      Point2d pp2 = this.getPlate().getRealLocationForScreenPoint(this.point2);
      pp1.translate(-0.5, -0.5);
      pp2.translate(-0.5, -0.5);
      this.drawConfiguredLine(pp1.getX(), pp1.getY(), pp2.getX(), pp2.getY(), style, shiftDown);
      this.drawArrowheads(pp1, pp2, style);
   }

   private void drawArrowheads(Point2d pp1, Point2d pp2, AlgorithmicLineStyle style) {
      ArrowheadPlacement placement = this.options.getArrowheadPlacement();
      if (placement == ArrowheadPlacement.START || placement == ArrowheadPlacement.BOTH) {
         this.drawArrowhead(pp2, pp1, style);
      }
      if (placement == ArrowheadPlacement.END || placement == ArrowheadPlacement.BOTH) {
         this.drawArrowhead(pp1, pp2, style);
      }
   }

   private void drawArrowhead(Point2d tail, Point2d tip, AlgorithmicLineStyle style) {
      double dx = tip.getX() - tail.getX();
      double dy = tip.getY() - tail.getY();
      if (dx == 0.0 && dy == 0.0) {
         return;
      }

      double visualDx = dx / CORRECT_FACTOR;
      double theta = Math.atan2(dy, visualDx);
      double headAngle = Math.toRadians(this.options.getArrowheadAngle());
      double headLength = this.options.getArrowheadSize();
      this.drawArrowheadLeg(tip, theta + Math.PI - headAngle, headLength, style);
      this.drawArrowheadLeg(tip, theta + Math.PI + headAngle, headLength, style);
      this.drawCardinalTip(tip, visualDx, dy);
   }

   private void drawArrowheadLeg(Point2d tip, double angle, double headLength, AlgorithmicLineStyle style) {
      double wingX = tip.getX() + Math.cos(angle) * headLength * CORRECT_FACTOR;
      double wingY = tip.getY() + Math.sin(angle) * headLength;
      this.drawConfiguredLine(tip.getX(), tip.getY(), wingX, wingY, style, false);
   }

   private void drawCardinalTip(Point2d tip, double visualDx, double dy) {
      if (!this.options.isCardinalTips()) {
         return;
      }

      double adx = Math.abs(visualDx);
      double ady = Math.abs(dy);
      char tipChar = 0;
      if (adx >= ady * CARDINAL_TIP_RATIO) {
         tipChar = visualDx < 0.0 ? '<' : '>';
      } else if (ady >= adx * CARDINAL_TIP_RATIO) {
         tipChar = dy < 0.0 ? '^' : 'v';
      }

      if (tipChar != 0) {
         this.markPlate.set((int)Math.round(tip.getX()), (int)Math.round(tip.getY()), tipChar);
      }
   }

   private void drawConfiguredLine(
      double x1, double y1, double x2, double y2, AlgorithmicLineStyle style, boolean normalized
   ) {
      if (style != AlgorithmicLineStyle.CHARACTERS) {
         LineAlgorithm.drawLine(this.markPlate, x1, y1, x2, y2, style, normalized);
      } else {
         LineAlgorithm.drawLineBresenham(
            this.markPlate, (int)Math.round(x1), (int)Math.round(y1), (int)Math.round(x2), (int)Math.round(y2), this.getMouseChar()
         );
      }
   }

   private static boolean isKey(int code, int mainKey, int keypadKey) {
      return code == mainKey || code == keypadKey;
   }
}

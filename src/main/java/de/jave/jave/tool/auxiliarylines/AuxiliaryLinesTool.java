package de.jave.jave.tool.auxiliarylines;

import de.jave.jave.JavEApplication;
import de.jave.jave.Point2d;
import de.jave.jave.Tool;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import de.jave.jave.watermark.IWatermarkPainter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.mousecursor.CursorId;
import net.disy.commons.swing.mousecursor.CursorProvider;

public class AuxiliaryLinesTool extends Tool implements IWatermarkPainter {
   private final AuxiliaryLinesModel model = new AuxiliaryLinesModel();
   private Line2d newLine;
   private Point2d lastPoint;
   private Object selectedObject;
   private Line2d popupObject;
   private boolean enabled = false;
   private IInlineToolOptions inlineOptions;

   public AuxiliaryLinesTool(JaveMainPanel mainPanel, JavEApplication application, Filter filter) {
      super(mainPanel, application, filter);
      mainPanel.addWatermarkPainter(this);
      this.model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AuxiliaryLinesTool.this.repaintAll();
         }
      });
   }

   private JPopupMenu createPopupMenu() {
      JPopupMenu popup = new JPopupMenu();
      popup.add(new SmartAction("Delete") {
         @Override
         protected void execute(Component parentComponent) {
            AuxiliaryLinesTool.this.performDelete();
         }
      });
      return popup;
   }

   @Override
   public String getName() {
      return "Auxiliary Lines";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_AUXILIARY_LINES_ICON;
   }

   @Override
   public IInlineToolOptions getInlineOptionsPanel() {
      if (this.inlineOptions == null) {
         final SmartAction clearAction = new SmartAction("Clear") {
            @Override
            protected void execute(Component parentComponent) {
               AuxiliaryLinesTool.this.model.removeAll();
            }
         };
         this.model.addChangeListener(new IChangeListener() {
            @Override
            public void stateChanged() {
               AuxiliaryLinesTool.this.updateClearActionEnabled(clearAction);
            }
         });
         this.updateClearActionEnabled(clearAction);
         final JButton button = new JButton(clearAction);
         this.inlineOptions = () -> button;
      }
      return this.inlineOptions;
   }

   private void updateClearActionEnabled(SmartAction clearAction) {
      clearAction.setEnabled(!this.model.isEmpty());
   }

   @Override
   public void takeToHand() {
      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
      this.application.getToolBar().setAuxiliaryLinesVisible(true);
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   @Override
   public boolean isEnabled() {
      return this.enabled;
   }

   @Override
   public void paint(Graphics g, Point plateOrigin, ColorScheme colorScheme, int charWidth, int charHeight) {
      if (this.enabled) {
         g.setColor(colorScheme.getColorToolHelping());
         boolean active = this.isActiveTool();
         Line2d[] lines = this.model.getLines();

         for (int i = lines.length - 1; i >= 0; i--) {
            Line2d line = lines[i];
            Point p0 = this.getPlate().getScreenPointFor(line.getStartPoint());
            Point p1 = this.getPlate().getScreenPointFor(line.getEndPoint());
            g.drawLine(p0.x, p0.y, p1.x, p1.y);
            if (active) {
               g.drawRect(p0.x - 2, p0.y - 2, 4, 4);
               g.drawRect(p1.x - 2, p1.y - 2, 4, 4);
            }
         }
      }
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.newLine != null) {
         Point p0 = this.getPlate().getScreenPointFor(this.newLine.getStartPoint());
         Point p1 = this.getPlate().getScreenPointFor(this.newLine.getEndPoint());
         g.setColor(colorScheme.getColorToolHelping());
         g.drawLine(p0.x, p0.y, p1.x, p1.y);
      } else {
         Object so = this.selectedObject;
         if (this.selectedObject == null) {
            so = this.popupObject;
         }

         if (so != null) {
            if (so instanceof Point2d) {
               Point p0 = this.getPlate().getScreenPointFor((Point2d)so);
               g.setColor(Color.red);
               g.drawRect(p0.x - 2, p0.y - 2, 4, 4);
            } else {
               Point p0 = this.getPlate().getScreenPointFor(((Line2d)so).getStartPoint());
               Point p1 = this.getPlate().getScreenPointFor(((Line2d)so).getEndPoint());
               g.setColor(Color.red);
               g.drawLine(p0.x, p0.y, p1.x, p1.y);
               g.drawRect(p0.x - 2, p0.y - 2, 4, 4);
               g.drawRect(p1.x - 2, p1.y - 2, 4, 4);
            }
         }
      }
   }

   private Object getObjectNear(Point2d p) {
      Object o = this.model.getPointNear(p);
      if (o == null) {
         o = this.model.getLineNear(p);
      }

      return o;
   }

   @Override
   public void mouseMoved(Point point, Point location, MouseEvent evt) {
      super.mouseMoved(point, location, evt);
      Point2d pp1 = this.getPlate().getRealLocationForScreenPoint(point);
      Object o = this.getObjectNear(pp1);
      if (this.selectedObject != o) {
         this.selectedObject = o;
         if (this.selectedObject != null) {
            this.setCursor(Cursor.getPredefinedCursor(13));
         } else {
            this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
         }

         this.repaintCursor();
      }
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (this.popupObject != null) {
         this.popupObject = null;
         this.repaintCursor();
      } else if (this.tryShowPopup(point, evt)) {
         // popup shown
      } else if (this.selectedObject != null) {
         Point2d pp1 = this.getPlate().getRealLocationForScreenPoint(point);
         this.lastPoint = pp1;
      } else if (this.newLine == null) {
         Point2d pp1 = this.getPlate().getRealLocationForScreenPoint(point);
         this.newLine = new Line2d(pp1, pp1);
      }
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      if (evt.isPopupTrigger()) {
         // Windows/Linux fire popup trigger on release; cancel any in-progress
         // line stub from the matching press so a right-click doesn't draw.
         this.newLine = null;
         this.tryShowPopup(point, evt);
         return;
      }
      if (this.newLine != null) {
         if (this.newLine.length() > 2.0) {
            if (!this.enabled) {
               this.application.getToolBar().setAuxiliaryLinesVisible(true);
            }

            this.model.add(this.newLine);
         } else {
            this.repaintCursor();
         }

         this.newLine = null;
      }
   }

   private boolean tryShowPopup(Point point, MouseEvent evt) {
      if (!evt.isPopupTrigger()) {
         return false;
      }
      if (!(this.selectedObject instanceof Line2d)) {
         return false;
      }
      this.popupObject = (Line2d)this.selectedObject;
      this.selectedObject = null;
      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
      JPopupMenu menu = this.createPopupMenu();
      menu.show(this.getPlate(), point.x, point.y);
      return true;
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      if (this.newLine != null) {
         Point2d pp1 = this.getPlate().getRealLocationForScreenPoint(point);
         this.newLine.setEndPoint(pp1);
         this.repaintCursor();
      } else if (this.selectedObject != null) {
         if (this.selectedObject instanceof Point2d) {
            Point2d pp1 = this.getPlate().getRealLocationForScreenPoint(point);
            ((Point2d)this.selectedObject).moveTo(pp1);
            this.repaintAll();
         } else {
            Point2d pp1 = this.getPlate().getRealLocationForScreenPoint(point);
            double dx = pp1.getX() - this.lastPoint.getX();
            double dy = pp1.getY() - this.lastPoint.getY();
            ((Line2d)this.selectedObject).translate(dx, dy);
            this.lastPoint = pp1;
            this.repaintAll();
         }
      }
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      if (code == 27 && this.newLine != null) {
         this.newLine = null;
         this.repaintCursor();
      } else if (code == 127 && this.selectedObject != null) {
         this.performDelete();
      }
   }

   private void performDelete() {
      Line2d toRemove = null;
      if (this.popupObject != null) {
         toRemove = this.popupObject;
      } else if (this.selectedObject instanceof Line2d) {
         toRemove = (Line2d)this.selectedObject;
      } else if (this.selectedObject instanceof Point2d) {
         Point2d endpoint = (Point2d)this.selectedObject;
         for (Line2d line : this.model.getLines()) {
            if (line.getStartPoint() == endpoint || line.getEndPoint() == endpoint) {
               toRemove = line;
               break;
            }
         }
      }
      if (toRemove != null) {
         this.model.remove(toRemove);
      }
      this.popupObject = null;
      this.selectedObject = null;
      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
   }
}

package de.jave.ascii.plate.ruler;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.Timer;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.ObjectUtilities;

public final class RulerComponent extends JComponent {
   private static final int UPDATE_INTERVAL_MILLISECONDS = 30;
   private final IRulerRenderingStrategy renderingStrategy;
   private final AsciiRulerProperties properties;
   private final JComponent mainComponent;
   private Point lastPaintedMousePoint;
   private Point lastRecognizedMousePoint;
   private IRulerXorPainter xorPainter;
   private final Timer timer;

   public RulerComponent(final IRulerRenderingStrategy renderingStrategy, JComponent mainComponent, final AsciiRulerProperties properties) {
      Ensure.ensureArgumentNotNull(renderingStrategy);
      Ensure.ensureArgumentNotNull(mainComponent);
      Ensure.ensureArgumentNotNull(properties);
      ActionListener updateCursorLocationListener = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (!ObjectUtilities.equals(RulerComponent.this.lastPaintedMousePoint, RulerComponent.this.lastRecognizedMousePoint)) {
               Point previousPoint = RulerComponent.this.lastPaintedMousePoint;
               RulerComponent.this.lastPaintedMousePoint = RulerComponent.this.lastRecognizedMousePoint;
               if (properties.isShowMouseLocation()
                  && renderingStrategy.isRelevantMouseLocationChange(previousPoint, RulerComponent.this.lastRecognizedMousePoint)) {
                  RulerComponent.this.setXorPainter(
                     new RulerXorPainter(
                        renderingStrategy, RulerComponent.this.lastRecognizedMousePoint, properties.getBackgroundColor(), properties.getForegroundColor()
                     )
                  );
               }
            }
         }
      };
      this.timer = new Timer(30, updateCursorLocationListener);
      this.timer.start();
      this.setVisible(properties.isRulerVisible(renderingStrategy.getLayoutDirection()));
      this.renderingStrategy = renderingStrategy;
      this.properties = properties;
      this.mainComponent = mainComponent;
      this.properties.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            if (RulerComponent.this.isVisible() != properties.isRulerVisible(renderingStrategy.getLayoutDirection())) {
               RulerComponent.this.setVisible(properties.isRulerVisible(renderingStrategy.getLayoutDirection()));
            }

            RulerComponent.this.repaint();
         }
      });
      mainComponent.addMouseMotionListener(new MouseMotionListener() {
         @Override
         public void mouseMoved(MouseEvent e) {
            RulerComponent.this.setNewMouseLocation(e.getPoint());
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            RulerComponent.this.setNewMouseLocation(e.getPoint());
         }
      });
      mainComponent.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseExited(MouseEvent e) {
            RulerComponent.this.setNewMouseLocation(null);
         }
      });
   }

   public void dispose() {
      this.timer.stop();
   }

   private void setNewMouseLocation(Point mousePoint) {
      this.lastRecognizedMousePoint = mousePoint;
   }

   private void setXorPainter(IRulerXorPainter xorPainter) {
      Graphics graphics = this.getGraphics();
      if (this.xorPainter != null) {
         this.paintCurrentXorPainter(graphics);
      }

      this.xorPainter = xorPainter;
      this.paintCurrentXorPainter(graphics);
      graphics.dispose();
   }

   @Override
   public final Dimension getPreferredSize() {
      return !this.properties.isRulerVisible(this.renderingStrategy.getLayoutDirection())
         ? new Dimension(0, 0)
         : this.renderingStrategy.getPreferredSize(this.properties, this.mainComponent.getSize());
   }

   @Override
   protected final void paintComponent(Graphics g) {
      if (this.properties.isRulerVisible(this.renderingStrategy.getLayoutDirection())) {
         this.renderingStrategy.paintRuler((Graphics2D)g, this.getSize(), this.properties);
      }

      this.paintCurrentXorPainter(g);
   }

   private final void paintCurrentXorPainter(Graphics graphics) {
      if (this.xorPainter != null) {
         this.xorPainter.paintXor(graphics, this.getSize());
      }
   }
}

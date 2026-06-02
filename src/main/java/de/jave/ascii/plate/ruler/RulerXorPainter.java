package de.jave.ascii.plate.ruler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.util.EventDispatchThreadUtilities;

public class RulerXorPainter implements IRulerXorPainter {
   private final Point mousePoint;
   private final IRulerRenderingStrategy renderingStrategy;
   private final Color foregroundColor;
   private final Color backgroundColor;

   public RulerXorPainter(IRulerRenderingStrategy renderingStrategy, Point mousePoint, Color backgroundColor, Color foregroundColor) {
      Ensure.ensureArgumentNotNull(renderingStrategy);
      Ensure.ensureArgumentNotNull(backgroundColor);
      Ensure.ensureArgumentNotNull(foregroundColor);
      this.renderingStrategy = renderingStrategy;
      this.mousePoint = mousePoint;
      this.backgroundColor = backgroundColor;
      this.foregroundColor = foregroundColor;
   }

   @Override
   public void paintXor(Graphics g, Dimension size) {
      EventDispatchThreadUtilities.ensureIsEventDispatchThread();
      if (this.mousePoint != null) {
         g.setColor(this.backgroundColor);
         g.setXORMode(this.foregroundColor);
         this.renderingStrategy.renderMouseLocation(g, size, this.mousePoint);
         g.setPaintMode();
      }
   }
}

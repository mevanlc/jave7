package de.jave.ascii.plate.ruler;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import net.dizzy.commons.swing.layout.util.LayoutDirection;

public interface IRulerRenderingStrategy {
   void renderMouseLocation(Graphics var1, Dimension var2, Point var3);

   Dimension getPreferredSize(AsciiRulerProperties var1, Dimension var2);

   boolean isRelevantMouseLocationChange(Point var1, Point var2);

   LayoutDirection getLayoutDirection();

   void paintRuler(Graphics2D var1, Dimension var2, AsciiRulerProperties var3);
}

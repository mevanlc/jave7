package de.jave.jave.watermark;

import de.jave.jave.preferences.ColorScheme;
import java.awt.Graphics;
import java.awt.Point;

public interface IWatermarkPainter {
   void paint(Graphics var1, Point var2, ColorScheme var3, int var4, int var5);

   void setEnabled(boolean var1);

   boolean isEnabled();
}

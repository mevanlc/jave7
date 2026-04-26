package de.jave.gui.layout;

import java.awt.Dimension;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import net.disy.commons.swing.layout.util.LayoutUtilities;

/**
 * Measures the maximum natural preferred width across a list of inline
 * tool-options panels. Used at startup to set a constant width for the
 * tool selector bar's inline-options host so the bar doesn't jump as
 * the active tool changes.
 *
 * <p>Recipe: build a hidden, undecorated {@link JFrame}, add the panel,
 * {@code pack()}, read the panel's preferred width, dispose. {@code pack()}
 * triggers {@code addNotify()} and {@code validate()} even when the
 * frame is never made visible — peers get created, fonts resolve, layout
 * runs.
 *
 * <p>If a real-world panel ever returns 0 (HTML labels, exotic custom
 * components), the documented escape hatch is to position the measuring
 * frame far off-screen, briefly {@code setVisible(true)} then
 * {@code setVisible(false)}, dispose. Not implemented here yet — only
 * needed if observed.
 */
public final class InlineOptionsWidthMeasurer {
   private static final int PADDING_PX = 4;

   private InlineOptionsWidthMeasurer() {}

   public static int measureMaxWidth(List<JComponent> panels) {
      int max = 0;
      for (JComponent p : panels) {
         JFrame f = new JFrame();
         f.setUndecorated(true);
         f.add(p);
         f.pack();
         Dimension d = p.getPreferredSize();
         if (d != null && d.width > max) {
            max = d.width;
         }
         f.remove(p);
         f.dispose();
      }
      return max + LayoutUtilities.getDpiAdjusted(PADDING_PX);
   }
}

package de.jave.gui.splash;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.model.listener.IChangeListener;

public class SplashComponentUtilities {
   private static final Font FONT = new Font("Dialog", 0, 11);

   public static JComponent createTextOverlayedComponent(final Icon baseIcon, final Rectangle labelArea, final ObjectModel<String> textModel) {
      Icon icon = new Icon() {
         @Override
         public void paintIcon(Component c, Graphics g, int x, int y) {
            baseIcon.paintIcon(c, g, x, y);
            Rectangle actualLabelArea = new Rectangle(labelArea);
            actualLabelArea.translate(x, y);
            SplashComponentUtilities.renderLabel(g, textModel.getValue(), actualLabelArea);
         }

         @Override
         public int getIconWidth() {
            return baseIcon.getIconWidth();
         }

         @Override
         public int getIconHeight() {
            return baseIcon.getIconHeight();
         }
      };
      final JLabel label = new JLabel(icon);
      textModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            label.repaint();
         }
      });
      return label;
   }

   public static void renderLabel(Graphics g, String text, Rectangle progressRectangle) {
      g.setColor(Color.WHITE);
      g.fillRect(progressRectangle.x, progressRectangle.y, progressRectangle.width, progressRectangle.height);
      g.setColor(Color.BLACK);
      g.setFont(FONT);
      int textX = progressRectangle.x + 2;
      FontMetrics fontMetrics = g.getFontMetrics(FONT);
      int fontHeight = fontMetrics.getHeight();
      int ascent = fontMetrics.getAscent();
      int delta = progressRectangle.height - fontHeight;
      int textY = progressRectangle.y + delta / 2 + ascent;
      g.drawString(text, textX, textY);
   }
}

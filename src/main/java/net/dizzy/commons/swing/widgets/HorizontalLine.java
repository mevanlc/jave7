package net.dizzy.commons.swing.widgets;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JSeparator;

public class HorizontalLine extends JSeparator {
   private Insets margin = new Insets(0, 0, 0, 0);

   public HorizontalLine() {
   }

   public HorizontalLine(int widthHint) {
      setPreferredSize(new Dimension(widthHint, getPreferredSize().height));
   }

   public void setMargin(Insets margin) {
      this.margin = margin;
      revalidate();
      repaint();
   }

   @Override
   public Insets getInsets() {
      return margin;
   }
}

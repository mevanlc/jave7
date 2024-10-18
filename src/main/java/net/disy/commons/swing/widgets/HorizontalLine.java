package net.disy.commons.swing.widgets;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JComponent;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.color.SwingColors;

public class HorizontalLine extends JComponent {
   private Insets margin = new Insets(0, 0, 0, 0);

   public HorizontalLine() {
      this(100);
   }

   public HorizontalLine(int preferredWidth) {
      this.setPreferredSize(new Dimension(preferredWidth, 2));
   }

   public HorizontalLine(Insets margin) {
      this.setMargin(margin);
   }

   @Override
   public Dimension getPreferredSize() {
      Dimension dimension = super.getPreferredSize();
      return new Dimension(dimension.width + this.margin.left + this.margin.right, dimension.height + this.margin.top + this.margin.bottom);
   }

   @Override
   protected void paintComponent(Graphics g) {
      int y = this.margin.top + (this.getSize().height - 2 - this.margin.top - this.margin.bottom) / 2;
      g.setColor(SwingColors.getControlShadowColor());
      g.drawLine(this.margin.left, y, this.getSize().width - this.margin.right, y);
      g.setColor(SwingColors.getControlLtHighlightColor());
      g.drawLine(this.margin.left, y + 1, this.getSize().width - this.margin.right, y + 1);
   }

   public void setMargin(Insets margin) {
      Ensure.ensureArgumentNotNull(margin);
      this.margin = margin;
   }
}

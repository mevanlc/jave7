package net.disy.commons.swing.dialog.color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import net.disy.commons.swing.color.widgets.ColorModel;

public class ColorChooserLabel extends AbstractColorChoosingComponent {
   public ColorChooserLabel(ColorModel model) {
      this(model, new DefaultColorChooserConfiguration());
   }

   public ColorChooserLabel(ColorModel model, IColorChooserConfiguration configuration) {
      super(model, configuration);
   }

   @Override
   protected JComponent createContent() {
      JComponent label = new JComponent() {
         @Override
         public Dimension getPreferredSize() {
            return new Dimension(50, 20);
         }

         @Override
         public void paint(Graphics g) {
            ColorChooserLabel.this.paintColorRectangle(g, 0, 0, this.getSize(), this.isEnabled());
            super.paint(g);
         }
      };
      label.setOpaque(false);
      label.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseReleased(MouseEvent e) {
            if (e.getClickCount() == 2 && !e.isMetaDown()) {
               ColorChooserLabel.this.performColorChooseDialog();
            }
         }
      });
      label.addKeyListener(new KeyAdapter() {
         @Override
         public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 113) {
               ColorChooserLabel.this.performColorChooseDialog();
            }
         }
      });
      return label;
   }
}

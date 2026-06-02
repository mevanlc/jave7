package de.jave.asciimation.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;

public class FilmStripBorder extends JComponent {
   private final AnimationEditorModel model;

   public FilmStripBorder(AnimationEditorModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      IChangeListener repaintChangeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            FilmStripBorder.this.repaint();
         }
      };
      model.addChangeListener(repaintChangeListener);
      model.getCurrentFrameIndexModel().addChangeListener(repaintChangeListener);
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(100, 15);
   }

   @Override
   public Dimension getMinimumSize() {
      return new Dimension(20, 15);
   }

   @Override
   public Dimension getMaximumSize() {
      return new Dimension(1000, 15);
   }

   @Override
   protected void paintComponent(Graphics g) {
      Dimension d = this.getSize();
      g.setColor(Color.darkGray);
      g.fillRect(0, 0, d.width, d.height);
      g.setColor(Color.lightGray);

      for (int i = 0; i <= d.width / 15 + 1; i++) {
         g.fillRoundRect(i * 15 - 4, d.height / 2 - 4, 7, 8, 3, 3);
      }

      String label = this.generateLabel();
      int w = g.getFontMetrics().stringWidth(label);
      g.setColor(Color.white);
      g.fillRect(d.width / 2 - w / 2 - 2, 1, w + 4, d.height - 3);
      g.setColor(Color.gray);
      g.drawRect(d.width / 2 - w / 2 - 2, 1, w + 4, d.height - 3);
      g.setColor(Color.black);
      g.drawString(label, d.width / 2 - w / 2, d.height - 3);
   }

   private String generateLabel() {
      int frameCount = this.model.getAnimationFile().getFrameCount();
      return frameCount == 0 ? "--" : this.model.getCurrentFrameIndexModel().getCurrentFrameIndex() + 1 + " / " + frameCount;
   }
}

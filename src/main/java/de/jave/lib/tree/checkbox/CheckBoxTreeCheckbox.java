package de.jave.lib.tree.checkbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

public class CheckBoxTreeCheckbox extends JComponent {
   private CheckBoxNodeRenderState state;
   private ActionListener checkListener;
   private boolean selected;

   public CheckBoxTreeCheckbox(CheckBoxNodeRenderState state) {
      this.setState(state);
   }

   public CheckBoxTreeCheckbox() {
      this(new CheckBoxNodeRenderState(VisibleCheckBoxState.NOT_SELECTED, false));
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            if (!e.isMetaDown() && CheckBoxTreeCheckbox.this.checkListener != null) {
               CheckBoxTreeCheckbox.this.checkListener.actionPerformed(new ActionEvent(this, 0, null));
            }
         }
      });
   }

   public void setState(CheckBoxNodeRenderState state) {
      this.state = state;
      this.selected = state.isSelected();
      this.repaint();
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(16, 16);
   }

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   @Override
   public void update(Graphics g) {
      this.paint(g);
   }

   @Override
   public void paint(Graphics g) {
      if (this.state.isPartialSelected()) {
         g.setColor(Color.lightGray);
      } else {
         g.setColor(Color.white);
      }

      g.fillRect(3, 4, 9, 9);
      if (this.state.isEnabled()) {
         g.setColor(Color.black);
      } else {
         g.setColor(Color.gray);
      }

      g.drawRect(1, 2, 12, 12);
      g.drawRect(2, 3, 10, 10);
      if (this.state.isSelected() || this.state.isPartialSelected()) {
         if (this.state.isPartialSelected() || !this.state.isEnabled()) {
            g.setColor(Color.gray);
         }

         g.drawLine(6, 9, 10, 5);
         g.drawLine(6, 10, 10, 6);
         g.drawLine(6, 11, 10, 7);
         g.drawLine(4, 7, 4, 9);
         g.drawLine(5, 8, 5, 10);
      }
   }

   public void addActionListener(CheckBoxCheckListener checkListener) {
      this.checkListener = checkListener;
   }

   public boolean isSelected() {
      return this.selected;
   }
}

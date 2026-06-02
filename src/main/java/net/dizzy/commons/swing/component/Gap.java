package net.dizzy.commons.swing.component;

import java.awt.Dimension;

import javax.swing.JComponent;

public class Gap extends JComponent {
   public Gap(int width, int height) {
      Dimension size = new Dimension(width, height);
      setMinimumSize(size);
      setPreferredSize(size);
   }
}

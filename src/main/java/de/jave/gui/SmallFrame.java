package de.jave.gui;

import java.awt.Container;
import java.awt.Frame;
import javax.swing.JComponent;
import javax.swing.JWindow;

public class SmallFrame extends JWindow {
   private final SmallFrameTitleBar titleBar;

   public SmallFrame(Frame frame, String title, JComponent content) {
      super(frame);
      this.titleBar = new SmallFrameTitleBar(title);
      Container contentPane = this.getContentPane();
      contentPane.add(this.titleBar, "North");
      contentPane.add(new SmallFrameLeftBorder(), "West");
      contentPane.add(new SmallFrameRightBorder(), "East");
      contentPane.add(new SmallFrameBottomBorder(), "South");
      contentPane.add(content, "Center");
   }

   public void setTitle(String title) {
      this.titleBar.setTitle(title);
   }

   public String getTitle() {
      return this.titleBar.getTitle();
   }
}

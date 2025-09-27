package net.disy.commons.swing.dialog.core;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import net.disy.commons.core.util.Ensure;

public class SwingDialog implements ISwingFrameOrDialog {
   private final JDialog dialog;

   public SwingDialog(JDialog dialog) {
      Ensure.ensureArgumentNotNull(dialog);
      this.dialog = dialog;
   }

   @Override
   public void setTitle(String title) {
      this.dialog.setTitle(title);
   }

   @Override
   public JRootPane getRootPane() {
      return this.dialog.getRootPane();
   }

   @Override
   public void pack() {
      this.dialog.pack();
   }

   @Override
   public void setModal(boolean modal) {
      this.dialog.setModal(modal);
   }

   @Override
   public Container getContentPane() {
      return this.dialog.getContentPane();
   }

   @Override
   public void setDefaultCloseOperation(int closeOperation) {
      this.dialog.setDefaultCloseOperation(closeOperation);
   }

   @Override
   public void addWindowListener(WindowListener windowListener) {
      this.dialog.addWindowListener(windowListener);
   }

   @Override
   public void removeWindowListener(WindowListener windowListener) {
      this.dialog.removeWindowListener(windowListener);
   }

   @Override
   public void dispose() {
      this.dialog.dispose();
      this.dialog.getContentPane().removeAll();
   }

   @Override
   public void validate() {
      this.dialog.validate();
   }

   @Override
   public void repaint() {
      this.dialog.repaint();
   }

   @Override
   public Window getWindow() {
      return this.dialog;
   }

   @Override
   public void setResizable(boolean resizable) {
      this.dialog.setResizable(resizable);
   }

   @Override
   public void show() {
      this.dialog.show();
   }

   @Override
   public void setVisible(boolean visible) {
      this.dialog.setVisible(visible);
   }

   @Override
   public boolean isVisible() {
      return this.dialog.isVisible();
   }
}

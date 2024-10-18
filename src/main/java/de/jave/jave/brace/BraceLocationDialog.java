package de.jave.jave.brace;

import de.jave.gui.dialog.JDialogFactory;
import de.jave.gui.layout.Gap;
import de.jave.lib.gui.GuiUtilities;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import net.disy.commons.swing.action.SmartAction;

public class BraceLocationDialog {
   public static final int NONE = -1;
   public static final int TOP = 0;
   public static final int BOTTOM = 1;
   public static final int LEFT = 2;
   public static final int RIGHT = 3;
   private int location = -1;
   private final JDialog dialog;

   public BraceLocationDialog(Component parent) {
      this.dialog = JDialogFactory.createJDialog(parent, "Braces", true);
      this.dialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            BraceLocationDialog.this.location = -1;
            BraceLocationDialog.this.dialog.dispose();
         }
      });
      JButton bTop = new JButton(new SmartAction("Top") {
         @Override
         protected void execute(Component parentComponent) {
            BraceLocationDialog.this.location = 0;
            BraceLocationDialog.this.dialog.dispose();
         }
      });
      JButton bBottom = new JButton(new SmartAction("Bottom") {
         @Override
         protected void execute(Component parentComponent) {
            BraceLocationDialog.this.location = 1;
            BraceLocationDialog.this.dialog.dispose();
         }
      });
      JButton bRight = new JButton(new SmartAction("Right") {
         @Override
         protected void execute(Component parentComponent) {
            BraceLocationDialog.this.location = 3;
            BraceLocationDialog.this.dialog.dispose();
         }
      });
      JButton bLeft = new JButton(new SmartAction("Left") {
         @Override
         protected void execute(Component parentComponent) {
            BraceLocationDialog.this.location = 2;
            BraceLocationDialog.this.dialog.dispose();
         }
      });
      this.dialog.getContentPane().setLayout(new GridLayout(0, 3, 0, 3));
      this.dialog.getContentPane().add(new Gap());
      this.dialog.getContentPane().add(bTop);
      this.dialog.getContentPane().add(new Gap());
      this.dialog.getContentPane().add(bLeft);
      this.dialog.getContentPane().add(new JLabel("<html>Choose<br>location</html>", 0));
      this.dialog.getContentPane().add(bRight, "East");
      this.dialog.getContentPane().add(new Gap());
      this.dialog.getContentPane().add(bBottom, "South");
      this.dialog.pack();
      this.dialog.setResizable(false);
      GuiUtilities.centerToParent(this.dialog);
      this.dialog.setVisible(true);
   }

   public int getBraceLocation() {
      return this.location;
   }

   public JDialog getDialog() {
      return this.dialog;
   }
}

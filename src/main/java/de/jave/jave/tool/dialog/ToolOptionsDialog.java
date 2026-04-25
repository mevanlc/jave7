package de.jave.jave.tool.dialog;

import de.jave.gui.SmallFrame;
import de.jave.gui.dialog.JDialogFactory;
import de.jave.jave.JavEApplication;
import de.jave.jave.Tool;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;

public class ToolOptionsDialog {
   private Tool tool;
   private final JavEApplication jave;
   private final SmallFrame smallFrame;
   private final JDialog dialog;
   private final Container contentPane;
   private final BooleanModel model;
   private JComponent currentDisplayed;
   private JComponent hintLabelPanel;

   public ToolOptionsDialog(JavEApplication jave, boolean small, BooleanModel model) {
      Ensure.ensureArgumentNotNull(jave);
      Ensure.ensureArgumentNotNull(model);
      this.jave = jave;
      this.model = model;
      if (small) {
         JPanel panel = new JPanel();
         this.contentPane = panel;
         this.smallFrame = new SmallFrame(jave.getFrame(), "", panel);
         this.dialog = null;
      } else {
         this.dialog = JDialogFactory.createJDialog(jave.getFrame(), "", false);
         this.dialog.getContentPane().setLayout(new BorderLayout());
         this.dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               ToolOptionsDialog.this.model.setValue(false);
            }
         });
         this.contentPane = this.dialog.getContentPane();
         this.smallFrame = null;
      }

      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ToolOptionsDialog.this.updateVisibility();
         }
      });
      this.updateVisibility();
   }

   private void updateVisibility() {
      this.getWindow().setVisible(this.model.getValue());
      this.jave.getMainPanel().requestFocus();
   }

   public void setTool(Tool tool) {
      JComponent desired = tool.getInlineOptionsPanel() != null
            ? this.getOrBuildHintLabelPanel()
            : tool.getOptionsComponent();
      this.tool = tool;
      if (this.currentDisplayed != desired) {
         if (this.smallFrame != null) {
            this.contentPane.removeAll();
            this.contentPane.add(desired, "Center");
            this.smallFrame.setTitle(tool.getName());
            this.smallFrame.pack();
         } else {
            this.dialog.setEnabled(false);
            this.contentPane.removeAll();
            this.contentPane.add(desired, "Center");
            this.dialog.setTitle(tool.getName());
            this.dialog.pack();
            this.dialog.setEnabled(true);
            this.jave.toFront();
            this.jave.getMainPanel().requestFocus();
         }
         this.currentDisplayed = desired;
      }
   }

   private JComponent getOrBuildHintLabelPanel() {
      if (this.hintLabelPanel == null) {
         JPanel p = new JPanel(new BorderLayout());
         JLabel l = new JLabel("Options shown in the toolbar.");
         l.setHorizontalAlignment(SwingConstants.CENTER);
         p.add(l, BorderLayout.CENTER);
         this.hintLabelPanel = p;
      }
      return this.hintLabelPanel;
   }

   public Window getWindow() {
      return this.smallFrame != null ? this.smallFrame : this.dialog;
   }

   public Point getLocation() {
      return this.getWindow().getLocation();
   }

   public void toFront() {
      this.getWindow().toFront();
   }

   public void pack() {
      this.getWindow().pack();
   }

   public Dimension getSize() {
      return this.getWindow().getSize();
   }

   public void show() {
      this.setVisible(true);
   }

   public void setVisible(boolean what) {
      this.getWindow().setVisible(what && this.model.getValue());
   }

   public void setLocation(Point location) {
      int x = location.x;
      int y = location.y;
      this.getWindow().setLocation(x, y);
   }
}

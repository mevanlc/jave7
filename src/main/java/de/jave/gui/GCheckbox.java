package de.jave.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.layout.grid.GridDialogLayout;

public class GCheckbox {
   private final BooleanModel selectionModel;
   private final JLabel label;
   private final JCheckBox checkBox;
   private final JComponent content;

   public GCheckbox(final BooleanModel selectionModel, Icon icon) {
      Ensure.ensureArgumentNotNull(selectionModel);
      this.selectionModel = selectionModel;
      this.checkBox = new JCheckBox("", selectionModel.getValue());
      selectionModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            GCheckbox.this.checkBox.setSelected(selectionModel.getValue());
         }
      });
      this.checkBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            GCheckbox.this.toggle();
         }
      });
      JPanel panel = new JPanel(new GridDialogLayout(2, true, 0, 0));
      panel.add(this.checkBox);
      this.label = new JLabel(icon);
      this.label.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseReleased(MouseEvent e) {
            GCheckbox.this.toggle();
         }
      });
      panel.add(this.label);
      this.content = panel;
   }

   public JComponent getContent() {
      return this.content;
   }

   private void toggle() {
      this.selectionModel.setValue(!this.selectionModel.getValue());
   }

   public void setToolTipText(String string) {
      this.content.setToolTipText(string);
      this.checkBox.setToolTipText(string);
      this.label.setToolTipText(string);
   }
}

package net.disy.commons.swing.showhide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;

public class ShowHidePanel {
   private JLabel label;
   private final JComponent content;

   public ShowHidePanel(BooleanModel model, String label, JComponent hideableContent) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(this.createTitle(model, label), "North");
      ShowHideContentPanel showHideContentPanel = new ShowHideContentPanel(model, hideableContent);
      showHideContentPanel.setBorder(new EmptyBorder(4, 18, 4, 4));
      panel.add(showHideContentPanel.getContent(), "Center");
      this.content = panel;
   }

   private JComponent createTitle(final BooleanModel model, String labelText) {
      JPanel panel = new JPanel(new GridDialogLayout(2, false, 0, 0));
      panel.add(new ShowHideButton(model).getContent());
      this.label = new JLabel(labelText);
      this.label.setFont(this.label.getFont().deriveFont(1));
      this.label.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            model.setValue(!model.getValue());
         }
      });
      panel.add(this.label);
      return panel;
   }

   public JComponent getContent() {
      return this.content;
   }

   public void setColor(Color color) {
      this.label.setForeground(color);
   }
}

package de.jave.figlet.swing.fontchooser;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.dizzy.commons.swing.events.AbstractDocumentChangeListener;

public class FontNameFilterPanel {
   private final JComponent content;
   private final JTextField filterTextField;
   private final JButton clearButton;
   private boolean enabled;
   private final JLabel label = new JLabel("Name contains:");

   public FontNameFilterPanel(final ActionListener listener) {
      this.filterTextField = new JTextField(12);
      this.clearButton = new JButton("Clear");
      this.clearButton.setToolTipText("Clear the search criteria and show all fonts");
      this.filterTextField.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            FontNameFilterPanel.this.updateEnabled();
            listener.actionPerformed(null);
         }
      });
      this.clearButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FontNameFilterPanel.this.filterTextField.setText("");
         }
      });
      JPanel filterPanel = new JPanel(new FlowLayout(0, 5, 0));
      filterPanel.add(this.label);
      filterPanel.add(this.filterTextField);
      filterPanel.add(this.clearButton);
      this.content = filterPanel;
      this.updateEnabled();
   }

   public JComponent getComponent() {
      return this.content;
   }

   private void updateEnabled() {
      this.clearButton.setEnabled(this.enabled && this.filterTextField.getText().length() > 0);
      this.filterTextField.setEnabled(this.enabled);
      this.label.setEnabled(this.enabled);
   }

   public String getFilterText() {
      return this.filterTextField.getText();
   }

   public void setEnabled(boolean b) {
      this.enabled = b;
      this.updateEnabled();
   }

   public void clear() {
      this.filterTextField.setText("");
   }
}

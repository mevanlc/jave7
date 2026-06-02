package net.dizzy.commons.swing.border;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class TitledPanel extends JPanel {
   public TitledPanel(String title, JComponent content) {
      super(new BorderLayout());
      setBorder(new TitledBorder(title));
      add(content, BorderLayout.CENTER);
   }
}

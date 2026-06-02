package net.dizzy.commons.swing.showhide;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.swing.component.IComponentContainer;

public class ShowHideContentPanel implements IComponentContainer {
   private final JPanel panel = new JPanel(new BorderLayout());

   public ShowHideContentPanel(BooleanModel model, JComponent content) {
      panel.add(content, BorderLayout.CENTER);
      panel.setVisible(Boolean.TRUE.equals(model.getValue()));
      model.addChangeListener(() -> panel.setVisible(Boolean.TRUE.equals(model.getValue())));
   }

   @Override public JComponent getContent() { return panel; }
}

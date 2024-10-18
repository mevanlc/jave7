package net.disy.commons.swing.showhide;

import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;

public class ShowHideContentPanel {
   private final JPanel panel;
   private final BooleanModel model;
   private final JComponent hideableContent;
   private Border border;

   public ShowHideContentPanel(BooleanModel model, JComponent hideableContent) {
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(hideableContent);
      this.hideableContent = hideableContent;
      this.model = model;
      this.panel = new JPanel(new GridLayout(1, 1));
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ShowHideContentPanel.this.updateHideableContentVisible();
         }
      });
      this.updateHideableContentVisible();
   }

   private void updateHideableContentVisible() {
      if (this.model.getValue() != (this.panel.getComponentCount() == 1)) {
         this.panel.removeAll();
         if (this.model.getValue()) {
            this.panel.add(this.hideableContent);
         }

         this.updateBorder();
         this.panel.revalidate();
         this.panel.doLayout();
         this.panel.repaint();
      }
   }

   public JComponent getContent() {
      return this.panel;
   }

   public void setBorder(Border border) {
      this.border = border;
      this.updateBorder();
   }

   private void updateBorder() {
      this.panel.setBorder(this.model.getValue() ? this.border : null);
   }
}
